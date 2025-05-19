package com.example.tfg_android.funcionesSinGui
//Esta clase sirve para hacer la conexión a la API
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
//Sirve para  configurar y/o poder acceder al Retrofit desde cualquier clase y/o funcionalidad de la app
object RetrofitClient {
    private const val BASE_URL = "http://obkserver.duckdns.org:8000/"    // URL definida
    // una instancia  de retrofit cuando se use por primera vez
    val retrofitInstance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL) //lama a la url declarada antes
            .addConverterFactory(GsonConverterFactory.create()) // cambia el json de fastapi a gson
            .build() //construye el retrofit
    }
    // hace la instancia del servicio api y lo llama apiService
    val apiService: ApiService by lazy {
        retrofitInstance.create(ApiService::class.java) // hace una interfaz de Api
    }
}
