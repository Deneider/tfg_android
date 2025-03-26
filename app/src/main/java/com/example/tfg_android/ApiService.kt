package com.example.tfg_android

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    // TRABAJADOR

    // Para obtener todos los trabajadores
    @GET("apiDesubicados/Trabajadores")
    fun getTrabajadores(): Call<List<Trabajador>>

    // Para obtener un trabajador por correo
    @GET("apiDesubicados/Trabajadores/correo/")
    fun getTrabajadorByCorreo(@Query("correo") correo: String): Call<Trabajador>

    // Para crear un trabajador
    @POST("apiDesubicados/Trabajadores")
    fun createTrabajador(@Body trabajador: Trabajador): Call<Void>

    // CLIENTE

    // Para obtener todos los clientes
    @GET("apiDesubicados/Clientes")
    fun getClientes(): Call<List<Cliente>>

    // Para obtener un cliente por correo
    @GET("apiDesubicados/Clientes/correo/")
    fun getClienteByCorreo(@Query("correo") correo: String): Call<Cliente>

    // Para crear un cliente
    @POST("apiDesubicados/Clientes")
    fun createCliente(@Body cliente: Cliente): Call<Void>

}
