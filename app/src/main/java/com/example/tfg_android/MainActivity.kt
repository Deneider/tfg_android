package com.example.tfg_android

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.tfg_android.ui.theme.Tfg_androidTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Tfg_androidTheme {
                LoginScreen(onLogin = { correo, contrasena ->
                    // Aquí debes llamar a la función correctamente
                    getTrabajadorByCorreo(correo, contrasena) // Pasamos el correo y la contrasena al método
                })
            }
        }
    }

    // Asegúrate de que la función se llame correctamente
    private fun getTrabajadorByCorreo(correo: String, contrasena: String) {
        val apiService = RetrofitClient.retrofitInstance.create(ApiService::class.java)

        apiService.getTrabajadorByCorreo(correo).enqueue(object : Callback<Trabajador> {
            override fun onResponse(call: Call<Trabajador>, response: Response<Trabajador>) {
                if (response.isSuccessful) {
                    val trabajador = response.body()
                    if (trabajador != null && trabajador.contrasena == contrasena) {
                        // El trabajador existe y la contraseña es correcta
                        val sharedPreferences: SharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("username", trabajador.nombre)
                        editor.apply()

                        // Redirigir según el puesto
                        if (trabajador.puesto == "jefe") {
                            startActivity(Intent(this@MainActivity, MenuJefeActivity::class.java))
                        } else if (trabajador.puesto == "camarero") {
                            startActivity(Intent(this@MainActivity, MenuCamareroActivity::class.java))
                        }
                    } else {
                        Toast.makeText(applicationContext, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(applicationContext, "Error en la respuesta: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Trabajador>, t: Throwable) {
                Toast.makeText(applicationContext, "Error en la solicitud: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
