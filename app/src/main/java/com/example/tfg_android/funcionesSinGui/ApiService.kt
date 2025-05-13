package com.example.tfg_android.funcionesSinGui


//En esta clase llamamos a los métodos del endpoint de mi API
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
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
    @POST("apiDesubicados/Trabajadores/crear")
    fun createTrabajador(@Body trabajador: Trabajador): Call<Void>

    // borrar trabajador
    @DELETE("apiDesubicados/Trabajadores/borrar/")
    fun deleteTrabajador(
        @Query("id_trabajador") idTrabajador: Int): Call<ResponseBody>

    //modificar trabajador
    @PUT("apiDesubicados/Trabajadores/modificar/")
    fun updateTrabajador(
        @Query("id_trabajador") idTrabajador: Int,
        @Body trabajador: Trabajador
    ): Call<Void>


    // CLIENTE

    // Para obtener todos los clientes
    @GET("apiDesubicados/Clientes")
    fun getClientes(): Call<List<Cliente>>

    // Para obtener un cliente por correo
    @GET("apiDesubicados/Clientes/correo/")
    fun getClienteByCorreo(@Query("correo") correo: String): Call<Cliente>

    // Obtener cliente por dni
    @GET("apiDesubicados/Clientes/dni/")
    fun getClienteByDni(@Query("dni") dni: String): Call<Cliente>

    // Para crear un cliente
    @POST("apiDesubicados/Clientes/crear")
    fun createCliente(@Body cliente: Cliente): Call<Void>

    // Para borrar un cliente
    @DELETE("apiDesubicados/Clientes/borrar/")
    fun deleteCliente(
        @Query("id_cliente") idCliente: String
    ): Call<ResponseBody>

    // Para modificar un cliente
    @PUT("apiDesubicados/Clientes/modificar/")
    fun updateCliente(
        @Query("id_cliente") idCliente: Int,
        @Body cliente: Cliente
    ): Call<Void>

    // Añadir puntos a un cliente
    @POST("apiDesubicados/Clientes/anadirPuntos/")
    fun anadirPuntos(
        @Query("id_cliente") idCliente: Int,
        @Query("puntos") puntos: Int
    ): Call<Map<String, Any>>

    // Cobrar puntos a un cliente
    @POST("apiDesubicados/Clientes/cobrarPuntos/")
    fun cobrarPuntos(
        @Query("id_cliente") idCliente: Int,
        @Query("puntos") puntos: Int
    ): Call<Map<String, Any>>


    // RELOJES

    //Para obtener todos los relojes
    @GET("apiDesubicados/Relojes")
    fun getRelojes(): Call<List<Reloj>>

    //Clientes_Relojes
    // AsignarRelojes
    @POST("apiDesubicados/Clientes_Relojes/asignar")
    fun asignarReloj(
        @Query("id_cliente") idCliente: String,
        @Query("id_reloj") idReloj: String
    ): Call<ApiResponse>

    // DesasignarReloj
    @DELETE("apiDesubicados/Clientes_Relojes/desasignar")
    fun desasignarReloj(
        @Query("id_cliente") idCliente: String,
        @Query("id_reloj") idReloj: String
    ): Call<ApiResponse>

    // Para obtener al cliente a través de la mac del reloj
    @GET("apiDesubicados/Clientes_Relojes/obtenerPorMacReloj")
    fun getClientePorMacReloj(@Query("mac") mac: String): Call<Cliente>



}
