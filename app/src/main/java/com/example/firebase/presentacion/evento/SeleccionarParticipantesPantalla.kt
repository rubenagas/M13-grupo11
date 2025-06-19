package com.example.firebase.presentacion.evento

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
import kotlinx.coroutines.launch
import com.example.firebase.presentacion.evento.Evento
import java.util.UUID
import androidx.compose.foundation.layout.statusBarsPadding

// Modelo de datos para los jugadores
data class Jugador(
    val id: String,
    val nombre: String,
    val apellido: String,
    val numero: String,
    val email: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeleccionarParticipantesPantalla(navController: NavHostController) {
    var jugadores by remember { mutableStateOf<List<Jugador>>(emptyList()) }
    var jugadoresSeleccionados by remember { mutableStateOf(setOf<String>()) }
    var seleccionarTodos by remember { mutableStateOf(false) }
    var enviarAlarma by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    var isCreatingEvent by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val context = LocalContext.current
    val storageManager = remember { LocalStorageManager(context) }

    // Cargar jugadores desde almacenamiento local
    LaunchedEffect(Unit) {
        try {
            // Inicializar jugadores por defecto si es necesario
            storageManager.inicializarJugadoresPorDefecto()

            // Cargar jugadores
            jugadores = storageManager.obtenerJugadores()
            isLoading = false
        } catch (e: Exception) {
            println("Error al cargar jugadores: ${e.message}")
            isLoading = false
        }
    }

    // Función para crear el evento
    fun crearEvento() {
        if (jugadoresSeleccionados.isEmpty()) {
            scope.launch {
                snackbarHostState.showSnackbar("Debes seleccionar al menos un participante")
            }
            return
        }

        // Obtener los datos del evento del objeto singleton
        val eventoData = EventoDataHolder.eventoTemporal
        if (eventoData == null) {
            scope.launch {
                snackbarHostState.showSnackbar("Error: No se encontraron los datos del evento")
            }
            return
        }

        isCreatingEvent = true

        val eventoId = UUID.randomUUID().toString()

        // Crear el evento completo con los datos guardados
        val nuevoEvento = Evento(
            id = eventoId,
            nombre = eventoData["nombre"] as? String ?: "",
            tipo = eventoData["tipo"] as? String ?: "",
            fecha = eventoData["fecha"] as? String ?: "",
            horaReunion = eventoData["horaReunion"] as? String ?: "",
            horaInicio = eventoData["horaInicio"] as? String ?: "",
            horaFin = eventoData["horaFin"] as? String ?: "",
            direccion = eventoData["direccion"] as? String ?: "",
            descripcion = eventoData["descripcion"] as? String ?: "",
            participantes = jugadoresSeleccionados.toList(),
            asistentes = emptyList(),
            creadorId = eventoData["creadorId"] as? String ?: "",
            timestamp = eventoData["timestamp"] as? Long ?: System.currentTimeMillis(),
            activo = true
        )

        try {
            // Guardar en almacenamiento local
            storageManager.guardarEvento(nuevoEvento)

            // Limpiar los datos temporales
            EventoDataHolder.eventoTemporal = null

            // Navegar de vuelta a la pantalla de eventos
            navController.navigate("eventos") {
                popUpTo("eventos") { inclusive = true }
            }
        } catch (e: Exception) {
            isCreatingEvent = false
            scope.launch {
                snackbarHostState.showSnackbar("Error al crear el evento: ${e.message}")
            }
        }
    }

    // 🔥 USAR BOX EN LUGAR DE SCAFFOLD
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Grey, MidnightBlue),
                    startY = 0f,
                    endY = 600f
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // 🔥 TOPAPPBAR PERSONALIZADO
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .statusBarsPadding(), // Para el espacio de la barra de estado
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.navigateUp() }
                ) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint = Secondary
                    )
                }
                Text(
                    text = "EVENTOS",
                    color = Secondary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            // 🔥 RESTO DEL CONTENIDO
            if (isCreatingEvent) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Secondary)
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Serán como checkbox, para dar las opciones de seleccionarlo o no.",
                        color = Color.Gray,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    Text(
                        text = "El único con selección múltiple será participantes",
                        color = Color.Gray,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Sección de entrenador
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White.copy(alpha = 0.1f)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "ENTRENADOR",
                                color = Secondary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Checkbox(
                                        checked = seleccionarTodos,
                                        onCheckedChange = {
                                            seleccionarTodos = it
                                            if (it) {
                                                jugadoresSeleccionados = jugadores.map { j -> j.id }.toSet()
                                            } else {
                                                jugadoresSeleccionados = emptySet()
                                            }
                                        },
                                        colors = CheckboxDefaults.colors(
                                            checkedColor = Secondary,
                                            uncheckedColor = Color.Gray
                                        )
                                    )
                                    Text(
                                        text = "ENTRENADOR SELECCIONA A:",
                                        color = Color.White,
                                        fontSize = 12.sp
                                    )
                                }
                                if (seleccionarTodos) {
                                    Card(
                                        colors = CardDefaults.cardColors(
                                            containerColor = Secondary
                                        ),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = "Todos los jugadores deben confirmar",
                                            color = Color.White,
                                            fontSize = 10.sp,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                            if (!seleccionarTodos) {
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color.Gray.copy(alpha = 0.5f)
                                    ),
                                    shape = RoundedCornerShape(4.dp),
                                    modifier = Modifier.padding(top = 8.dp)
                                ) {
                                    Text(
                                        text = "Los jugadores no deben confirmar",
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Opciones de entrenador
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = enviarAlarma,
                            onCheckedChange = { enviarAlarma = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = Secondary,
                                uncheckedColor = Color.Gray
                            )
                        )
                        Text(
                            text = "ENTRENADOR SELECCIONA SI: Manda mensaje de grupo o alarma",
                            color = Color.White,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    // Lista de jugadores
                    Text(
                        text = "JUGADORES",
                        color = Secondary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    if (isLoading) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Secondary)
                        }
                    } else if (jugadores.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No hay jugadores disponibles",
                                color = Color.Gray
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(jugadores) { jugador ->
                                JugadorItem(
                                    jugador = jugador,
                                    isSelected = jugadoresSeleccionados.contains(jugador.id),
                                    onSelectionChange = { isSelected ->
                                        if (isSelected) {
                                            jugadoresSeleccionados = jugadoresSeleccionados + jugador.id
                                        } else {
                                            jugadoresSeleccionados = jugadoresSeleccionados - jugador.id
                                            seleccionarTodos = false
                                        }
                                    }
                                )
                            }
                        }
                    }

                    // Botón de crear evento
                    Button(
                        onClick = { crearEvento() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .padding(vertical = 8.dp),
                        enabled = jugadoresSeleccionados.isNotEmpty(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Secondary,
                            disabledContainerColor = Color.Gray
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Crear evento",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // SnackbarHost en la parte inferior
        Box(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            SnackbarHost(snackbarHostState)
        }
    }
}

@Composable
fun JugadorItem(
    jugador: Jugador,
    isSelected: Boolean,
    onSelectionChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelectionChange(!isSelected) },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Secondary.copy(alpha = 0.2f) else Color.White.copy(alpha = 0.1f)
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if (isSelected) Secondary else Color.Gray
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = onSelectionChange,
                    colors = CheckboxDefaults.colors(
                        checkedColor = Secondary,
                        uncheckedColor = Color.Gray
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${jugador.nombre} ${jugador.apellido}",
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }

            Text(
                text = "#${jugador.numero}",
                color = if (isSelected) Secondary else Color.Gray,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSeleccionarParticipantesPantalla() {
    val navController = rememberNavController()
    SeleccionarParticipantesPantalla(navController)
}