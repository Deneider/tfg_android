package com.example.tfg_android.pantallas
import android.app.Activity
import android.content.Context
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
import androidx.compose.material3.OutlinedTextField
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
import com.example.tfg_android.funcionesSinGui.ApiResponse
import com.example.tfg_android.funcionesSinGui.Cliente
import com.example.tfg_android.funcionesSinGui.RetrofitClient
import com.google.zxing.integration.android.IntentIntegrator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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
    var inputCorreoODni by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    val context = LocalContext.current

    if (isScanningQR) {
        EscanearQR(
            onMacScanned = {
                macEscaneada = it
                isScanningQR = false
            },
            onBack = { isScanningQR = false }
        )
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(16.dp)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Text(
                        "Asociar Reloj a Cliente",
                        color = Color.White,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(16.dp)
                    )

                    OutlinedTextField(
                        value = inputCorreoODni,
                        onValueChange = { inputCorreoODni = it },
                        label = { Text("Correo o DNI del cliente") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        singleLine = true
                    )

                    macEscaneada?.let {
                        Text("MAC escaneada: $it", color = Color.Green)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { isScanningQR = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF24BDFF))
                    ) {
                        Text("ESCANEAR QR", color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            asociarReloj(context, inputCorreoODni, macEscaneada) { resultado ->
                                mensaje = resultado
                            }
                        },
                        enabled = macEscaneada != null && inputCorreoODni.isNotBlank(),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("ASOCIAR", color = Color.White)
                    }

                    if (mensaje.isNotBlank()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(mensaje, color = Color.Yellow)
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

fun asociarReloj(
    context: Context,
    correoODni: String,
    macReloj: String?,
    onResultado: (String) -> Unit
) {
    if (macReloj == null) {
        onResultado("MAC del reloj no disponible")
        return
    }

    val api = RetrofitClient.apiService

    // Buscar cliente por correo o DNI
    val callBuscar = if (correoODni.contains("@")) {
        api.getClienteByCorreo(correoODni)
    } else {
        api.getClienteByDni(correoODni)
    }

    callBuscar.enqueue(object : Callback<Cliente> {
        override fun onResponse(call: Call<Cliente>, response: Response<Cliente>) {
            if (response.isSuccessful) {
                val cliente = response.body()
                if (cliente != null) {
                    // Aquí la respuesta puede ser más compleja, verifiquemos el cuerpo correctamente
                    val callAsignar = api.asignarReloj(cliente.id_cliente.toString(), macReloj)
                    callAsignar.enqueue(object : Callback<ApiResponse> {
                        override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                            if (response.isSuccessful) {
                                // Comprobamos la respuesta correctamente
                                val apiResponse = response.body()
                                if (apiResponse != null && apiResponse.message == "Reloj asignado correctamente al cliente") {
                                    onResultado("Error al asociar reloj: ${apiResponse?.message ?: "Desconocido"}")
                                } else {
                                    onResultado(" Reloj asociado con éxito ")
                                }
                            } else {
                                onResultado("Error al asociar reloj: ${response.message()}")
                            }
                        }

                        override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                            onResultado("Fallo al asociar reloj: ${t.message}")
                        }
                    })
                } else {
                    onResultado("Cliente no encontrado")
                }
            } else {
                onResultado("Error al buscar cliente")
            }
        }

        override fun onFailure(call: Call<Cliente>, t: Throwable) {
            onResultado("Fallo al buscar cliente: ${t.message}")
        }
    })
}



@Composable
fun DesAsociarReloj(onBack: () -> Unit) {
    var isScanningQR by remember { mutableStateOf(false) }
    var macEscaneada by remember { mutableStateOf<String?>(null) }
    var inputCorreoODni by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    val context = LocalContext.current

    if (isScanningQR) {
        EscanearQR(
            onMacScanned = {
                macEscaneada = it
                isScanningQR = false
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
                        "Desasociar Reloj de Cliente",
                        color = Color.White,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(16.dp)
                    )

                    OutlinedTextField(
                        value = inputCorreoODni,
                        onValueChange = { inputCorreoODni = it },
                        label = { Text("Correo o DNI del cliente") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        singleLine = true
                    )

                    macEscaneada?.let {
                        Text("MAC escaneada: $it", color = Color.Green)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { isScanningQR = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF24BDFF))
                    ) {
                        Text("ESCANEAR QR", color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            desasociarReloj(context, inputCorreoODni, macEscaneada) { resultado ->
                                mensaje = resultado
                            }
                        },
                        enabled = macEscaneada != null && inputCorreoODni.isNotBlank(),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("DESASOCIAR", color = Color.White)
                    }

                    if (mensaje.isNotBlank()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(mensaje, color = Color.Yellow)
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


fun desasociarReloj(
    context: Context,
    correoODni: String,
    macReloj: String?,
    onResultado: (String) -> Unit
) {
    if (macReloj == null) {
        onResultado("MAC del reloj no disponible")
        return
    }

    val api = RetrofitClient.apiService

    // Buscar cliente por correo o DNI
    val callBuscar = if (correoODni.contains("@")) {
        api.getClienteByCorreo(correoODni)
    } else {
        api.getClienteByDni(correoODni)
    }

    callBuscar.enqueue(object : Callback<Cliente> {
        override fun onResponse(call: Call<Cliente>, response: Response<Cliente>) {
            if (response.isSuccessful) {
                val cliente = response.body()
                if (cliente != null) {
                    // Realizamos la solicitud DELETE para desasociar el reloj
                    val callDesasignar = api.desasignarReloj(cliente.id_cliente.toString(), macReloj)
                    callDesasignar.enqueue(object : Callback<ApiResponse> {
                        override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                            if (response.isSuccessful && response.body()?.success == true) {
                                onResultado("Error al desasociar reloj")
                            } else {
                                onResultado("Reloj desasociado con éxito")
                            }
                        }

                        override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                            onResultado("Fallo al desasociar reloj: ${t.message}")
                        }
                    })
                } else {
                    onResultado("Cliente no encontrado")
                }
            } else {
                onResultado("Error al buscar cliente")
            }
        }

        override fun onFailure(call: Call<Cliente>, t: Throwable) {
            onResultado("Fallo al buscar cliente: ${t.message}")
        }
    })
}
