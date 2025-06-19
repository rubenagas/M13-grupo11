package com.example.firebase

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import androidx.navigation.compose.rememberNavController
import androidx.navigation.findNavController
import com.example.firebase.presentacion.inicio.InicioPantalla
import com.example.firebase.ui.theme.FirebaseTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

// Esta es la clase que arranca la app.
// Se inicializa Firebase y se prepara la navegación con Compose.
// El tema se aplica, se crea el navController y se lanza la interfaz.
//Ultima actualización para la persistencia de datos.
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializacion de Firebase
        FirebaseApp.initializeApp(this)

        // Verifica si hay sesión activa con Firebase
        val user = FirebaseAuth.getInstance().currentUser
        val startDestino = if (user != null) "eventos" else "inicio"

        setContent {
            val navController = rememberNavController()

            FirebaseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Llama al sistema de navegación principal que lleva a la ruta entre pantallas
                    Navegacion( navController, startDestino)
                }
            }
        }

    }


    }

