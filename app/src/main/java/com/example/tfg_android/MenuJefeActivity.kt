package com.example.tfg_android

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tfg_android.ui.theme.Tfg_androidTheme
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import retrofit2.Call




class MenuJefeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences: SharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val nombreUsuario = sharedPreferences.getString("username", "Usuario desconocido") ?: "Usuario desconocido"

        setContent {
            val navController = rememberNavController() // 📌 Inicializar NavController
            Tfg_androidTheme {
                MenuJefeScreen(nombreUsuario, navController)
            }
        }
    }
}


@Composable
fun MenuJefeScreen(nombreUsuario: String, navController: NavController) {
    var selectedScreen by remember { mutableStateOf("Inicio") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (selectedScreen) {
            "Inicio" -> InicioScreen(nombreUsuario)
            "Asociar" -> AsociarRelojScreen()
            "Empleados" -> VerEmpleadosScreen(navController) // 📌 Pasamos NavController
            "Perfil" -> PerfilScreen()
        }

        Spacer(modifier = Modifier.height(16.dp))

        NavigationBar(selectedScreen) { selectedScreen = it }
    }
}

//PANTALLA INICO SESION
@Composable
fun InicioScreen(nombreUsuario: String) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .background(Color.White, shape = CircleShape)
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo4),
            contentDescription = "Logo",
            modifier = Modifier
                .size(80.dp)
                .align(Alignment.Center)
        )
    }

    Spacer(modifier = Modifier.height(40.dp))

    Text(
        text = "¡Hola, $nombreUsuario!",
        color = Color.White,
        fontSize = 24.sp,
        modifier = Modifier.padding(16.dp)
    )

    Spacer(modifier = Modifier.height(40.dp))

    Button(
        onClick = { /* Acción de jefe */ },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF24BDFF))
    ) {
        Text(text = "Acción del Jefe", color = Color.White)
    }
}


// PANTALLA PARA ASOCIAR RELOJES A CLIENTES
@Composable
fun AsociarRelojScreen() {
    Text(
        text = "Asociar Reloj a Usuario",
        color = Color.White,
        fontSize = 20.sp,
        modifier = Modifier.padding(16.dp)
    )
    // Aquí agregas el contenido para asociar el reloj
}


@Composable
fun VerEmpleadosScreen(navController: NavController) {
    var selectedOption by remember { mutableStateOf("Menu") } // Controla la pantalla a mostrar

    when (selectedOption) {
        "Menu" -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Menú de empleados",
                    color = Color.White,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(16.dp)
                )

                Spacer(modifier = Modifier.height(40.dp))

                Button(
                    onClick = { selectedOption = "Alta" }, // Cambia la pantalla
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Cyan)
                ) {
                    Text(text = "Alta Empleado", color = Color.White)
                }

                Spacer(modifier = Modifier.height(40.dp))

                Button(
                    onClick = { selectedOption = "Modificar" },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Cyan)
                ) {
                    Text(text = "Modificar Empleado", color = Color.White)
                }

                Spacer(modifier = Modifier.height(40.dp))

                Button(
                    onClick = { selectedOption = "Borrar" },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Cyan)
                ) {
                    Text(text = "Borrar Empleado", color = Color.White)
                }
            }
        }
        "Alta" -> AltaEmpleadoScreen { selectedOption = "Menu" } // Pasamos callback para regresar
        "Modificar" -> ModificarEmpleadoScreen { selectedOption = "Menu" }
        "Borrar" -> BorrarEmpleadoScreen { selectedOption = "Menu" }
    }
}
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
                    label = {Text("Contraseña")},
                    modifier = Modifier.fillMaxWidth()
                )

                // Dropdown para seleccionar el puesto
                var expanded by remember { mutableStateOf(false) }
                Box {
                    OutlinedTextField(
                        value = puesto,
                        onValueChange = {},
                        label = { Text("Puesto") },
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expanded = true }
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        listOf("Camarero", "Jefe", "Cocinero").forEach { role ->
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
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
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
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Modificar Empleado",
            color = Color.White,
            fontSize = 20.sp,
            modifier = Modifier.padding(16.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { onBack() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text(text = "Volver", color = Color.White)
        }
    }
}

// Pantalla para borrar empleados
@Composable
fun BorrarEmpleadoScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Borrar Empleado",
            color = Color.White,
            fontSize = 20.sp,
            modifier = Modifier.padding(16.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { onBack() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text(text = "Volver", color = Color.White)
        }
    }
}




//PANTALLA PERFIL DEL JEFE
@Composable
fun PerfilScreen() {
    Text(
        text = "Mi Perfil",
        color = Color.White,
        fontSize = 20.sp,
        modifier = Modifier.padding(16.dp)
    )

    Spacer(modifier = Modifier.height(20.dp))

    Button(
        onClick = { /* Lógica para cerrar sesión */ },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
    ) {
        Text(text = "Cerrar Sesión", color = Color.White)
    }
}


//MENU ABAJO DEL TODO
@Composable
fun NavigationBar(selectedScreen: String, onScreenSelected: (String) -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter // Fijar la barra en la parte inferior
    ) {
        NavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp), // Ajustar la altura
            containerColor = Color.DarkGray,
            contentColor = Color.White
        ) {
            NavigationBarItem(
                selected = selectedScreen == "Inicio",
                onClick = { onScreenSelected("Inicio") },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.home),
                        contentDescription = "Inicio",
                        modifier = Modifier.size(20.dp) // Reducir tamaño del icono
                    )
                },
                label = { Text("Inicio", fontSize = 12.sp) } // Reducir tamaño del texto
            )
            NavigationBarItem(
                selected = selectedScreen == "Asociar",
                onClick = { onScreenSelected("Asociar") },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.du_asociar),
                        contentDescription = "Asociar",
                        modifier = Modifier.size(20.dp)
                    )
                },
                label = { Text("Asociar", fontSize = 12.sp) }
            )
            NavigationBarItem(
                selected = selectedScreen == "Empleados",
                onClick = { onScreenSelected("Empleados") },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.empleados),
                        contentDescription = "Empleados",
                        modifier = Modifier.size(20.dp)
                    )
                },
                label = { Text("Empleados", fontSize = 12.sp) }
            )
            NavigationBarItem(
                selected = selectedScreen == "Perfil",
                onClick = { onScreenSelected("Perfil") },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.usuario),
                        contentDescription = "Perfil",
                        modifier = Modifier.size(20.dp)
                    )
                },
                label = { Text("Perfil", fontSize = 12.sp) }
            )
        }
    }
}
