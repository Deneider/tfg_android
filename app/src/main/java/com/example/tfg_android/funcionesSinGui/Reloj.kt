package com.example.tfg_android.funcionesSinGui;

data class Reloj (
    val id_reloj: String, // Primary Key
    val ip: String? // Puede ser nulo porque en la DB es nullable
)
