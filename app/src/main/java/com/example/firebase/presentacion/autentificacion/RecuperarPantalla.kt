package com.example.firebase.presentacion.autentificacion


import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.firebase.ui.theme.DarkIndigo
import com.example.firebase.ui.theme.Grey
import com.example.firebase.ui.theme.MidnightBlue
import com.example.firebase.ui.theme.Surface
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

// Pantalla para recuperar contraseña.
@Composable
fun Recuperar(navController: NavHostController, isPreview: Boolean = false) {
    // Layout con fondo de gradiente para mejor diseño visual
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Grey, MidnightBlue)))
    ) {
        val screenHeight = maxHeight
        val scrollState = rememberScrollState()
        var email by remember { mutableStateOf("") }

        // En Preview la app no corre de verdad, así que Firebase no puede inicializarse correctamente (no hay emulador, contexto ni usuario), por eso evitamos usar LocalContext aquí
        val context = if (!isPreview) LocalContext.current else null

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // Título + botón atrás
            Recuperacion(navController)

            Spacer(modifier = Modifier.height(screenHeight * 0.05f))

            // Campo para introducir el correo electrónico
            CampEmail(email) { email = it }

            Spacer(modifier = Modifier.height(screenHeight * 0.05f))

            Spacer(modifier = Modifier.height(7.dp))

            // Botón para enviar el correo de recuperación
            BttnEnviar(
                email = email,
                isPreview = isPreview,
                context = context,
                onSuccess = {
                    Toast.makeText(context, "Correo de recuperación enviado", Toast.LENGTH_LONG).show()
                },
                onError = { error ->
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                }
            )
        }
    }
}

// Título + ícono de volver atrás
@Composable
fun Recuperacion(navController: NavHostController) {
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
            text = "RECUPERAR TU CONTRASEÑA",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Yellow
        )
    }
}

// Campo de texto para capturar el email del usuario
@Composable
fun CampEmail(email: String, onCambio: (String) -> Unit) {
    Text(text = "Correo electrónico", color = Color.White)
    Spacer(modifier = Modifier.height(4.dp))
    TextField(
        value = email,
        onValueChange = onCambio,
        placeholder = { Text("Introduce tu correo") },
        modifier = Modifier.fillMaxWidth()
    )
}

// Botón que envía correo de recuperación usando Firebase Auth
@Composable
fun BttnEnviar(
    email: String,
    isPreview: Boolean,
    context: android.content.Context?,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    Button(
        onClick = {
            // Solo enviamos si no estamos en preview, el email no está vacío, y el contexto es válido
            if (!isPreview && !email.isNullOrBlank() && context != null) {
                // Enviamos correo de recuperación con Firebase Auth
                Firebase.auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            onSuccess()
                        } else {
                            onError("Error al enviar el correo. Verifica el correo electrónico.")
                        }
                    }
            } else {
                onError("El correo no puede estar vacío.")
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        colors = ButtonDefaults.buttonColors(containerColor = DarkIndigo)
    ) {
        Text(text = "Enviar", color = Surface)
    }
}

// Vista previa de la pantalla para diseño sin necesidad de emulador
@PreviewScreenSizes
@Preview(showBackground = true)
@Composable
fun PreviewRecuperarPantalla() {
    val navController = rememberNavController()
    Recuperar(navController = navController, isPreview = true)
}