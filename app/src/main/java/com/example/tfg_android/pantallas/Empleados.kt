package com.example.tfg_android.pantallas

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
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
import com.example.tfg_android.funcionesSinGui.ApiService
import com.example.tfg_android.funcionesSinGui.ColoresFormularios
import com.example.tfg_android.funcionesSinGui.RetrofitClient
import com.example.tfg_android.funcionesSinGui.Trabajador
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AltaEmpleadoScreen(onBack: () -> Unit) {
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
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var puesto by remember { mutableStateOf("") }
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
                    "Alta de Empleado",
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

                // Estado para abrir el DatePicker
                var isDatePickerDialogShown by remember { mutableStateOf(false) }

                if (isDatePickerDialogShown) {
                    android.app.DatePickerDialog(
                        context,
                        { _, year, month, dayOfMonth ->
                            calendar.set(year, month, dayOfMonth)
                            fechaNacimiento = dateFormatter.format(calendar.time)
                            isDatePickerDialogShown = false
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    ).show()
                }

                // Fecha de nacimiento con botón
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = fechaNacimiento,
                        onValueChange = {},
                        label = { Text("Fecha de Nacimiento") },
                        readOnly = true,
                        modifier = Modifier.weight(1f),
                        colors = ColoresFormularios.textoBlanco()
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { isDatePickerDialogShown = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                    ) {
                        Text("Seleccionar Fecha", color = Color.White)
                    }
                }


                OutlinedTextField(
                    value = dni,
                    onValueChange = {dni = it},
                    label = { Text("Dni") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ColoresFormularios.textoBlanco()
                )

                OutlinedTextField(
                    value = calle,
                    onValueChange = {calle = it},
                    label = { Text("Calle") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ColoresFormularios.textoBlanco()
                )

                OutlinedTextField(
                    value = numero_casa,
                    onValueChange = {numero_casa = it},
                    label = { Text("Numero casa") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ColoresFormularios.textoBlanco()
                )
                OutlinedTextField(
                    value = localidad,
                    onValueChange = {localidad = it},
                    label = { Text("Localidad") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ColoresFormularios.textoBlanco()
                )
                OutlinedTextField(
                    value = provincia,
                    onValueChange = {provincia = it},
                    label = { Text("Provincia") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ColoresFormularios.textoBlanco()
                )
                OutlinedTextField(
                    value = cod_postal,
                    onValueChange = {cod_postal = it},
                    label = { Text("Codigo postal") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ColoresFormularios.textoBlanco()
                )
                OutlinedTextField(
                    value = nacionalidad,
                    onValueChange = {nacionalidad = it},
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
                    onValueChange = {contrasena = it},
                    label = { Text("Contraseña") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ColoresFormularios.textoBlanco()
                )

                // Dropdown para seleccionar el puesto
                var expanded by remember { mutableStateOf(false) }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = puesto,
                        onValueChange = {},
                        label = { Text("Puesto") },
                        readOnly = true,
                        modifier = Modifier.weight(1f),
                        colors = ColoresFormularios.textoBlanco()
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { expanded = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                    ) {
                        Text("Elegir", color = Color.White)
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        listOf("camarero", "jefe").forEach { role ->
                            DropdownMenuItem(
                                text = { Text(role) },
                                onClick = {
                                    puesto = role
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (nombre.isBlank() || apellido1.isBlank() || apellido2.isBlank() || fechaNacimiento.isBlank() ||
                            dni.isBlank()|| calle.isBlank() || numero_casa.isBlank() || localidad.isBlank() || provincia.isBlank() || cod_postal.isBlank() ||
                            nacionalidad.isBlank() || correo.isBlank() || contrasena.isBlank() || puesto.isBlank()) {

                            Toast.makeText(context, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        isLoading = true
                        val nuevoTrabajador = Trabajador(
                            id_trabajador = 0, // El backend lo generará
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
                            correo = correo,
                            contrasena = contrasena,
                            puesto = puesto
                        )

                        apiService.createTrabajador(nuevoTrabajador).enqueue(object : retrofit2.Callback<Void> {
                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                isLoading = false
                                if (response.isSuccessful) {
                                    Toast.makeText(context, "Empleado registrado con éxito", Toast.LENGTH_SHORT).show()
                                    onBack()
                                } else {
                                    Toast.makeText(context, "Error en el registro", Toast.LENGTH_SHORT).show()
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
                        Text("Registrar Empleado", color = Color.White)
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

// Pantalla para modificar empleados
@Composable
fun ModificarEmpleadoScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val apiService = RetrofitClient.retrofitInstance.create(ApiService::class.java)

    var correoBusqueda by remember { mutableStateOf("") }
    var trabajadorActual by remember { mutableStateOf<Trabajador?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val formatoEntrada = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // del backend
    val formatoSalida = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())  // para mostrar
    val formatoValidacion = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

    fun cargarTrabajadorPorCorreo(correo: String) {
        if (correo.isBlank()) {
            Toast.makeText(context, "Introduce un correo", Toast.LENGTH_SHORT).show()
            return
        }

        isLoading = true
        apiService.getTrabajadorByCorreo(correo.trim()).enqueue(object : Callback<Trabajador> {
            override fun onResponse(call: Call<Trabajador>, response: Response<Trabajador>) {
                isLoading = false
                if (response.isSuccessful) {
                    response.body()?.let {
                        // Convertir fecha de nacimiento para mostrar en formato "dd-MM-yyyy"
                        val fechaMostrada = try {
                            formatoSalida.format(formatoEntrada.parse(it.fecha_nacimiento))
                        } catch (e: Exception) {
                            it.fecha_nacimiento
                        }
                        trabajadorActual = it.copy(fecha_nacimiento = fechaMostrada)
                    }
                } else {
                    Toast.makeText(context, "Trabajador no encontrado", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Trabajador>, t: Throwable) {
                isLoading = false
                Toast.makeText(context, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun actualizarTrabajador() {
        trabajadorActual?.let { trabajador ->
            // Validar formato de fecha antes de enviar
            try {
                formatoValidacion.parse(trabajador.fecha_nacimiento) // lanza excepción si el formato es inválido
            } catch (e: Exception) {
                Toast.makeText(context, "Formato de fecha inválido. Usa dd-MM-yyyy", Toast.LENGTH_SHORT).show()
                return
            }

            isLoading = true
            apiService.updateTrabajador(trabajador.id_trabajador, trabajador).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    isLoading = false
                    if (response.isSuccessful) {
                        Toast.makeText(context, "Trabajador actualizado correctamente", Toast.LENGTH_SHORT).show()
                        onBack()
                    } else {
                        Toast.makeText(context, "Error al actualizar trabajador", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    isLoading = false
                    Toast.makeText(context, "Fallo: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

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
        Text("Modificar Trabajador", color = Color.White, fontSize = 20.sp, modifier = Modifier.padding(16.dp))

        OutlinedTextField(
            value = correoBusqueda,
            onValueChange = { correoBusqueda = it },
            label = { Text("Correo del trabajador a buscar") },
            modifier = Modifier.fillMaxWidth(),
            colors = ColoresFormularios.textoBlanco()
        )

        Button(
            onClick = { cargarTrabajadorPorCorreo(correoBusqueda) },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF24BDFF))
        ) {
            Text("Buscar Trabajador", color = Color.White)
        }

        trabajadorActual?.let { trabajador ->
            Spacer(Modifier.height(16.dp))

            val campos = listOf(
                CampoEditable("Nombre", trabajador.nombre) { trabajadorActual = trabajadorActual?.copy(nombre = it) },
                CampoEditable("Primer Apellido", trabajador.primer_apellido) { trabajadorActual = trabajadorActual?.copy(primer_apellido = it) },
                CampoEditable("Segundo Apellido", trabajador.segundo_apellido) { trabajadorActual = trabajadorActual?.copy(segundo_apellido = it) },
                CampoEditable("Fecha de Nacimiento", trabajador.fecha_nacimiento) { trabajadorActual = trabajadorActual?.copy(fecha_nacimiento = it) },
                CampoEditable("DNI", trabajador.dni) { trabajadorActual = trabajadorActual?.copy(dni = it) },
                CampoEditable("Calle", trabajador.calle) { trabajadorActual = trabajadorActual?.copy(calle = it) },
                CampoEditable("Número Casa", trabajador.numero_casa) { trabajadorActual = trabajadorActual?.copy(numero_casa = it) },
                CampoEditable("Localidad", trabajador.localidad) { trabajadorActual = trabajadorActual?.copy(localidad = it) },
                CampoEditable("Provincia", trabajador.provincia) { trabajadorActual = trabajadorActual?.copy(provincia = it) },
                CampoEditable("Código Postal", trabajador.cod_postal) { trabajadorActual = trabajadorActual?.copy(cod_postal = it) },
                CampoEditable("Nacionalidad", trabajador.nacionalidad) { trabajadorActual = trabajadorActual?.copy(nacionalidad = it) },
                CampoEditable("Correo", trabajador.correo) { trabajadorActual = trabajadorActual?.copy(correo = it) },
                CampoEditable("Contraseña", trabajador.contrasena) { trabajadorActual = trabajadorActual?.copy(contrasena = it) },
                CampoEditable("Puesto", trabajador.puesto) { trabajadorActual = trabajadorActual?.copy(puesto = it) }
            )

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

            Button(
                onClick = { actualizarTrabajador() },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
            ) {
                Text("Actualizar Trabajador", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text("Volver", color = Color.White)
        }
    }
}


// Pantalla para borrar empleados
@Composable
fun BorrarEmpleadoScreen(onBack: () -> Unit) {
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
                    "Borrar Empleado",
                    color = Color.White,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(16.dp)
                )

                OutlinedTextField(
                    value = inputCorreo,
                    onValueChange = { inputCorreo = it },
                    label = { Text("Correo del empleado") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    singleLine = true,
                    colors = ColoresFormularios.textoBlanco()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        borrarEmpleado(context, inputCorreo) { resultado ->
                            mensaje = resultado
                        }
                    },
                    enabled = inputCorreo.isNotBlank(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("BORRAR EMPLEADO", color = Color.White)
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

fun borrarEmpleado(
    context: Context,
    correo: String,
    onResultado: (String) -> Unit
) {
    val api = RetrofitClient.apiService

    val callBuscar = api.getTrabajadorByCorreo(correo)

    callBuscar.enqueue(object : Callback<Trabajador> {
        override fun onResponse(call: Call<Trabajador>, response: Response<Trabajador>) {
            if (response.isSuccessful) {
                val trabajador = response.body()
                if (trabajador != null) {
                    val callBorrar = api.deleteTrabajador(trabajador.id_trabajador)
                    callBorrar.enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                            if (response.isSuccessful) {
                                val mensajeRespuesta = response.body()?.string() ?: "Empleado eliminado correctamente"
                                onResultado("Empleado eliminado correctamente: $mensajeRespuesta")
                            } else {
                                onResultado("Error al eliminar empleado: ${response.message()}")
                            }
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            onResultado("Fallo al eliminar empleado: ${t.message}")
                        }
                    })
                } else {
                    onResultado("Empleado no encontrado.")
                }
            } else {
                onResultado("Error al buscar empleado.")
            }
        }

        override fun onFailure(call: Call<Trabajador>, t: Throwable) {
            onResultado("Fallo al buscar empleado: ${t.message}")
        }
    })
}
