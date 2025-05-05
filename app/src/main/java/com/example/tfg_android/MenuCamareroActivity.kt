package com.example.tfg_android

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tfg_android.pantallas.AltaClienteScreen
import com.example.tfg_android.pantallas.AsociarReloj
import com.example.tfg_android.pantallas.CobrarPuntos
import com.example.tfg_android.pantallas.DesAsociarReloj
import com.example.tfg_android.pantallas.ModificarClienteScreen
import com.example.tfg_android.pantallas.RecargarPuntos
import com.example.tfg_android.ui.theme.Tfg_androidTheme

class MenuCamareroActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Recuperamos el nombre del usuario desde SharedPreferences
        val sharedPreferences: SharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val nombreUsuario = sharedPreferences.getString("username", "Usuario desconocido") ?: "Usuario desconocido"

        // Configuramos el contenido de la actividad
        setContent {
            Tfg_androidTheme {
                MenuCamareroScreen(nombreUsuario = nombreUsuario)
            }
        }
    }
}

@Composable
fun MenuCamareroScreen(nombreUsuario: String) {
    // Usar NavController para gestionar la navegación
    val navController = rememberNavController()

    // Establecer la vista principal con Scaffold para la barra de navegación inferior
    Scaffold(
        bottomBar = {
            CamareroBottomNavigationBar(navController = navController)
        }
    ) { paddingValues ->
        // Contenido principal
        Box(modifier = Modifier.padding(paddingValues)) {
            // Composable que gestiona las pantallas
            NavHost(navController = navController, startDestination = "perfil") {
                composable("perfil") {
                    CamareroPerfilScreen(nombreUsuario = nombreUsuario)
                }
                composable("relojes") {
                    CamareroRelojesScreen()
                }
                composable("clientes") {
                    CamareroClientesScreen()
                }
            }
        }
    }
}

@Composable
fun CamareroBottomNavigationBar(navController: NavHostController) {
    BottomNavigation(
        backgroundColor = Color(0xFF121212),
        contentColor = Color.White,
        elevation = 8.dp
    ) {
        BottomNavigationItem(
            selected = false, // Cambiar a un estado si es necesario para mostrar la selección
            onClick = { navController.navigate("relojes") },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.du_asociar),
                    contentDescription = "Relojes",
                    tint = Color.White,
                    modifier = Modifier.size(50.dp) // Íconos más grandes
                )
            },
            label = { Text("Relojes", color = Color.White, fontSize = 18.sp) }, // Texto más grande
            selectedContentColor = Color(0xFF24BDFF),
            unselectedContentColor = Color.Gray
        )
        BottomNavigationItem(
            selected = false,
            onClick = { navController.navigate("clientes") },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.clientes),
                    contentDescription = "Clientes",
                    tint = Color.White,
                    modifier = Modifier.size(50.dp) // Íconos más grandes
                )
            },
            label = { Text("Clientes", color = Color.White, fontSize = 18.sp) }, // Texto más grande
            selectedContentColor = Color(0xFF24BDFF),
            unselectedContentColor = Color.Gray
        )
        BottomNavigationItem(
            selected = false,
            onClick = { navController.navigate("perfil") },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.usuario),
                    contentDescription = "Perfil",
                    tint = Color.White,
                    modifier = Modifier.size(50.dp) // Íconos más grandes
                )
            },
            label = { Text("Perfil", color = Color.White, fontSize = 18.sp) }, // Texto más grande
            selectedContentColor = Color(0xFF24BDFF),
            unselectedContentColor = Color.Gray
        )
    }
}


//  1.  RELOJES
@Composable
fun CamareroRelojesScreen() {
    var opcionSeleccionada by remember { mutableStateOf("MenuCamarero") }

    when (opcionSeleccionada) {
        "MenuCamarero" -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = "Pantalla de Relojes",
                    color = Color.White,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                Text(
                    text = "Asociaciones de relojes",
                    color = Color.White,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                Button(
                    onClick = { opcionSeleccionada = "AsociarReloj" },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF24BDFF))
                ) {
                    Text(text = "Vincular reloj", color = Color.White)
                }
                Button(
                    onClick = { opcionSeleccionada = "DesAsociarReloj" },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF24BDFF))
                ){
                    Text(text = "Desvincular reloj", color = Color.White)
                }

                Text(
                    text = "Cobro y recarga de puntos",
                    color = Color.White,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
                Button(
                    onClick = { opcionSeleccionada = "CobrarPuntos" },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF24BDFF))
                ){
                    Text(text  = "Cobrar", color = Color.White)
                }
                Button(
                    onClick = { opcionSeleccionada = "RecargarPuntos" },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF24BDFF))
                ) {
                    Text(text  = "Recargar", color = Color.White)
                }
            }
        }

        "AsociarReloj" -> {
            AsociarReloj {
                opcionSeleccionada = "MenuCamarero"
            }
        }
        "DesAsociarReloj" -> {
            DesAsociarReloj{
                opcionSeleccionada = "MenuCamarero"
            }
        }
        // AÑADIR FUNCIONALIDADES DE COBRO Y PAGOS
        "CobrarPuntos" ->{
            CobrarPuntos{
                opcionSeleccionada = "MenuCamarero"
            }
        }
        "RecargarPuntos" -> {
            RecargarPuntos{
                opcionSeleccionada = "MenuCamarero"
            }
        }
    }
}

// 2.   CLIENTES
@Composable
fun CamareroClientesScreen() {
    var opcionSeleccionada by remember { mutableStateOf("MenuCamarero") }

    when (opcionSeleccionada) {
        "MenuCamarero" -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = "Pantalla Clientes",
                    color = Color.White,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                Button(
                    onClick = { opcionSeleccionada = "CrearCliente" },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF24BDFF))
                ) {
                    Text(text = "Crear Cliente", color = Color.White)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { opcionSeleccionada = "ModificarCliente" },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107)) // Amarillo
                ) {
                    Text(text = "Modificar Cliente", color = Color.Black)
                }

                Spacer(modifier = Modifier.height(8.dp))
                /*
                Button(
                    onClick = { opcionSeleccionada = "BorrarCliente" },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text(text = "Borrar Cliente", color = Color.White)
                }*/
            }
        }

        "CrearCliente" -> {
            AltaClienteScreen {
                opcionSeleccionada = "MenuCamarero"
            }
        }

        "ModificarCliente" -> {
            ModificarClienteScreen {
                opcionSeleccionada = "MenuCamarero"
            }
        }
        /*
        "BorrarCliente" -> {
            BorrarClienteScreen {
                opcionSeleccionada = "MenuCamarero"
            }
        }
         */
    }
}

//  3. PERFIL
@Composable
fun CamareroPerfilScreen(nombreUsuario: String) {
    // Aquí va la interfaz de la pantalla de Perfil
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Perfil de $nombreUsuario", color = Color.White, fontSize = 24.sp) // Texto más grande
    }
}