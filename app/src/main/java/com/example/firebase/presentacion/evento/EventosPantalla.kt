package com.example.firebase.presentacion.evento

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
// import androidx.compose.foundation.clickable // Ya no es necesario para EventoCard
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.firebase.ui.theme.Grey
import com.example.firebase.ui.theme.MidnightBlue
import com.example.firebase.ui.theme.Secondary
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.ui.platform.LocalContext
import com.example.firebase.data.local.LocalStorageManager
import com.google.firebase.firestore.FirebaseFirestore

// Modelo de datos para los eventos
data class Evento(
    val id: String = "",
    val nombre: String = "",
    val fecha: String = "",
    val tipo: String = "",
    val horaReunion: String = "",
    val horaInicio: String = "",
    val horaFin: String = "",
    val direccion: String = "",
    val descripcion: String = "",
    val participantes: List<String> = emptyList(),
    val asistentes: List<String> = emptyList(),
    val creadorId: String = "",
    val timestamp: Long = 0L,
    val activo: Boolean = true
)

@Composable
fun EventosPantalla(
    navController: NavHostController
) {
    var selectedTab by remember { mutableStateOf(0) }
    var userRole by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    var eventos by remember { mutableStateOf<List<Evento>>(emptyList()) }
    var isLoadingEventos by remember { mutableStateOf(true) }

    val context = LocalContext.current
    val storageManager = remember { LocalStorageManager(context) }

    // Obtener el rol del usuario actual
    LaunchedEffect(Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            FirebaseFirestore.getInstance()
                .collection("usuarios")
                .document(currentUser.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val rolString = document.getString("rol")
                        userRole = rolString?.trim() ?: "jugador"
                        isLoading = false
                    } else {
                        FirebaseFirestore.getInstance()
                            .collection("users")
                            .document(currentUser.uid)
                            .get()
                            .addOnSuccessListener { userDoc ->
                                if (userDoc.exists()) {
                                    val rol = userDoc.getString("rol")?.trim() ?: "jugador"
                                    userRole = rol
                                } else {
                                    userRole = "jugador"
                                }
                                isLoading = false
                            }
                            .addOnFailureListener {
                                userRole = "jugador"
                                isLoading = false
                            }
                    }
                }
                .addOnFailureListener {
                    userRole = "jugador"
                    isLoading = false
                }
        } else {
            userRole = "jugador"
            isLoading = false
        }
    }

    // Cargar eventos desde almacenamiento local y recargar cuando la pantalla vuelve a estar visible
    // Usamos produceState para manejar la carga inicial y la recarga
    val eventosState by produceState<List<Evento>>(initialValue = emptyList(), storageManager) {
        isLoadingEventos = true
        try {
            storageManager.inicializarJugadoresPorDefecto() // Asegurarse que se inicializa
            value = storageManager.obtenerEventos()
                .filter { it.activo }
                .sortedByDescending { it.timestamp }
        } catch (e: Exception) {
            println("Error al cargar eventos: ${e.message}")
            value = emptyList() // En caso de error, lista vacía
        }
        isLoadingEventos = false

        // Esto es para recargar cuando la pantalla se vuelve visible de nuevo,
        // pero LocalStorageManager no tiene un sistema de observadores incorporado.
        // Si necesitas una actualización en tiempo real desde LocalStorageManager
        // tendrías que implementar un mecanismo de escucha o re-cargar al entrar en la pantalla.
        // Por ahora, este produceState se ejecutará una vez cuando entre en composición.
        // Para forzar la recarga al volver, se podría usar un LaunchedEffect con una clave
        // que cambie (ej. un contador que se incremente al navegar fuera y volver).
        // O una forma más simple es recargar en un onResume si esto fuera una Activity/Fragment.
        // En Compose, podrías usar un `DisposableEffect` y `LocalLifecycleOwner`.
    }

    // Actualizar la variable 'eventos' cuando 'eventosState' cambie
    LaunchedEffect(eventosState) {
        eventos = eventosState
    }

    // Recargar eventos cuando la pantalla vuelve a estar en primer plano
    // Esto es un ejemplo, necesitarías una forma de saber cuándo la pantalla vuelve a estar en primer plano
    // Podrías usar LocalLifecycleOwner si esta pantalla fuera el destino principal de una ruta.
    DisposableEffect(Unit) { // Este DisposableEffect podría simplificarse o eliminarse
        // si el produceState maneja la carga inicial adecuadamente.
        // Si el objetivo es recargar cuando se vuelve, se necesita una estrategia diferente.
        val reloadEvents = {
            isLoadingEventos = true
            try {
                eventos = storageManager.obtenerEventos()
                    .filter { it.activo }
                    .sortedByDescending { it.timestamp }
            } catch (e: Exception) {
                println("Error al recargar eventos: ${e.message}")
            }
            isLoadingEventos = false
        }
        // reloadEvents() // Podrías llamar esto aquí para la carga inicial si no usas produceState

        onDispose { }
    }


    val isEntrenador = userRole.equals("Entrenador", ignoreCase = true) ||
            userRole.equals("entrenador", ignoreCase = true)

    Scaffold { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    Brush.verticalGradient(
                        listOf(Grey, MidnightBlue),
                        startY = 0f,
                        endY = 600f
                    )
                )
        ) {
            val screenHeight = maxHeight

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                item { Spacer(modifier = Modifier.height(screenHeight * 0.05f)) }
                item {
                    Text(
                        text = "EVENTOS",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Secondary
                    )
                }
                item { Spacer(modifier = Modifier.height(8.dp)) }
                item {
                    Text(
                        text = "PRÓXIMOS EVENTOS",
                        fontSize = 14.sp,
                        color = Color.LightGray,
                        fontStyle = FontStyle.Italic
                    )
                }

                if (isLoading || isLoadingEventos) {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                CircularProgressIndicator(color = Secondary)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = if (isLoading) "Cargando usuario..." else "Cargando eventos...",
                                    color = Color.White
                                )
                            }
                        }
                    }
                }

                if (!isLoading && isEntrenador) {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                try {
                                    navController.navigate("crear_evento") {
                                        launchSingleTop = true
                                    }
                                } catch (e: Exception) {
                                    println("Error al navegar a crear_evento: ${e.message}")
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Secondary),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("CREAR UN NUEVO EVENTO +", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        TabButton(text = "Quitar filtro", isSelected = selectedTab == 0) { selectedTab = 0 }
                        TabButton(text = "Entrenamiento", isSelected = selectedTab == 1) { selectedTab = 1 }
                    }
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }

                val filteredEventos = when (selectedTab) {
                    1 -> eventos.filter { it.tipo.equals("Entrenamiento", ignoreCase = true) }
                    else -> eventos
                }

                if (filteredEventos.isEmpty() && !isLoadingEventos) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f))
                        ) {
                            Text(
                                text = "No hay eventos disponibles",
                                color = Color.Gray,
                                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    items(filteredEventos) { evento ->
                        // Se elimina el parámetro onClick de EventoCard
                        EventoCard(evento = evento)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                item { Spacer(modifier = Modifier.height(24.dp)) }
                item {
                    Button(
                        onClick = { navController.navigate("resumen_eventos") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp), // Aplicar padding horizontal aquí también si se desea
                        colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Resumen de eventos", fontWeight = FontWeight.Medium)
                    }
                }
                item { Spacer(modifier = Modifier.height(screenHeight * 0.05f)) } // Espaciado inferior
            }
        }
    }
}

