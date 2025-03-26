package com.example.tfg_android

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tfg_android.ui.theme.Tfg_androidTheme

class MenuCamareroActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Recuperamos el nombre del usuario desde SharedPreferences
        val sharedPreferences: SharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val nombreUsuario = sharedPreferences.getString("username", "Usuario desconocido")  // Default value in case no username is found

        // Configuramos el contenido de la actividad
        setContent {
            Tfg_androidTheme {
                MenuCamareroScreen(nombreUsuario = nombreUsuario ?: "Usuario desconocido")
            }
        }
    }
}

@Composable
fun MenuCamareroScreen(nombreUsuario: String) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color.White, shape = CircleShape)

        ) {
            Image(
                painter = painterResource(id = R.drawable.logo4), // Asegúrate de tener el logo en los recursos
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
            onClick = {
                // Aquí puedes agregar la funcionalidad de lo que deseas hacer después del clic
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF24BDFF))
        ) {
            Text(text = "Acción del Camarero", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Aquí puedes agregar otros botones o elementos según lo que necesites mostrar
    }
}
