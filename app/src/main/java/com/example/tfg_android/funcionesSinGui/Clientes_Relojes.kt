package com.example.tfg_android.funcionesSinGui;
//Aqui declaro las variables (campos de la base de datos) de Clientes_Relojes, no esta usada pero más adelante se le dara uso
data class Clientes_Relojes (
    val id_cliente: String,
    val id_reloj : String,
    val fecha_asignacion: String,
    val fecha_desasignacion: String
)
