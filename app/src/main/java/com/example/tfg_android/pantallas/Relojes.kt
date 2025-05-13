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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tfg_android.funcionesSinGui.ApiResponse
import com.example.tfg_android.funcionesSinGui.Cliente
import com.example.tfg_android.funcionesSinGui.ColoresFormularios
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
    var scannedMac by remember { mutableStateOf<String?>(null) } //variable de la mac escaneada
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
            onBack() // Si se cancela el escaneo, vuelve atrás
        }
    }

    val startQRScanner = {
        val integrator = IntentIntegrator(activity) //variable para iniciar el escaner
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE) //solo escanea qr, nada mas
        integrator.setPrompt("Escanea un código QR") //mensaje que aparece en la pantalla del escaneo
        integrator.setBeepEnabled(true)// sonido cuando escanee
        integrator.setBarcodeImageEnabled(true) //se guarda la imagen del qr
        val intent = integrator.createScanIntent()
        qrResultLauncher.launch(intent) //se lanza el escaneador
    }

    // Lanzar escáner al entrar en la pantalla
    LaunchedEffect(Unit) {
        if (!hasLaunched) {
            hasLaunched = true
            startQRScanner() //lanza el método
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
                Text("MAC Escaneada: $it", color = Color.Green) //muestra la info , en este caso la mac que lleva el qr del reloj, pero al escanear cualquier qr da la informacion de dentro
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
    var inputCorreo by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    val context = LocalContext.current
    //escanea el qr
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
                        value = inputCorreo,
                        onValueChange = { inputCorreo = it },
                        label = { Text("Correo del cliente") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        singleLine = true,
                        colors = ColoresFormularios.textoBlanco()

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
                            asociarReloj(context, inputCorreo, macEscaneada) { resultado ->
                                mensaje = resultado
                            }
                        },
                        enabled = macEscaneada != null && inputCorreo.isNotBlank(),
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
    correo: String,
    macReloj: String?,
    onResultado: (String) -> Unit
) {

    if (macReloj == null) {
        onResultado("MAC del reloj no disponible")
        return
    }

    val api = RetrofitClient.apiService

    // Buscar cliente por correo o DNI
    val callBuscar = if (correo.contains("@")) {
        api.getClienteByCorreo(correo)
    } else {
        api.getClienteByDni(correo)// mas adelante se implementara por dni
    }

    callBuscar.enqueue(object : Callback<Cliente> {
        override fun onResponse(call: Call<Cliente>, response: Response<Cliente>) {
            if (response.isSuccessful) {
                val cliente = response.body()
                if (cliente != null) {
                    // Cliente encontrado, ahora asigna reloj
                    val callAsignar = api.asignarReloj(cliente.id_cliente.toString(), macReloj) //asigna el reloj (MAC) al cliente
                    callAsignar.enqueue(object : Callback<ApiResponse> {
                        override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                            if (response.isSuccessful) {
                                onResultado("Reloj asociado con éxito")
                            } else {
                                onResultado("Error al asociar reloj: ${response.message()}")
                            }
                        }
                                                                                                                    //errores varios
                        override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                            onResultado("Fallo al asociar reloj: ${t.message}")
                        }
                    })
                } else {
                    onResultado("Cliente no encontrado")
                }
            } else {
                onResultado("Error al buscar cliente: ${response.message()}")
            }
        }

        override fun onFailure(call: Call<Cliente>, t: Throwable) {
            onResultado("Fallo al buscar cliente: ${t.message}")
        }
    })

}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DesAsociarReloj(onBack: () -> Unit) {
    var isScanningQR by remember { mutableStateOf(false) }
    var macEscaneada by remember { mutableStateOf<String?>(null) }
    var inputCorreo by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    val context = LocalContext.current

    if (isScanningQR) { //proceso de escanear qr
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
                        value = inputCorreo,
                        onValueChange = { inputCorreo = it },
                        label = { Text("Correo del cliente") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        singleLine = true,
                        colors = TextFieldDefaults.outlinedTextFieldColors( // diseño de colores para el formulario de meter el correo
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = Color.White,
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.White
                        )
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
                            desasociarReloj(context, inputCorreo, macEscaneada) { resultado ->
                                mensaje = resultado
                            }
                        },
                        enabled = macEscaneada != null && inputCorreo.isNotBlank(),
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
    correo: String,
    macReloj: String?,
    onResultado: (String) -> Unit
) {
    if (macReloj == null) {
        onResultado("MAC del reloj no disponible")
        return
    }
    //llamada  a la instancia de la apo
    val api = RetrofitClient.apiService

    // Buscar cliente por correo
    val callBuscar = if (correo.contains("@")) {
        api.getClienteByCorreo(correo)
    } else {
        api.getClienteByDni(correo) //se implementara mas adelante con dni tambien
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
                                onResultado("Reloj desasociado con éxito")
                            } else {
                                onResultado("Error al desasociar reloj")
                            }
                        }
                                                                                            //errores varios
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

@Composable
fun CobrarPuntos(onBack: () -> Unit) {
    var isScanningQR by remember { mutableStateOf(false) }              //para escanear qr
    var macEscaneada by remember { mutableStateOf<String?>(null) }
    var puntosACobrar by remember { mutableStateOf("") }
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
    } else { //mientras que no este seleccionada la opcion de escanear qr:
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
                    Text("Cobrar Puntos", color = Color.White, fontSize = 20.sp)

                    macEscaneada?.let {
                        Text("MAC escaneada: $it", color = Color.Green)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = puntosACobrar, //lamar a puntos a cobrar para cuando se tenga que hacer la operacion
                        onValueChange = { puntosACobrar = it },
                        label = { Text("Puntos a cobrar") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = ColoresFormularios.textoBlanco()
                    )

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
                            cobrarPuntos(context, macEscaneada, puntosACobrar.toIntOrNull()) { resultado ->
                                mensaje = resultado
                            }
                        },
                        enabled = macEscaneada != null && puntosACobrar.toIntOrNull() != null,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("COBRAR", color = Color.White)
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
fun cobrarPuntos(
    context: Context,
    macReloj: String?,
    puntos: Int?,
    onResultado: (String) -> Unit
) {
    if (macReloj == null || puntos == null) { //si estan vacios los campos:
        onResultado("MAC del reloj o puntos inválidos")
        return
    }
    // instancia de la api
    val api = RetrofitClient.apiService

    api.getClientePorMacReloj(macReloj).enqueue(object : Callback<Cliente> {
        override fun onResponse(call: Call<Cliente>, response: Response<Cliente>) {
            if (response.isSuccessful) {
                val cliente = response.body()
                if (cliente != null) {
                    api.cobrarPuntos(cliente.id_cliente, puntos).enqueue(object : Callback<Map<String, Any>> { // cobrar putnos a la id del cliente en concreto
                        override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
                            if (response.isSuccessful) {
                                onResultado("Puntos cobrados correctamente")
                            } else {
                                onResultado("Error al cobrar puntos: ${response.message()}")
                            }
                        }

                        override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                            onResultado("Fallo al cobrar puntos: ${t.message}")
                        }                                                                           //errores
                    })
                } else {
                    onResultado("Cliente no encontrado para la MAC proporcionada")
                }
            } else {
                onResultado("Error al buscar cliente: ${response.message()}")
            }
        }

        override fun onFailure(call: Call<Cliente>, t: Throwable) {
            onResultado("Fallo al buscar cliente: ${t.message}")
        }
    })
}

