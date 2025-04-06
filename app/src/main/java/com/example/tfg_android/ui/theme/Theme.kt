package com.example.tfg_android.ui.theme

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Definir los colores de la marca
val AzulPrimario = Color(0xFF24BDFF)  // Azul
val Negro = Color(0xFF000000)         // Negro
val Blanco = Color(0xFFFFFFFF)        // Blanco

// Esquema de colores para el tema oscuro
private val DarkColorScheme = darkColorScheme(
    primary = AzulPrimario,
    secondary = AzulPrimario.copy(alpha = 0.7f),  // Azul más suave para elementos secundarios
    surface = Negro,
    background = Negro,
    onPrimary = Blanco,  // El texto en el color primario será blanco
    onSecondary = Blanco,
    onSurface = Blanco,
    onBackground = Blanco
)

// Esquema de colores para el tema claro
private val LightColorScheme = lightColorScheme(
    primary = AzulPrimario,
    secondary = AzulPrimario.copy(alpha = 0.7f),
    surface = Blanco,
    background = Blanco,
    onPrimary = Blanco,
    onSecondary = Negro,
    onSurface = Negro,
    onBackground = Negro
)

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun Tfg_androidTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
