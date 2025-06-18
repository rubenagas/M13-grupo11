package com.example.firebase.presentacion.configuracion

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.example.firebase.ui.theme.Grey
import com.example.firebase.ui.theme.MidnightBlue
import com.example.firebase.ui.theme.Secondary
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfiguracionPantalla(navController: NavHostController) {
    val context = LocalContext.current
    val storageManager = remember { LocalStorageManager(context) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var showImportDialog by remember { mutableStateOf(false) }
    var importData by remember { mutableStateOf("") }

    // Launcher para guardar archivo
    val saveFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        uri?.let {
            try {
                val jsonData = storageManager.exportarDatos()
                context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                    outputStream.write(jsonData.toByteArray())
                }
                scope.launch {
                    snackbarHostState.showSnackbar("Datos exportados exitosamente")
                }
            } catch (e: Exception) {
                scope.launch {
                    snackbarHostState.showSnackbar("Error al exportar: ${e.message}")
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "CONFIGURACIÓN",
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
                    containerColor = Color.Transparent
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
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Sección de gestión de datos
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Gestión de Datos",
                            color = Secondary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )

                        // Gestionar jugadores
                        Button(
                            onClick = { navController.navigate("gestionar_jugadores") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Secondary
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Gestionar Jugadores")
                        }

                        // Exportar datos
                        Button(
                            onClick = {
                                val fecha = SimpleDateFormat("yyyy-MM-dd_HH-mm", Locale.getDefault())
                                    .format(Date())
                                saveFileLauncher.launch("backup_equipo_$fecha.json")
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Blue
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
//                            Icon(
//                                Icons.Default.Download,
//                                contentDescription = null,
//                                modifier = Modifier.size(20.dp)
//                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Exportar Datos")
                        }

                        // Importar datos
                        Button(
                            onClick = { showImportDialog = true },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Green
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
//                            Icon(
//                                Icons.Default.Upload,
//                                contentDescription = null,
//                                modifier = Modifier.size(20.dp)
//                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Importar Datos")
                        }

                        // Copiar datos al portapapeles
                        Button(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Datos del equipo", storageManager.exportarDatos())
                                clipboard.setPrimaryClip(clip)
                                scope.launch {
                                    snackbarHostState.showSnackbar("Datos copiados al portapapeles")
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Gray
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
//                            Icon(
//                                Icons.Default.ContentCopy,
//                                contentDescription = null,
//                                modifier = Modifier.size(20.dp)
//                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Copiar Datos")
                        }
                    }
                }

                // Sección de peligro
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Red.copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Zona de Peligro",
                            color = Color.Red,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )

                        Button(
                            onClick = {
                                storageManager.limpiarTodo()
                                storageManager.inicializarJugadoresPorDefecto()
                                scope.launch {
                                    snackbarHostState.showSnackbar("Todos los datos han sido eliminados")
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Red
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Eliminar Todos los Datos")
                        }
                    }
                }
            }
        }
    }

    // Diálogo para importar datos
    if (showImportDialog) {
        AlertDialog(
            onDismissRequest = { showImportDialog = false },
            title = {
                Text(
                    text = "Importar Datos",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = "Pega aquí los datos JSON:",
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    OutlinedTextField(
                        value = importData,
                        onValueChange = { importData = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        placeholder = { Text("Pega el JSON aquí...") }
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (storageManager.importarDatos(importData)) {
                            scope.launch {
                                snackbarHostState.showSnackbar("Datos importados exitosamente")
                            }
                            showImportDialog = false
                            importData = ""
                        } else {
                            scope.launch {
                                snackbarHostState.showSnackbar("Error al importar datos")
                            }
                        }
                    }
                ) {
                    Text("Importar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showImportDialog = false
                        importData = ""
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}