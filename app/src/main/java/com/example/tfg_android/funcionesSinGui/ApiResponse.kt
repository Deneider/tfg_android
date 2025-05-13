package com.example.tfg_android.funcionesSinGui
// Esta clase con el método ApiResponse esta hecha para recoger las respuestas del servidor y poder comunicarlas por el APK
data class ApiResponse(
    val success: Boolean,
    val message: String
)