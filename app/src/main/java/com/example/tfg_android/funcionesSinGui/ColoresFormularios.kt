package com.example.tfg_android.funcionesSinGui
// En esta clase pongo el "por defecto" de los colores de los formularios y algunos botones...
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object ColoresFormularios {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun textoBlanco() = TextFieldDefaults.outlinedTextFieldColors(
        focusedTextColor = Color.White,                     // Color del  texto cuando esta siendo usado
        unfocusedTextColor = Color(0xFF24BDFF),      // Color del texto cuando el campo no está usado
        cursorColor = Color.White,                          // Color del cursor que parpadea al rellenar los campos
        focusedBorderColor = Color.White,                   // Borde del campo cuando esta siendo usado
        unfocusedBorderColor = Color(0xFF24BDFF),    // Borde del campo cuando no está siendo usado
        focusedLabelColor = Color.White,                    // Color del label cuando esta siendo usado
        unfocusedLabelColor =Color(0xFF24BDFF)          // Color del label cuando no está enfocado
    )
}
