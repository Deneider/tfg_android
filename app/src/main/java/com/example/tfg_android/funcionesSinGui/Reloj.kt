package com.example.tfg_android.funcionesSinGui;
//Aqui declaro las variables (campos de la base de datos) de Reloj (llama a todos los relojes)
data class Reloj (
    val id_reloj: String, // PK
    val ip: String? // Puede ser nulo porque en la DB es nullable
)
