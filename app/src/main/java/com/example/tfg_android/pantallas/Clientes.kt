package com.example.tfg_android.pantallas

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import com.example.tfg_android.funcionesSinGui.ApiService
import com.example.tfg_android.funcionesSinGui.Cliente
import com.example.tfg_android.funcionesSinGui.ColoresFormularios
import com.example.tfg_android.funcionesSinGui.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// 2.1  alta cliente
@Composable
fun AltaClienteScreen(onBack: () -> Unit) {
    val context = LocalContext.current

    // Estados para los campos de entrada
    var nombre by remember { mutableStateOf("") }
    var apellido1 by remember { mutableStateOf("") }
    var apellido2 by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf("") }
    var dni by remember { mutableStateOf("") }
    var calle by remember { mutableStateOf("") }
    var numero_casa by remember { mutableStateOf("") }
    var localidad by remember { mutableStateOf("") }
    var provincia by remember { mutableStateOf("") }
    var cod_postal by remember { mutableStateOf("") }
    var nacionalidad by remember { mutableStateOf("") }
    var puntos by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var isDatePickerOpen by remember { mutableStateOf(false) }

    val apiService = RetrofitClient.retrofitInstance.create(ApiService::class.java)

    // Configuración de la fecha
    val dateFormatter = remember { SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()) }
    val calendar = Calendar.getInstance()

    // Scroll habilitado
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
                    "Alta de Cliente",
                    color = Color.White,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(16.dp)
                )

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ColoresFormularios.textoBlanco()
                )

                OutlinedTextField(
                    value = apellido1,
                    onValueChange = { apellido1 = it },
                    label = { Text("Primer Apellido") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ColoresFormularios.textoBlanco()
                )

                OutlinedTextField(
                    value = apellido2,
                    onValueChange = { apellido2 = it },
                    label = { Text("Segundo Apellido") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ColoresFormularios.textoBlanco()
                )

                // Fecha de nacimiento con DatePicker
                OutlinedTextField(
                    value = fechaNacimiento,
                    onValueChange = {},
                    label = { Text("Fecha de Nacimiento") },
                    readOnly = true,
                    colors = ColoresFormularios.textoBlanco(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isDatePickerOpen = true }
                )
                if (isDatePickerOpen) {
                    LaunchedEffect(Unit) {
                        val datePickerDialog = android.app.DatePickerDialog(
                            context,
                            { _, year, month, dayOfMonth ->
                                calendar.set(year, month, dayOfMonth)
                                fechaNacimiento = dateFormatter.format(calendar.time)
                                isDatePickerOpen = false
                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                        )
                        datePickerDialog.show()
                    }
                }

                OutlinedTextField(
                    value = dni,
                    onValueChange = { dni = it },
                    label = { Text("DNI") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ColoresFormularios.textoBlanco()
                )

                OutlinedTextField(
                    value = calle,
                    onValueChange = { calle = it },
                    label = { Text("Calle") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ColoresFormularios.textoBlanco()
                )

                OutlinedTextField(
                    value = numero_casa,
                    onValueChange = { numero_casa = it },
                    label = { Text("Número Casa") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ColoresFormularios.textoBlanco()
                )

                OutlinedTextField(
                    value = localidad,
                    onValueChange = { localidad = it },
                    label = { Text("Localidad") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ColoresFormularios.textoBlanco()
                )

                OutlinedTextField(
                    value = provincia,
                    onValueChange = { provincia = it },
                    label = { Text("Provincia") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ColoresFormularios.textoBlanco()
                )

                OutlinedTextField(
                    value = cod_postal,
                    onValueChange = { cod_postal = it },
                    label = { Text("Código Postal") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ColoresFormularios.textoBlanco()
                )

                OutlinedTextField(
                    value = nacionalidad,
                    onValueChange = { nacionalidad = it },
                    label = { Text("Nacionalidad") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ColoresFormularios.textoBlanco()
                )

                OutlinedTextField(
                    value = correo,
                    onValueChange = { correo = it },
                    label = { Text("Correo") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ColoresFormularios.textoBlanco()
                )

                OutlinedTextField(
                    value = contrasena,
                    onValueChange = { contrasena = it },
                    label = { Text("Contraseña") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ColoresFormularios.textoBlanco()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (nombre.isBlank() || apellido1.isBlank() || apellido2.isBlank() || fechaNacimiento.isBlank() ||
                            dni.isBlank() || calle.isBlank() || numero_casa.isBlank() || localidad.isBlank() || provincia.isBlank() ||
                            cod_postal.isBlank() || nacionalidad.isBlank() || correo.isBlank() || contrasena.isBlank()) {

                            Toast.makeText(context, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        isLoading = true
                        val nuevoCliente = Cliente(
                            id_cliente = 0, // El backend lo generará
                            nombre = nombre,
                            primer_apellido = apellido1,
                            segundo_apellido = apellido2,
                            fecha_nacimiento = fechaNacimiento,
                            dni = dni,
                            calle = calle,
                            numero_casa = numero_casa,
                            localidad = localidad,
                            provincia = provincia,
                            cod_postal = cod_postal,
                            nacionalidad = nacionalidad,
                            puntos = 0,
                            correo = correo,
                            contrasena = contrasena
                        )

                        apiService.createCliente(nuevoCliente).enqueue(object : retrofit2.Callback<Void> {
                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                isLoading = false
                                if (response.isSuccessful) {
                                    Toast.makeText(context, "Cliente creado con éxito", Toast.LENGTH_SHORT).show()
                                    onBack()
                                } else {
                                    Toast.makeText(context, "Error en el registro: ${response.message()}", Toast.LENGTH_SHORT).show()
                                }
                            }
                            override fun onFailure(call: Call<Void>, t: Throwable) {
                                isLoading = false
                                Toast.makeText(context, "Fallo de conexión: ${t.message}", Toast.LENGTH_LONG).show()
                            }
                        })
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text("Registrar Cliente", color = Color.White)
                    }
                }

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

// 2.2  modificar cliente
@Composable
fun ModificarClienteScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val apiService = RetrofitClient.retrofitInstance.create(ApiService::class.java)

    var correoBusqueda by remember { mutableStateOf("") }
    var clienteActual by remember { mutableStateOf<Cliente?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    // Función para cargar el cliente por correo
    fun cargarClientePorCorreo(correo: String) {
        isLoading = true
        apiService.getClienteByCorreo(correo).enqueue(object : Callback<Cliente> {
            override fun onResponse(call: Call<Cliente>, response: Response<Cliente>) {
                isLoading = false
                if (response.isSuccessful) {
                    clienteActual = response.body()
                } else {
                    Toast.makeText(context, "Cliente no encontrado", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Cliente>, t: Throwable) {
                isLoading = false
                Toast.makeText(context, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Función para actualizar el cliente
    fun actualizarCliente() {
        clienteActual?.let { cliente ->
            isLoading = true  // Al iniciar la actualización
            // Realizar la solicitud a la API
            apiService.updateCliente(cliente.id_cliente, cliente).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    isLoading = false  // Al finalizar la respuesta
                    if (response.isSuccessful) {
                        Toast.makeText(context, "Cliente actualizado con éxito", Toast.LENGTH_SHORT).show()
                        onBack()
                    } else {
                        Toast.makeText(context, "Error al actualizar", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    isLoading = false  // En caso de fallo
                    Toast.makeText(context, "Fallo: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    // Clase auxiliar para los campos editables
    data class CampoEditable(
        val etiqueta: String,
        val valor: String,
        val onCambio: (String) -> Unit
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Modificar Cliente", color = Color.White, fontSize = 20.sp, modifier = Modifier.padding(16.dp))

        // Campo para buscar por correo
        OutlinedTextField(
            value = correoBusqueda,
            onValueChange = { correoBusqueda = it },
            label = { Text("Correo del cliente a buscar") },
            modifier = Modifier.fillMaxWidth(),
            colors = ColoresFormularios.textoBlanco()
        )

        // Botón para buscar el cliente
        Button(
            onClick = { cargarClientePorCorreo(correoBusqueda) },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF24BDFF))
        ) {
            Text("Buscar Cliente", color = Color.White)
        }

        clienteActual?.let { cliente ->

            Spacer(Modifier.height(16.dp))

            // Definición de los campos editables para cada propiedad del cliente
            val campos = listOf(
                CampoEditable("Nombre", cliente.nombre) { nuevo -> clienteActual = clienteActual?.copy(nombre = nuevo) },
                CampoEditable("Primer Apellido", cliente.primer_apellido) { nuevo -> clienteActual = clienteActual?.copy(primer_apellido = nuevo) },
                CampoEditable("Segundo Apellido", cliente.segundo_apellido) { nuevo -> clienteActual = clienteActual?.copy(segundo_apellido = nuevo) },
                CampoEditable("Fecha de Nacimiento", cliente.fecha_nacimiento) { nuevo -> clienteActual = clienteActual?.copy(fecha_nacimiento = nuevo) },
                CampoEditable("DNI", cliente.dni) { nuevo -> clienteActual = clienteActual?.copy(dni = nuevo) },
                CampoEditable("Calle", cliente.calle) { nuevo -> clienteActual = clienteActual?.copy(calle = nuevo) },
                CampoEditable("Número Casa", cliente.numero_casa) { nuevo -> clienteActual = clienteActual?.copy(numero_casa = nuevo) },
                CampoEditable("Localidad", cliente.localidad) { nuevo -> clienteActual = clienteActual?.copy(localidad = nuevo) },
                CampoEditable("Provincia", cliente.provincia) { nuevo -> clienteActual = clienteActual?.copy(provincia = nuevo) },
                CampoEditable("Código Postal", cliente.cod_postal) { nuevo -> clienteActual = clienteActual?.copy(cod_postal = nuevo) },
                CampoEditable("Nacionalidad", cliente.nacionalidad) { nuevo -> clienteActual = clienteActual?.copy(nacionalidad = nuevo) },
                CampoEditable("Correo", cliente.correo) { nuevo -> clienteActual = clienteActual?.copy(correo = nuevo) },
                CampoEditable("Contraseña", cliente.contrasena) { nuevo -> clienteActual = clienteActual?.copy(contrasena = nuevo) }
            )

            // Mostrar los campos editables
            campos.forEach { campo ->
                OutlinedTextField(
                    value = campo.valor,
                    onValueChange = campo.onCambio,
                    label = { Text(campo.etiqueta) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ColoresFormularios.textoBlanco()
                )
            }

            Spacer(Modifier.height(16.dp))

            // Botón para actualizar el cliente
            Button(
                onClick = { actualizarCliente() },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
            ) {
                Text("Actualizar Cliente", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para volver
        Button(
            onClick = { onBack() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text("Volver", color = Color.White)
        }
    }
}


// 2.3  borrar cliente
@Composable
fun BorrarClienteScreen(onBack: () -> Unit) {
    var inputCorreo by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    val context = LocalContext.current

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
                    "Borrar Cliente",
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

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        borrarCliente(context, inputCorreo) { resultado ->
                            mensaje = resultado
                        }
                    },
                    enabled = inputCorreo.isNotBlank(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("BORRAR CLIENTE", color = Color.White)
                }

                if (mensaje.isNotBlank()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(mensaje, color = Color.Yellow)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("Volver", color = Color.White)
                }
            }
        }
    }
}

fun borrarCliente(
    context: Context,
    correo: String,
    onResultado: (String) -> Unit
) {
    val api = RetrofitClient.apiService

    val callBuscar = api.getClienteByCorreo(correo)

    callBuscar.enqueue(object : Callback<Cliente> {
        override fun onResponse(call: Call<Cliente>, response: Response<Cliente>) {
            if (response.isSuccessful) {
                val cliente = response.body()
                if (cliente != null) {
                    val callBorrar = api.deleteCliente(cliente.id_cliente.toString())
                    callBorrar.enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                            if (response.isSuccessful) {
                                val mensajeRespuesta = response.body()?.string() ?: "Cliente eliminado correctamente"
                                onResultado("Cliente eliminado correctamente: $mensajeRespuesta")
                            } else {
                                onResultado("Error al eliminar cliente: ${response.message()}")
                            }
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            onResultado("Fallo al eliminar cliente: ${t.message}")
                        }
                    })
                } else {
                    onResultado("Cliente no encontrado.")
                }
            } else {
                onResultado("Error al buscar cliente.")
            }
        }

        override fun onFailure(call: Call<Cliente>, t: Throwable) {
            onResultado("Fallo al buscar cliente: ${t.message}")
        }
    })
}
