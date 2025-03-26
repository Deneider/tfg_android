package com.example.tfg_android

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.res.painterResource
import com.example.tfg_android.ui.theme.Tfg_androidTheme

class MenuJefeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences: SharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val nombreUsuario = sharedPreferences.getString("username", "Usuario desconocido") ?: "Usuario desconocido"

        setContent {
            Tfg_androidTheme {
                MenuJefeScreen(nombreUsuario)
            }
        }
    }
}

@Composable
fun MenuJefeScreen(nombreUsuario: String) {
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
            "Empleados" -> VerEmpleadosScreen()
            "Perfil" -> PerfilScreen()
        }

        Spacer(modifier = Modifier.height(16.dp))

        NavigationBar(selectedScreen) { selectedScreen = it }
    }
}

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
fun VerEmpleadosScreen() {
    Text(
        text = "Menú de empleados",
        color = Color.White,
        fontSize = 20.sp,
        modifier = Modifier.padding(16.dp)
    )
    Spacer(modifier = Modifier.height(40.dp))

    //Boton crear empleados
    Button(
        onClick = {/*logica llevar a creacion de empleados*/},
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Cyan)
    ){
        Text(text = "Alta Empleado", color = Color.White)
    }


    Spacer(modifier = Modifier.height(40.dp))

    //Boton modificar empleados
    Button(
        onClick = {/*logica llevar a modificar de empleados*/},
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Cyan)
    ){
        Text(text = "Modificar Empleado", color = Color.White)
    }

    Spacer(modifier = Modifier.height(40.dp))

    //Boton borrar empleados
    Button(
        onClick = {/*logica llevar a borrar de empleados*/},
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Cyan)
    ){
        Text(text = "Borrar Empleado", color = Color.White)
    }
}

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
