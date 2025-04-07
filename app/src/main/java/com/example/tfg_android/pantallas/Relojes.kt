package com.example.tfg_android.pantallas
import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
fun EscanearQR(onBack: () -> Unit) {
    val context = LocalContext.current
    val activity = context as? Activity

    // Usamos rememberLauncherForActivityResult para manejar el resultado
    val qrResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val scanContent = data?.getStringExtra("SCAN_RESULT")
            scanContent?.let {
                println("Resultado del escaneo QR: $it") // Procesa el resultado aquí
            }
        }
    }

    // Lanzar el escáner de código QR
    val startQRScanner = {
        val integrator = IntentIntegrator(activity)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Escanea un código QR")
        integrator.setBeepEnabled(true)
        integrator.setBarcodeImageEnabled(true)
        val intent = integrator.createScanIntent()
        qrResultLauncher.launch(intent) // Inicia el escaneo
    }

    // Composición de los botones
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Botón para iniciar el escaneo
        Button(onClick = { startQRScanner() }, modifier = Modifier.padding(16.dp)) {
            Text("Escanear QR")
        }

        // Botón para regresar
        Button(onClick = onBack, modifier = Modifier.padding(16.dp)) {
            Text("Volver")
        }
    }
}

//  1.1 asociar relojes
@Composable
fun AsociarReloj(onBack: () -> Unit) {
    var isScanningQR by remember { mutableStateOf(false) }

    // Si estamos en la pantalla de escanear, mostramos EscanearQR
    if (isScanningQR) {
        EscanearQR(onBack = { isScanningQR = false }) // Pasamos el cambio de estado al regresar
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

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { isScanningQR = true }, // Cambiar el estado para mostrar EscanearQR
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text("ESCANEAR QR", color = Color.White)
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