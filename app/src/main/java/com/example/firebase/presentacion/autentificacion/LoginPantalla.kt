package com.example.firebase.presentacion.autentificacion

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.firebase.data.AuthService
//import com.example.firebase.data.firebase.AuthService
import com.example.firebase.ui.theme.DarkIndigo
import com.example.firebase.ui.theme.Grey
import com.example.firebase.ui.theme.MidnightBlue
import com.example.firebase.ui.theme.Surface

@Composable
fun LoginPantalla(navController: NavHostController, isPreview: Boolean = false) {
    val context = if (!isPreview) LocalContext.current else null

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
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
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            TituloLogin()

            Spacer(modifier = Modifier.height(screenHeight * 0.05f))

            CampoEmail(email) { email = it }

            Spacer(modifier = Modifier.height(16.dp))

            CampoPassword(password) { password = it }

            Spacer(modifier = Modifier.height(screenHeight * 0.08f))

            BotonEntrar(
                email = email,
                password = password,
                isPreview = isPreview,
                context = context,
                navController = navController,
                onError = { errorMessage = it }
            )

            Spacer(modifier = Modifier.height(8.dp))

            errorMessage?.let {
                MensajeError(it)
            }

            Spacer(modifier = Modifier.height(screenHeight * 0.08f))

            BotonOlvidoContrasena {
                if (!isPreview) navController.navigate("registro")
            }
        }
    }
}

@Composable
fun TituloLogin() {
    Text(
        text = "INICIA SESIÓN",
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        color = Yellow,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally)
    )
}

@Composable
fun CampoEmail(email: String, onCambio: (String) -> Unit) {
    Text(text = "Correo electrónico", color = Color.White)
    Spacer(modifier = Modifier.height(4.dp))
    TextField(
        value = email,
        onValueChange = onCambio,
        placeholder = { Text("Introduce tu correo") },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun CampoPassword(password: String, onCambio: (String) -> Unit) {
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

@Composable
fun BotonEntrar(
    email: String,
    password: String,
    isPreview: Boolean,
    context: android.content.Context?,
    navController: NavHostController,
    onError: (String) -> Unit
) {
    Button(
        onClick = {
            if (email.isBlank() || password.isBlank()) {
                onError("Completa todos los campos")
                return@Button
            }

            if (!isPreview) {
                AuthService.loginConCorreo(
                    email = email,
                    password = password,
                    onSuccess = {
                        Toast.makeText(context, "Sesión iniciada", Toast.LENGTH_LONG).show()
                        navController.navigate("eventos")
                    },
                    onError = { exception ->
                        onError(exception.message ?: "Error desconocido")
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

@Composable
fun BotonOlvidoContrasena(onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        Text("¿Has olvidado tu contraseña?", color = Color.White)
    }
}

@PreviewFontScale
@PreviewScreenSizes
@Preview(showBackground = true)
@Composable
fun PreviewLoginPantalla() {
    val navController = rememberNavController()
    LoginPantalla(navController = navController, isPreview = true)
}
