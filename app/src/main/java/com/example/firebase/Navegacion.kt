package com.example.firebase

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.firebase.presentacion.autentificacion.LoginPantalla
import com.example.firebase.presentacion.autentificacion.RecuperarPantalla
import com.example.firebase.presentacion.autentificacion.SignUpPantalla
import com.example.firebase.presentacion.equipo.EquipoPantalla
import com.example.firebase.presentacion.evento.EventosPantalla
import com.example.firebase.presentacion.inicio.InicioPantalla
import com.example.firebase.presentacion.perfil.PerfilPantalla


object Rutas {
    const val Inicio = "inicio"
    const val Login = "login"
    const val SignUp = "registro"
    const val Perfil = "perfil"
    const val Eventos = "eventos"
    const val Equipo = "equipo"
    const val Recuperar = "recuperar"
}

@Composable
fun Navegacion(navHostController: NavHostController) {

    val rutasSinFooter = listOf(Rutas.Inicio, Rutas.Login, Rutas.SignUp, Rutas.Recuperar)
    val currentRoute = navHostController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute !in rutasSinFooter) {
                FooterBar(navController = navHostController)
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navHostController,
            startDestination = Rutas.Inicio,
            modifier = androidx.compose.ui.Modifier.padding(paddingValues)
        ) {
            composable(Rutas.Inicio) {
                InicioPantalla(navController = navHostController)
            }
            composable(Rutas.Login) {
                LoginPantalla(navController = navHostController)
            }
            composable(Rutas.SignUp) {
                SignUpPantalla(navController = navHostController)
            }

            composable(Rutas.Recuperar) {
            RecuperarPantalla(navController = navHostController)
        }

            // Puedes seguir añadiendo:
            composable(Rutas.Perfil) {
                PerfilPantalla(navController = navHostController)
            }
            composable(Rutas.Eventos) {
                EventosPantalla(navController = navHostController)
            }
            composable(Rutas.Equipo) {
                EquipoPantalla(navController = navHostController)
            }
        }
    }
}
