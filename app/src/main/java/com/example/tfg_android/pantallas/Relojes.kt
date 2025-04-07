package com.example.tfg_android.pantallas
import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.zxing.integration.android.IntentIntegrator


// Escanear QR

@Composable
fun EscanearQR(
    onMacScanned: (String) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val activity = context as? Activity
    var scannedMac by remember { mutableStateOf<String?>(null) }
    var hasLaunched by remember { mutableStateOf(false) }

    val qrResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val scanContent = data?.getStringExtra("SCAN_RESULT")
            scanContent?.let {
                println("Resultado del escaneo QR (MAC): $it")
                scannedMac = it
                onMacScanned(it) // Enviamos la MAC al padre para asociarla
            }
        } else {
            onBack() // Si se cancela el escaneo, volvemos
        }
    }

    val startQRScanner = {
        val integrator = IntentIntegrator(activity)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Escanea un código QR")
        integrator.setBeepEnabled(true)
        integrator.setBarcodeImageEnabled(true)
        val intent = integrator.createScanIntent()
        qrResultLauncher.launch(intent)
    }

    // Lanzar escáner al entrar en la pantalla
    LaunchedEffect(Unit) {
        if (!hasLaunched) {
            hasLaunched = true
            startQRScanner()
        }
    }

    // Puedes mostrar algo mientras se escanea o si falla
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Esperando escaneo...", color = Color.White)

            scannedMac?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Text("MAC Escaneada: $it", color = Color.Green)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Volver", color = Color.White)
            }
        }
    }
}



//  1.1 asociar relojes
@Composable
fun AsociarReloj(onBack: () -> Unit) {
    var isScanningQR by remember { mutableStateOf(false) }
    var macEscaneada by remember { mutableStateOf<String?>(null) }

    if (isScanningQR) {
        EscanearQR(
            onMacScanned = {
                macEscaneada = it
                isScanningQR = false // Volver a la pantalla de asociación
            },
            onBack = { isScanningQR = false }
        )
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(16.dp)
                .imePadding()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Text(
                        "Asociar Reloj a cliente con QR",
                        color = Color.White,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(16.dp)
                    )

                    macEscaneada?.let {
                        Text("MAC asociada: $it", color = Color.Green)
                        // Aquí puedes hacer la lógica para asociar ese reloj a un cliente
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { isScanningQR = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF24BDFF))
                    ) {
                        Text("ESCANEAR QR", color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = onBack,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text("Volver", color = Color.White)
                    }
                }
            }
        }
    }
}



@Composable
fun DesAsociarReloj(onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
            .imePadding()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    "Desasociar Reloj de cliente",
                    color = Color.White,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { onBack() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Volver", color = Color.White)
                }
            }
        }
    }
}