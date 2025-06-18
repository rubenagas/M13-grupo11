package com.example.firebase.presentacion.autentificacion

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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.firebase.data.AuthService
import com.example.firebase.ui.theme.DarkIndigo
import com.example.firebase.ui.theme.Grey
import com.example.firebase.ui.theme.MidnightBlue
import com.example.firebase.ui.theme.Surface

// Pantalla principal de login con correo y contraseña
@Composable
fun LoginPantalla(navController: NavHostController, isPreview: Boolean = false) {
    // En Preview la app no corre de verdad, así que Firebase no puede inicializarse correctamente (no hay emulador, contexto ni usuario), por eso evitamos usar LocalContext aquí
    val context = if (!isPreview) LocalContext.current else null

    // Estados para formulario de login
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Fondo con gradiente visual y layout adaptado a altura de pantalla
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

        // Contenido de la pantalla
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // Título + botón atrás
            TituloLogin("INICIA SESIÓN", navController)

            Spacer(modifier = Modifier.height(screenHeight * 0.05f))

            // Campo de correo electrónico
            Email(email) { email = it }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de contraseña
            Contrasena(password) { password = it }

            Spacer(modifier = Modifier.height(screenHeight * 0.08f))

            // Botón para iniciar sesión con validaciones
            Entrar(
                email = email,
                password = password,
                isPreview = isPreview,
                context = context,
                navController = navController,
                onError = { mensaje -> errorMessage = mensaje }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Mostrar mensaje de error si existe
            errorMessage?.let { MensajeError(it) }

            Spacer(modifier = Modifier.height(screenHeight * 0.05f))

            // Enlace a pantalla de recuperación de contraseña
            ContrasenaOlvidada {
                if (!isPreview) navController.navigate("recuperar")
            }

            Spacer(modifier = Modifier.height(24.dp)) // extra padding final
        }
    }
}

// Botón con ícono para volver atrás
@Composable
fun BotonAtrasL(navController: NavHostController) {
    IconButton(onClick = { navController.popBackStack() }) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "atras",
            tint = Yellow
        )
    }
}

// Título principal + botón atrás
@Composable
fun TituloLogin(texto: String, navController: NavHostController) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        BotonAtrasL(navController)
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = texto,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Yellow,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.Start)
        )
    }
}

// Campo para introducir el correo
@Composable
fun Email(email: String, onCambio: (String) -> Unit) {
    Text(text = "Correo electrónico", color = Color.White)
    Spacer(modifier = Modifier.height(4.dp))
    TextField(
        value = email,
        onValueChange = onCambio,
        placeholder = { Text("Introduce tu correo") },
        modifier = Modifier.fillMaxWidth()
    )
}

// Campo para introducir la contraseña con ocultamiento de texto
@Composable
fun Contrasena(password: String, onCambio: (String) -> Unit) {
    Text(text = "Contraseña", color = Color.White)
    Spacer(modifier = Modifier.height(4.dp))
    TextField(
        value = password,
        onValueChange = onCambio,
        placeholder = { Text("Introduce tu contraseña") },
        visualTransformation = PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth()
    )
}

// Botón que ejecuta el login con Firebase (basado en AuthService y Firebase Auth Docs)
@Composable
fun Entrar(
    email: String,
    password: String,
    isPreview: Boolean,
    context: android.content.Context?,
    navController: NavHostController,
    onError: (String) -> Unit
) {
    Button(
        onClick = {
            // Validaciones básicas
            if (email.isBlank() || password.isBlank()) {
                onError("Completa todos los campos")
                return@Button
            }

            // Login con Firebase solo si no estamos en preview
            if (!isPreview && context != null) {
                AuthService.loginCorreo(
                    email = email,
                    contraseña = password,
                    context = context,
                    navController = navController,
                    onError = { exception ->
                        onError(exception.message ?: "Error al iniciar sesión")
                    }
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        colors = ButtonDefaults.buttonColors(containerColor = DarkIndigo)
    ) {
        Text(text = "Entrar", color = Surface)
    }
}

// Muestra el mensaje de error si existe
@Composable
fun MensajeError(mensaje: String) {
    Text(
        text = mensaje,
        color = MaterialTheme.colorScheme.error,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally)
    )
}

// Enlace a la pantalla de recuperación de contraseña
@Composable
fun ContrasenaOlvidada(onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        Text(
            "¿Has olvidado tu contraseña?",
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally),
            color = Color.White
        )
    }
}

// Vista previa del diseño de pantalla de login
@PreviewScreenSizes
@Preview(showBackground = true)
@Composable
fun PreviewLoginPantalla() {
    val navController = rememberNavController()
    LoginPantalla(navController = navController, isPreview = true)
}