@Composable
fun EventoCard(
    evento: Evento
    // Se elimina el parámetro onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(), // Se elimina .clickable { onClick() }
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Secondary),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.05f)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = evento.nombre,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            InfoRow("Fecha:", evento.fecha)
            InfoRow("Tipo:", evento.tipo)
            InfoRow("Reunión:", evento.horaReunion)
            InfoRow("Inicio:", evento.horaInicio)
            InfoRow("Fin:", evento.horaFin)
            InfoRow("Dirección:", evento.direccion)
            if (evento.descripcion.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = evento.descripcion,
                    fontSize = 14.sp,
                    color = Color.LightGray,
                    fontStyle = FontStyle.Italic
                )
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    if (value.isNotBlank()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = label,
                fontSize = 14.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.width(90.dp) // Ancho fijo para etiquetas
            )
            Text(
                text = value,
                fontSize = 14.sp,
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
    }
}

@Composable
fun TabButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Secondary else Color.Transparent,
            contentColor = if (isSelected) Color.Black else Color.White
        ),
        border = if (!isSelected) BorderStroke(1.dp, Secondary) else null,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        Text(text, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewEventosPantalla() {
    // Para el Preview, envuélvelo en tu tema si los colores dependen de él.
    // TuAppTheme {
    EventosPantalla(navController = rememberNavController())
    // }
}