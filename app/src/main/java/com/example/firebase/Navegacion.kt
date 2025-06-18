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

// IMPORTA TU FooterBar REAL AQUÍ SI ESTÁ EN OTRO ARCHIVO
// Ejemplo: import com.example.firebase.presentacion.common.FooterBar

// IMPORTA TU RolPantalla REAL AQUÍ SI ESTÁ EN OTRO ARCHIVO Y NO USAS EL PLACEHOLDER DE ABAJO
// Ejemplo: import com.example.firebase.presentacion.rol.RolPantalla


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

@Composable
fun Navegacion(navHostController: NavHostController) {

    val rutasSinFooter = listOf(Rutas.Inicio, Rutas.Login, Rutas.SignUp, Rutas.Recuperar, Rutas.Rol)
    val navBackStackEntry = navHostController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute !in rutasSinFooter) {
                // Asegúrate de que este FooterBar sea el que quieres usar.
                // Si tienes uno en `common`, se usará ese si lo importas.
                // Si no importas ninguno y tienes el placeholder arriba, se usará ese.
                FooterBar(navController = navHostController)
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navHostController,
            startDestination = Rutas.Inicio,
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
            composable(Rutas.Rol) {
                // Esto usará el placeholder RolPantalla definido arriba.
                // Reemplázalo o impórtalo si tu pantalla real está en otro lugar.
                RolPantalla(navController = navHostController)
            }

            // --- Ruta para Detalles del Evento (con argumento) ---
            composable(
                route = Rutas.DetallesEvento,
                arguments = listOf(navArgument(Rutas.DetallesEventoArgName) {
                    type = NavType.StringType
                    // Considera si el argumento puede ser nulo o no.
                    // Si siempre debe estar presente, `nullable = false` (por defecto).
                })
            ) { backStackEntry ->
                val eventoId = backStackEntry.arguments?.getString(Rutas.DetallesEventoArgName)
                if (eventoId != null) {
                    // LLAMA A TU PANTALLA DE DETALLES DE EVENTO REAL AQUÍ
                    // Ejemplo:
                    // DetallesEventoPantalla(navController = navHostController, eventoId = eventoId)
                    Text("Detalles del Evento ID: $eventoId", modifier = Modifier.padding(16.dp)) // Placeholder
                } else {
                    // Manejar el caso donde el eventoId es nulo o no se proporciona
                    Text("Error: ID del evento no encontrado.", modifier = Modifier.padding(16.dp))
                    // Considera navHostController.popBackStack() para volver si el ID es esencial
                }
            }
        }
    }
}