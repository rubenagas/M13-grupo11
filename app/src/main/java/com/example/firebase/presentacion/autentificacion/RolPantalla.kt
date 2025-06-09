package com.example.firebase.presentacion.autentificacion

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
    var anioNacimiento by remember { mutableStateOf<String?>(null) }
    var rolSeleccionado by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(screenHeight * 0.05f))

            Text(
                text = "Completa tu perfil",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Yellow,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(16.dp))

            CampoTextoNombre(nombre) { nombre = it }

            Spacer(modifier = Modifier.height(16.dp))

            DropdownAnoNacimiento(anioNacimiento) { anioNacimiento = it }

            Spacer(modifier = Modifier.height(16.dp))

            SeleccionRol(rolSeleccionado) { rolSeleccionado = it }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val user = AuthService.usuarioActual()
                    if (user == null) {
                        errorMessage = "No se encontró el usuario autenticado"
                        return@Button
                    }

                    if (nombre.isBlank() || anioNacimiento.isNullOrBlank() || rolSeleccionado.isNullOrBlank()) {
                        errorMessage = "Por favor completa todos los campos"
                        return@Button
                    }

                    val usuario = Usuario(
                        id = user.uid,
                        nombre = nombre,
                        correo = user.email ?: "",
                        rol = rolSeleccionado!!,
                        anioNacimiento = anioNacimiento!!
                    )

                    FirestoreService.guardarUsuario(
                        usuario,
                        onSuccess = {
                            Toast.makeText(context, "Perfil guardado con éxito", Toast.LENGTH_SHORT).show()
                            navController.navigate("perfil")
                        },
                        onError = {
                            errorMessage = "Error al guardar el perfil: ${it.message}"
                        }
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = DarkIndigo)
            ) {
                Text("Confirmar", color = Surface)
            }

            Spacer(modifier = Modifier.height(8.dp))

            errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Composable
fun CampoTextoNombre(nombre: String, onCambio: (String) -> Unit) {
    Text(text = "Nombre", color = Color.White)
    Spacer(modifier = Modifier.height(4.dp))
    TextField(
        value = nombre,
        onValueChange = onCambio,
        placeholder = { Text("Introduce tu nombre") },
        modifier = Modifier.fillMaxWidth()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownAnoNacimiento(anioSeleccionado: String?, onSeleccion: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val anios = (2025 downTo 1920).map { it.toString() }

    Text(text = "Año de nacimiento", color = Color.White)
    Spacer(modifier = Modifier.height(4.dp))

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = anioSeleccionado ?: "",
            onValueChange = {},
            readOnly = true,
            placeholder = { Text("Selecciona el año") },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            anios.forEach { anio ->
                DropdownMenuItem(
                    text = { Text(anio) },
                    onClick = {
                        onSeleccion(anio)
                        expanded = false
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
    val roles = listOf("Jugador", "Entrenador/Máster", "Tesorero")
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

@PreviewFontScale
@PreviewScreenSizes
@Preview(showBackground = true)
@Composable
fun RolPantallaPreview() {
    val navController = rememberNavController()
    RolPantalla(navController = navController, isPreview = true)
}
