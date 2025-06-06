package com.example.firebase.presentacion.evento

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.firebase.FooterBar
import com.example.firebase.ui.theme.Grey
import com.example.firebase.ui.theme.MidnightBlue
import com.example.firebase.ui.theme.Secondary

// Modelo de datos para los eventos
data class Evento(
    val fecha: String,
    val tipo: String,
    val horaReunion: String,
    val horaInicio: String,
    val horaFin: String,
    val color: Color = Color.Green
)

@Composable
fun EventosPantalla(navController: NavHostController) {

    // Lista de eventos de ejemplo - puedes reemplazar esto con datos reales
    val eventos = listOf(
        Evento("15/03/2024", "Entrenamiento", "19:00", "19:30", "21:00"),
        Evento("17/03/2024", "Partido", "14:00", "15:00", "17:00", Color.Red),
        Evento("20/03/2024", "Entrenamiento", "19:00", "19:30", "21:00"),
        Evento("22/03/2024", "Reunión", "18:00", "18:30", "19:30", Color.Blue),
        Evento("24/03/2024", "Partido", "16:00", "17:00", "19:00", Color.Red),
        Evento("27/03/2024", "Entrenamiento", "19:00", "19:30", "21:00"),
        // Agrega más eventos según necesites
    )

    Scaffold(
        bottomBar = {
            FooterBar(navController = navController)
        }
    ) { innerPadding ->
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
                // Espaciado superior
                item {
                    Spacer(modifier = Modifier.height(screenHeight * 0.05f))
                }

                // Título principal
                item {
                    Text(
                        text = "EVENTOS",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Secondary
                    )
                }

                // Espaciado
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Subtítulo
                item {
                    Text(
                        text = "PRÓXIMOS EVENTOS",
                        fontSize = 14.sp,
                        color = Color.LightGray,
                        fontStyle = FontStyle.Italic
                    )
                }

                // Espaciado antes de los eventos
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Aquí es donde agregarías el botón de crear evento si el usuario tiene el rol adecuado
                // Descomenta y ajusta según tu lógica de roles
                /*
                item {
                    if (userHasRole) { // Reemplaza con tu lógica de verificación de rol
                        Button(
                            onClick = { /* Navegar a crear evento */ },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Secondary)
                        ) {
                            Text("Crear Nuevo Evento")
                        }
                    }
                }
                */

                // Lista de eventos
                items(eventos) { evento ->
                    EventoCard(evento = evento)
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Espaciado antes del botón de resumen
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }

                // Botón de resumen
                item {
                    Button(
                        onClick = { /* Navegar a resumen de eventos */ },
                        modifier = Modifier.fillMaxWidth(0.9f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
                    ) {
                        Text("Resumen de eventos")
                    }
                }

                // Espaciado inferior
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun EventoCard(evento: Evento) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color.Blue)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Fecha con ancho fijo para mantener alineación
                Text(
                    text = evento.fecha,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                // Tipo de evento alineado a la derecha
                Text(
                    text = evento.tipo,
                    color = evento.color,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                EventoItem(evento.horaReunion, "Reunirse")
                EventoItem(evento.horaInicio, "Comenzar")
                EventoItem(evento.horaFin, "Terminar")
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun EventoItem(hora: String, accion: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(hora, color = Color.Blue, fontWeight = FontWeight.Bold)
        Text(accion, color = Color.Blue)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewEventosPantalla() {
    val navController = rememberNavController()
    EventosPantalla(navController)
}