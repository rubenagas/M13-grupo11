package com.example.firebase.presentacion.jugadores

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.firebase.data.local.LocalStorageManager
import com.example.firebase.presentacion.evento.Jugador
import com.example.firebase.ui.theme.Grey
import com.example.firebase.ui.theme.MidnightBlue
import com.example.firebase.ui.theme.Secondary
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GestionarJugadoresPantalla(navController: NavHostController) {
    var jugadores by remember { mutableStateOf<List<Jugador>>(emptyList()) }
    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var jugadorToEdit by remember { mutableStateOf<Jugador?>(null) }

    val context = LocalContext.current
    val storageManager = remember { LocalStorageManager(context) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Cargar jugadores
    LaunchedEffect(Unit) {
        jugadores = storageManager.obtenerJugadores()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "GESTIONAR JUGADORES",
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
                actions = {
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Agregar jugador",
                            tint = Secondary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = Secondary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar jugador")
            }
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(jugadores) { jugador ->
                    JugadorCard(
                        jugador = jugador,
                        onEdit = {
                            jugadorToEdit = jugador
                            showEditDialog = true
                        },
                        onDelete = {
                            storageManager.eliminarJugador(jugador.id)
                            jugadores = storageManager.obtenerJugadores()
                            scope.launch {
                                snackbarHostState.showSnackbar("Jugador eliminado")
                            }
                        }
                    )
                }
            }
        }
    }

    // Diálogo para agregar jugador
    if (showAddDialog) {
        JugadorDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { nombre, apellido, numero, email ->
                val nuevoJugador = Jugador(
                    id = UUID.randomUUID().toString(),
                    nombre = nombre,
                    apellido = apellido,
                    numero = numero,
                    email = email
                )
                storageManager.agregarJugador(nuevoJugador)
                jugadores = storageManager.obtenerJugadores()
                showAddDialog = false
                scope.launch {
                    snackbarHostState.showSnackbar("Jugador agregado")
                }
            }
        )
    }

    // Diálogo para editar jugador
    if (showEditDialog && jugadorToEdit != null) {
        JugadorDialog(
            jugador = jugadorToEdit,
            onDismiss = {
                showEditDialog = false
                jugadorToEdit = null
            },
            onConfirm = { nombre, apellido, numero, email ->
                jugadorToEdit?.let { jugador ->
                    val jugadorActualizado = jugador.copy(
                        nombre = nombre,
                        apellido = apellido,
                        numero = numero,
                        email = email
                    )
                    storageManager.actualizarJugador(jugadorActualizado)
                    jugadores = storageManager.obtenerJugadores()
                }
                showEditDialog = false
                jugadorToEdit = null
                scope.launch {
                    snackbarHostState.showSnackbar("Jugador actualizado")
                }
            }
        )
    }
}

@Composable
fun JugadorCard(
    jugador: Jugador,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${jugador.nombre} ${jugador.apellido}",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = "Número: ${jugador.numero}",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                Text(
                    text = jugador.email,
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }

            Row {
                IconButton(onClick = onEdit) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Editar",
                        tint = Secondary
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = Color.Red
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JugadorDialog(
    jugador: Jugador? = null,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String) -> Unit
) {
    var nombre by remember { mutableStateOf(jugador?.nombre ?: "") }
    var apellido by remember { mutableStateOf(jugador?.apellido ?: "") }
    var numero by remember { mutableStateOf(jugador?.numero ?: "") }
    var email by remember { mutableStateOf(jugador?.email ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (jugador == null) "Agregar Jugador" else "Editar Jugador",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = apellido,
                    onValueChange = { apellido = it },
                    label = { Text("Apellido") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = numero,
                    onValueChange = { numero = it },
                    label = { Text("Número") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (nombre.isNotBlank() && apellido.isNotBlank() && numero.isNotBlank()) {
                        onConfirm(nombre, apellido, numero, email)
                    }
                },
                enabled = nombre.isNotBlank() && apellido.isNotBlank() && numero.isNotBlank()
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}