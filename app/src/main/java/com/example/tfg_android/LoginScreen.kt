package com.example.tfg_android

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tfg_android.funcionesSinGui.ColoresFormularios

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(onLogin: (String, String) -> Unit) {
    var correo by remember { mutableStateOf(TextFieldValue("")) }
    var contrasena by remember { mutableStateOf(TextFieldValue("")) }

    // ANIMACIÓN de opacidad para el logo
    val transition = rememberInfiniteTransition()
    val animationValue by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 2000
                0f at 0 with LinearOutSlowInEasing
                1f at 1000
                0f at 2000
            },
            repeatMode = RepeatMode.Reverse
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
            .imePadding(),  // Para que el teclado no tape los campos
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color.White, shape = CircleShape)
                .alpha(animationValue) // Aplicamos la animación aquí
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

        Text(text = "Bienvenido/a a DesUbicados", color = Color.White, fontSize = 24.sp)

        Spacer(modifier = Modifier.height(40.dp))

        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Correo electrónico", color = Color.White) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            colors = ColoresFormularios.textoBlanco()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Contraseña", color = Color.White) },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            colors = ColoresFormularios.textoBlanco()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (correo.text.isNotBlank() && contrasena.text.isNotBlank()) {
                    onLogin(correo.text, contrasena.text)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF24BDFF))
        ) {
            Text(text = "Iniciar sesión", color = Color.White)
        }
    }
}
