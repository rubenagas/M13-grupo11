package com.example.firebase.presentacion.evento

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
// import androidx.compose.foundation.clickable // Ya no se necesita aquí para EventoResumenCard
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
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

// Modelo de datos para el resumen del evento
data class EventoResumen(
    val id: String,
    val fecha: String,
    val tipo: String,
    val participantesConfirmados: Int,
    val participantesTotales: Int,
    val estadoUsuario: EstadoParticipacion
)

enum class EstadoParticipacion {
    ASISTIO,
    NO_ASISTIO,
    NO_CONVOCADO
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResumenEventosPantalla(navController: NavHostController) {
    var mesSeleccionado by remember { mutableStateOf(3) } // Marzo por defecto
    var añoSeleccionado by remember { mutableStateOf(2024) }

    val eventosPasados = remember {
        listOf(
            EventoResumen("1", "05/03/2024", "Entrenamiento", 18, 20, EstadoParticipacion.ASISTIO),
            EventoResumen("2", "07/03/2024", "Partido", 22, 22, EstadoParticipacion.ASISTIO),
            EventoResumen("3", "10/03/2024", "Entrenamiento", 15, 20, EstadoParticipacion.NO_ASISTIO),
            EventoResumen("4", "12/03/2024", "Reunión", 8, 10, EstadoParticipacion.NO_CONVOCADO),
            EventoResumen("5", "14/03/2024", "Partido", 22, 22, EstadoParticipacion.ASISTIO),
            EventoResumen("6", "01/02/2024", "Entrenamiento", 16, 20, EstadoParticipacion.ASISTIO),
            EventoResumen("7", "03/02/2024", "Partido", 20, 22, EstadoParticipacion.ASISTIO)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "RESUMEN DE EVENTOS",
                        color = Secondary,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = Secondary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Grey,
                    titleContentColor = Secondary,
                    navigationIconContentColor = Secondary
                )
            )
        }
    ) { innerPadding ->
        Box(
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White.copy(alpha = 0.1f)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Mes",
                                color = Color.Gray,
                                fontSize = 12.sp
                            )
                            Text(
                                text = obtenerNombreMes(mesSeleccionado),
                                color = Secondary,
                                fontWeight = FontWeight.Bold,
                                // Quitado el modifier.clickable aquí también, si antes era para otra cosa
                                // y no para seleccionar el mes con un diálogo, puedes mantenerlo.
                                // Si era para un diálogo de selección de mes, esa lógica se mantiene.
                                // Si era solo para la navegación del evento, ya no es necesario.
                                // modifier = Modifier.clickable { /* Lógica para diálogo de mes si es necesario */ }
                            )
                        }
                    }

                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White.copy(alpha = 0.1f)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Año",
                                color = Color.Gray,
                                fontSize = 12.sp
                            )
                            Text(
                                text = añoSeleccionado.toString(),
                                color = Secondary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Text(
                    text = "Solo se verán los eventos futuros, de querer ver eventos pasados, deberá de buscar en un filtro de día, mes y año.",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "No podrá entrar a ese evento si no participó.",
                    color = Color.Red.copy(alpha = 0.8f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        eventosPasados.filter { evento ->
                            val mes = evento.fecha.split("/")[1].toIntOrNull() ?: 0
                            val año = evento.fecha.split("/")[2].toIntOrNull() ?: 0
                            mes == mesSeleccionado && año == añoSeleccionado
                        }
                    ) { evento ->
                        // Ya no se pasa el parámetro onClick aquí
                        EventoResumenCard(evento = evento)
                    }
                }
            }
        }
    }
}

@Composable
fun EventoResumenCard(
    evento: EventoResumen
    // Ya no se recibe el parámetro onClick: () -> Unit
) {
    val backgroundColor = when (evento.estadoUsuario) {
        EstadoParticipacion.ASISTIO -> Color.Green.copy(alpha = 0.1f)
        EstadoParticipacion.NO_ASISTIO -> Color.Red.copy(alpha = 0.1f)
        EstadoParticipacion.NO_CONVOCADO -> Color.Gray.copy(alpha = 0.1f)
    }

    val borderColor = when (evento.estadoUsuario) {
        EstadoParticipacion.ASISTIO -> Color.Green
        EstadoParticipacion.NO_ASISTIO -> Color.Red
        EstadoParticipacion.NO_CONVOCADO -> Color.Gray
    }

    Card(
        modifier = Modifier.fillMaxWidth(), // Se quita .clickable(...)
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        border = BorderStroke(1.dp, borderColor),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = evento.fecha,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = evento.tipo,
                    color = borderColor,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Asistencia: ${evento.participantesConfirmados}/${evento.participantesTotales}",
                    color = Color.Gray,
                    fontSize = 14.sp
                )

                Text(
                    text = when (evento.estadoUsuario) {
                        EstadoParticipacion.ASISTIO -> "Asististe"
                        EstadoParticipacion.NO_ASISTIO -> "No asististe"
                        EstadoParticipacion.NO_CONVOCADO -> "No convocado"
                    },
                    color = borderColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

fun obtenerNombreMes(mes: Int): String {
    return when (mes) {
        1 -> "Enero"
        2 -> "Febrero"
        3 -> "Marzo"
        4 -> "Abril"
        5 -> "Mayo"
        6 -> "Junio"
        7 -> "Julio"
        8 -> "Agosto"
        9 -> "Septiembre"
        10 -> "Octubre"
        11 -> "Noviembre"
        12 -> "Diciembre"
        else -> "Mes"
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewResumenEventosPantalla() {
    val navController = rememberNavController()
    ResumenEventosPantalla(navController)
}