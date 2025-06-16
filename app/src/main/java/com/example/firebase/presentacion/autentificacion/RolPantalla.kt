package com.example.firebase.presentacion.autentificacion

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.firebase.ui.theme.DarkIndigo
import com.example.firebase.ui.theme.Grey
import com.example.firebase.ui.theme.MidnightBlue
import com.example.firebase.ui.theme.Surface
import com.example.firebase.data.AuthService
import com.example.firebase.data.firebase.FirestoreService
import com.example.firebase.data.modelo.Usuario

@Composable
fun RolPantalla(navController: NavHostController, isPreview: Boolean = false) {
    val context = if (!isPreview) LocalContext.current else null

    var nombre by remember { mutableStateOf("") }
    var anNacimiento by remember { mutableStateOf<String?>(null) }
    var rolSeleccionado by remember { mutableStateOf<String?>(null) }
    var mensajeError by remember { mutableStateOf<String?>(null) }

    BoxWithConstraints(
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
        val screenHeight = maxHeight
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // Título + botón atrás
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint = Yellow
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Completa tu perfil",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Yellow
                )
            }

            Spacer(modifier = Modifier.height(screenHeight * 0.05f))

            TextoNombre(nombre) { nombre = it }

            Spacer(modifier = Modifier.height(16.dp))

            Nacimiento(anNacimiento) { anNacimiento = it }

            Spacer(modifier = Modifier.height(16.dp))

            SeleccionRol(rolSeleccionado) { rolSeleccionado = it }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val user = AuthService.usuarioActual()
                    if (user == null) {
                        mensajeError = "No se ha encontrado a el usuario autenticado"
                        return@Button
                    }

                    if (nombre.isBlank() || anNacimiento.isNullOrBlank() || rolSeleccionado.isNullOrBlank()) {
                        mensajeError = "Por favor, complete todos los campos"
                        return@Button
                    }

                    val usuario = Usuario(
                        id = user.uid,
                        nombre = nombre,
                        correo = user.email ?: "",
                        rol = rolSeleccionado!!,
                        anioNacimiento = anNacimiento!!
                    )

                    FirestoreService.guardarUsuario(
                        usuario,
                        onSuccess = {
                            Toast.makeText(context, "El perfil se ha guardado con éxito", Toast.LENGTH_SHORT).show()
                            navController.navigate("perfil")
                        },
                        onError = {
                            mensajeError = "Error al guardar el perfil: ${it.message}"
                        }
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = DarkIndigo)
            ) {
                Text("Confirmar", color = Surface)
            }

            Spacer(modifier = Modifier.height(8.dp))

            mensajeError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun TextoNombre(nombre: String, onCambio: (String) -> Unit) {
    Text(text = "Nombre", color = Color.White)
    Spacer(modifier = Modifier.height(4.dp))
    TextField(
        value = nombre,
        onValueChange = onCambio,
        placeholder = { Text("Introduce tu nombre") },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun Nacimiento(anioSelc: String?, onSeleccion: (String) -> Unit) {
    var xpan by remember { mutableStateOf(false) }
    val anios = (2025 downTo 1940).map { it.toString() }
    val selected = anioSelc ?: "Seleccione el año"

    Text(text = "Año de nacimiento", color = Color.White)
    Spacer(modifier = Modifier.height(4.dp))

    Box {
        OutlinedButton(
            onClick = { xpan = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(selected, color = Color.White)
        }

        DropdownMenu(
            expanded = xpan,
            onDismissRequest = { xpan = false }
        ) {
            anios.forEach { anio ->
                DropdownMenuItem(
                    text = { Text(anio) },
                    onClick = {
                        onSeleccion(anio)
                        xpan = false
                    }
                )
            }
        }
    }
}

@Composable
fun SeleccionRol(rolSeleccionado: String?, onRolSeleccionado: (String) -> Unit) {
    Text(text = "Rol", color = Color.White)
    Spacer(modifier = Modifier.height(4.dp))
    val roles = listOf("Jugador", "Entrenador", "Tesorero")
    roles.forEach { rol ->
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            RadioButton(
                selected = rolSeleccionado == rol,
                onClick = { onRolSeleccionado(rol) }
            )
            Text(
                text = rol,
                color = Color.White,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

//@PreviewFontScale
@PreviewScreenSizes
@Preview(showBackground = true)
@Composable
fun RolPantallaPreview() {
    val navController = rememberNavController()
    RolPantalla(navController = navController, isPreview = true)
}
