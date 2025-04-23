package com.example.tfg_android.funcionesSinGui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object ColoresFormularios {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun textoBlanco() = TextFieldDefaults.outlinedTextFieldColors(
        focusedTextColor = Color.White,                     // Color del texto cuando el campo está enfocado
        unfocusedTextColor = Color(0xFF24BDFF),      // Color del texto cuando el campo NO está enfocado
        cursorColor = Color.White,                          // Color del cursor que parpadea al escribir
        focusedBorderColor = Color.White,                   // Borde del campo cuando está enfocado
        unfocusedBorderColor = Color(0xFF24BDFF),    // Borde del campo cuando NO está enfocado
        focusedLabelColor = Color.White,                    // Color del label/flotante cuando está enfocado
        unfocusedLabelColor =Color(0xFF24BDFF)          // Color del label/flotante cuando NO está enfocado
    )
}
