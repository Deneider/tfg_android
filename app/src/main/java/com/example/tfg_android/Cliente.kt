package com.example.tfg_android

data class Cliente (
    val id_cliente: Int,
    val nombre: String,
    val primer_apellido: String,
    val segundo_apellido: String,
    val fecha_nacimiento: String,
    val dni: String,
    val calle: String,
    val numero_casa: String,
    val localidad: String,
    val provincia: String,
    val cod_postal: String,
    val nacionalidad: String,
    val puntos: Int,
    val correo: String,
    val contrasena: String
)