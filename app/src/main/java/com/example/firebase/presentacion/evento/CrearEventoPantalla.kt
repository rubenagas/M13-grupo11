package com.example.firebase.presentacion.evento

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.util.UUID

// Objeto singleton para compartir datos entre pantallas
//object EventoDataHolder {
//    var eventoTemporal: Map<String, Any>? = null
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearEventoPantalla(navController: NavHostController) {
    var currentStep by remember { mutableStateOf(1) }
    val snackbarHostState = remember { SnackbarHostState() }

    // Estados para el formulario
    var nombreEvento by remember { mutableStateOf("") }
    var tipoEvento by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var horaReunion by remember { mutableStateOf("") }
    var horaInicio by remember { mutableStateOf("") }
    var horaFin by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    // Aplicar el gradiente de fondo a toda la pantalla
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
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            containerColor = Color.Transparent, // Hacer el Scaffold transparente
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "CREAR EVENTO",
                            color = Secondary,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver",
                                tint = Secondary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = Secondary,
                        navigationIconContentColor = Secondary
                    )
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (currentStep) {
                    1 -> PrimerPaso(
                        nombreEvento = nombreEvento,
                        onNombreChange = { nombreEvento = it },
                        tipoEvento = tipoEvento,
                        onTipoChange = { tipoEvento = it },
                        onContinuar = { currentStep = 2 }
                    )
                    2 -> SegundoPaso(
                        fecha = fecha,
                        onFechaChange = { fecha = it },
                        horaReunion = horaReunion,
                        onHoraReunionChange = { horaReunion = it },
                        horaInicio = horaInicio,
                        onHoraInicioChange = { horaInicio = it },
                        horaFin = horaFin,
                        onHoraFinChange = { horaFin = it },
                        direccion = direccion,
                        onDireccionChange = { direccion = it },
                        descripcion = descripcion,
                        onDescripcionChange = { descripcion = it },
                        onContinuar = {
                            // Guardar los datos del evento en el objeto singleton
                            EventoDataHolder.eventoTemporal = hashMapOf(
                                "nombre" to nombreEvento,
                                "tipo" to tipoEvento,
                                "fecha" to fecha,
                                "horaReunion" to horaReunion,
                                "horaInicio" to horaInicio,
                                "horaFin" to horaFin,
                                "direccion" to direccion,
                                "descripcion" to descripcion,
                                "creadorId" to (FirebaseAuth.getInstance().currentUser?.uid ?: ""),
                                "timestamp" to System.currentTimeMillis()
                            )
                            // Navegar a la pantalla de selección de participantes
                            navController.navigate("seleccionar_participantes")
                        },
                        onVolver = { currentStep = 1 }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrimerPaso(
    nombreEvento: String,
    onNombreChange: (String) -> Unit,
    tipoEvento: String,
    onTipoChange: (String) -> Unit,
    onContinuar: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Información básica del evento",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Campo de nombre del evento
        OutlinedTextField(
            value = nombreEvento,
            onValueChange = onNombreChange,
            label = { Text("Nombre del evento", color = Color.Gray) },
            placeholder = { Text("Ej: Entrenamiento táctico", color = Color.Gray.copy(alpha = 0.5f)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Secondary,
                unfocusedBorderColor = Color.Gray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp),
            singleLine = true
        )

        // Dropdown para tipo de evento
        var expanded by remember { mutableStateOf(false) }
        val tiposEvento = listOf("Entrenamiento", "Partido", "Reunión", "Otro")

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = tipoEvento,
                onValueChange = { },
                readOnly = true,
                label = { Text("Tipo de evento", color = Color.Gray) },
                placeholder = { Text("Selecciona un tipo", color = Color.Gray.copy(alpha = 0.5f)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
                    .padding(vertical = 8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Secondary,
                    unfocusedBorderColor = Color.Gray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp)
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                tiposEvento.forEach { tipo ->
                    DropdownMenuItem(
                        text = { Text(tipo) },
                        onClick = {
                            onTipoChange(tipo)
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Botón de continuar
        Button(
            onClick = onContinuar,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = nombreEvento.isNotBlank() && tipoEvento.isNotBlank(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Secondary,
                disabledContainerColor = Color.Gray
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "Continuar",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun SegundoPaso(
    fecha: String,
    onFechaChange: (String) -> Unit,
    horaReunion: String,
    onHoraReunionChange: (String) -> Unit,
    horaInicio: String,
    onHoraInicioChange: (String) -> Unit,
    horaFin: String,
    onHoraFinChange: (String) -> Unit,
    direccion: String,
    onDireccionChange: (String) -> Unit,
    descripcion: String,
    onDescripcionChange: (String) -> Unit,
    onContinuar: () -> Unit,
    onVolver: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Detalles del evento",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Campos del formulario
        OutlinedTextField(
            value = fecha,
            onValueChange = onFechaChange,
            label = { Text("Fecha", color = Color.Gray) },
            placeholder = { Text("DD/MM/AAAA", color = Color.Gray.copy(alpha = 0.5f)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Secondary,
                unfocusedBorderColor = Color.Gray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp),
            singleLine = true
        )

        OutlinedTextField(
            value = horaReunion,
            onValueChange = onHoraReunionChange,
            label = { Text("Hora de reunión", color = Color.Gray) },
            placeholder = { Text("HH:MM", color = Color.Gray.copy(alpha = 0.5f)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Secondary,
                unfocusedBorderColor = Color.Gray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp),
            singleLine = true
        )

        OutlinedTextField(
            value = horaInicio,
            onValueChange = onHoraInicioChange,
            label = { Text("Hora de inicio", color = Color.Gray) },
            placeholder = { Text("HH:MM", color = Color.Gray.copy(alpha = 0.5f)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Secondary,
                unfocusedBorderColor = Color.Gray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp),
            singleLine = true
        )

        OutlinedTextField(
            value = horaFin,
            onValueChange = onHoraFinChange,
            label = { Text("Hora de finalización", color = Color.Gray) },
            placeholder = { Text("HH:MM", color = Color.Gray.copy(alpha = 0.5f)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Secondary,
                unfocusedBorderColor = Color.Gray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp),
            singleLine = true
        )

        OutlinedTextField(
            value = direccion,
            onValueChange = onDireccionChange,
            label = { Text("Dirección", color = Color.Gray) },
            placeholder = { Text("Lugar del evento", color = Color.Gray.copy(alpha = 0.5f)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Secondary,
                unfocusedBorderColor = Color.Gray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp),
            singleLine = true
        )

        OutlinedTextField(
            value = descripcion,
            onValueChange = onDescripcionChange,
            label = { Text("Descripción", color = Color.Gray) },
            placeholder = { Text("Detalles adicionales...", color = Color.Gray.copy(alpha = 0.5f)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            minLines = 3,
            maxLines = 5,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Secondary,
                unfocusedBorderColor = Color.Gray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Botones
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onVolver,
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Volver",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Button(
                onClick = onContinuar,
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                enabled = fecha.isNotBlank() && horaReunion.isNotBlank() &&
                        horaInicio.isNotBlank() && horaFin.isNotBlank() &&
                        direccion.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Secondary,
                    disabledContainerColor = Color.Gray
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Continuar",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCrearEventoPantalla() {
    val navController = rememberNavController()
    CrearEventoPantalla(navController)
}