@Composable
fun RecargarPuntos(onBack: () -> Unit) {
    var isScanningQR by remember { mutableStateOf(false) }
    var macEscaneada by remember { mutableStateOf<String?>(null) }          //para escanear el qr
    var puntosARecargar by remember { mutableStateOf("") }
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
    } else { // si no esta escaneando qr: (muestra el menu)
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
                    Text("Recargar Puntos", color = Color.White, fontSize = 20.sp)

                    macEscaneada?.let {
                        Text("MAC escaneada: $it", color = Color.Green)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = puntosARecargar,
                        onValueChange = { puntosARecargar = it },
                        label = { Text("Puntos a recargar") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = ColoresFormularios.textoBlanco()
                    )

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
                            recargarPuntos(context, macEscaneada, puntosARecargar.toIntOrNull()) { resultado ->
                                mensaje = resultado
                            }
                        },
                        enabled = macEscaneada != null && puntosARecargar.toIntOrNull() != null,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("RECARGAR", color = Color.White)
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
fun recargarPuntos(
    context: Context,
    macReloj: String?,
    puntos: Int?,
    onResultado: (String) -> Unit
) {
    if (macReloj == null || puntos == null) { //si no estan los campos de las variables rellenados
        onResultado("MAC del reloj o puntos inválidos")
        return
    }
    // variable de la instanci de la api
    val api = RetrofitClient.apiService

    api.getClientePorMacReloj(macReloj).enqueue(object : Callback<Cliente> {
        override fun onResponse(call: Call<Cliente>, response: Response<Cliente>) {
            if (response.isSuccessful) {
                val cliente = response.body()
                if (cliente != null) {
                    api.anadirPuntos(cliente.id_cliente, puntos).enqueue(object : Callback<Map<String, Any>> { //añadir puntos al cliente
                        override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
                            if (response.isSuccessful) {
                                onResultado("Puntos recargados correctamente")
                            } else {
                                onResultado("Error al recargar puntos: ${response.message()}")
                            }
                        }

                        override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                            onResultado("Fallo al recargar puntos: ${t.message}")
                        }
                    })
                } else {
                    onResultado("Cliente no encontrado para la MAC proporcionada")
                }
            } else {
                onResultado("Error al buscar cliente: ${response.message()}")
            }
        }

        override fun onFailure(call: Call<Cliente>, t: Throwable) {
            onResultado("Fallo al buscar cliente: ${t.message}")
        }
    })
}
