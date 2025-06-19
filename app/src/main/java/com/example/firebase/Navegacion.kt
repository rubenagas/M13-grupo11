package com.example.firebase

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text // Import para el placeholder de RolPantalla
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp // Necesario para Modifier.padding(16.dp) en RolPantalla Placeholder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument

// IMPORTACIONES DE PANTALLAS DE AUTENTICACIÓN
import com.example.firebase.presentacion.autentificacion.LoginPantalla
import com.example.firebase.presentacion.autentificacion.RecuperarPantalla
import com.example.firebase.presentacion.autentificacion.RegistroPantalla
import com.example.firebase.presentacion.autentificacion.RolPantalla

// IMPORTACIONES DE PANTALLAS PRINCIPALES
import com.example.firebase.presentacion.configuracion.ConfiguracionPantalla
import com.example.firebase.presentacion.equipo.EquipoPantalla
import com.example.firebase.presentacion.inicio.InicioPantalla
import com.example.firebase.presentacion.perfil.PerfilPantalla

// IMPORTACIONES DE PANTALLAS DE EVENTOS
import com.example.firebase.presentacion.evento.CrearEventoPantalla
// import com.example.firebase.presentacion.evento.DetallesEventoPantalla // Descomenta si tienes esta pantalla
import com.example.firebase.presentacion.evento.EventosPantalla
import com.example.firebase.presentacion.evento.ResumenEventosPantalla
import com.example.firebase.presentacion.evento.SeleccionarParticipantesPantalla

// IMPORTACIONES DE OTRAS PANTALLAS
import com.example.firebase.presentacion.jugadores.GestionarJugadoresPantalla


object Rutas {
    const val Inicio = "inicio"
    const val Login = "login"
    const val SignUp = "registro"
    const val Perfil = "perfil"
    const val Eventos = "eventos"
    const val Equipo = "equipo"
    const val Recuperar = "recuperar"
    const val Rol = "rol" // Ruta para la selección de rol

    // Rutas de eventos
    const val CrearEvento = "crear_evento"
    const val SeleccionarParticipantes = "seleccionar_participantes"
    const val ResumenEventos = "resumen_eventos"
    const val GestionarJugadores = "gestionar_jugadores"
    const val Configuracion = "configuracion"

    // Ruta para detalles de un evento (con argumento)
    const val DetallesEventoArgName = "eventoId"
    const val DetallesEventoBase = "detalles_evento"
    const val DetallesEvento = "$DetallesEventoBase/{$DetallesEventoArgName}"

    fun construirRutaDetallesEvento(eventoId: String): String {
        return "$DetallesEventoBase/$eventoId"
    }
}
// Este bloque se encarga de manejar toda la navegación de la app.
// Usa NavHostController para moverse entre pantallas como login, registro, perfil, eventos, etc.
// Tiene un Scaffold que muestra el footer solo si la ruta actual NO está en la lista de "rutas sin footer".
// Dentro del NavHost se definen todas las rutas de la app y se indica qué pantalla mostrar en cada una.
// También maneja rutas especiales como "detalles de evento", donde se pasa un ID como argumento.

@Composable
fun Navegacion(navHostController: NavHostController, startDestino: String) {

    val rutasSinFooter = listOf(Rutas.Inicio, Rutas.Login, Rutas.SignUp, Rutas.Recuperar, Rutas.Rol)
    val navBackStackEntry = navHostController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute !in rutasSinFooter) {
                FooterBar(navController = navHostController)
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navHostController,
            startDestination = startDestino,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Rutas.Inicio) {
                InicioPantalla(navController = navHostController)
            }
            composable(Rutas.Login) {
                LoginPantalla(navController = navHostController)
            }
            composable(Rutas.SignUp) {
                RegistroPantalla(navController = navHostController)
            }
            composable(Rutas.Recuperar) {
                RecuperarPantalla(navController = navHostController)
            }
            composable(Rutas.Rol) {
                 RolPantalla(navController = navHostController)
             }


            composable(Rutas.Perfil) {
                PerfilPantalla(navController = navHostController)
            }
            composable(Rutas.Equipo) {
                // Asegurense que EquipoPantalla NO tiene su propio Scaffold con FooterBar
                EquipoPantalla(navController = navHostController)
            }

            // --- Pantallas de Eventos ---
            composable(Rutas.Eventos) {
                EventosPantalla(navController = navHostController)
            }
            composable(Rutas.CrearEvento) {
                CrearEventoPantalla(navController = navHostController)
            }
            composable(Rutas.SeleccionarParticipantes) {
                SeleccionarParticipantesPantalla(navController = navHostController)
            }
            composable(Rutas.ResumenEventos) {
                ResumenEventosPantalla(navController = navHostController)
            }

            // --- Otras Pantallas ---
            composable(Rutas.GestionarJugadores) {
                GestionarJugadoresPantalla(navController = navHostController)
            }
            composable(Rutas.Configuracion) {
                ConfiguracionPantalla(navController = navHostController)
            }

            // --- Ruta para Selección de Rol ---
            composable(Rutas.Rol) { // Esta es la única definición ahora
                RolPantalla(navController = navHostController)
            }

            // --- Ruta para Detalles del Evento (con argumento) ---
            composable(
                route = Rutas.DetallesEvento,
                arguments = listOf(navArgument(Rutas.DetallesEventoArgName) {
                    type = NavType.StringType
                })
            ) { backStackEntry ->
                val eventoId = backStackEntry.arguments?.getString(Rutas.DetallesEventoArgName)
                if (eventoId != null) {
                    Text("Detalles del Evento ID: $eventoId", modifier = Modifier.padding(16.dp)) // Placeholder
                } else {
                    Text("Error: ID del evento no encontrado.", modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}