package com.example.tfg_android.pantallas

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.tfg_android.funcionesSinGui.ApiService
import com.example.tfg_android.funcionesSinGui.Cliente
import com.example.tfg_android.funcionesSinGui.RetrofitClient
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// 2.1  alta cliente
@Composable
fun AltaClienteScreen(onBack: () -> Unit){
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
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = apellido1,
                    onValueChange = { apellido1 = it },
                    label = { Text("Primer Apellido") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = apellido2,
                    onValueChange = { apellido2 = it },
                    label = { Text("Segundo Apellido") },
                    modifier = Modifier.fillMaxWidth()
                )
                // Fecha de nacimiento con DatePicker
                OutlinedTextField(
                    value = fechaNacimiento,
                    onValueChange = {},
                    label = { Text("Fecha de Nacimiento") },
                    readOnly = true,
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
                    onValueChange = {dni = it},
                    label = { Text("Dni") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = calle,
                    onValueChange = {calle = it},
                    label = { Text("Calle") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = numero_casa,
                    onValueChange = {numero_casa = it},
                    label = { Text("Numero casa") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = localidad,
                    onValueChange = {localidad = it},
                    label = { Text("Localidad") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = provincia,
                    onValueChange = {provincia = it},
                    label = { Text("Provincia") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = cod_postal,
                    onValueChange = {cod_postal = it},
                    label = { Text("Codigo postal") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = nacionalidad,
                    onValueChange = {nacionalidad = it},
                    label = { Text("Nacionalidad") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = correo,
                    onValueChange = { correo = it },
                    label = { Text("Correo") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = contrasena,
                    onValueChange = {contrasena = it},
                    label = { Text("Contraseña") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))


                Button(
                    onClick = {
                        if (nombre.isBlank() || apellido1.isBlank() || apellido2.isBlank() || fechaNacimiento.isBlank() ||
                            dni.isBlank()|| calle.isBlank() || numero_casa.isBlank() || localidad.isBlank() || provincia.isBlank() || cod_postal.isBlank() ||
                            nacionalidad.isBlank() || correo.isBlank() || contrasena.isBlank() ){

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
                            correo = correo,
                            puntos = 0,
                            contrasena = contrasena,
                        )

                        apiService.createCliente(nuevoCliente).enqueue(object : retrofit2.Callback<Void> {
                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                if (response.isSuccessful) {
                                    Toast.makeText(context, "Cliente creado con éxito", Toast.LENGTH_SHORT).show()
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
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
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
                    "Modificar Cliente",
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
// 2.3  borrar cliente
@Composable
fun BorrarClienteScreen (onBack: () -> Unit){
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