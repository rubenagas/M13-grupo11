package com.example.firebase.presentacion.autentificacion

import android.content.ContentValues.TAG
import android.util.Log
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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

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
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            TituloLogin("INICIA SESIÓN", navController)

            Spacer(modifier = Modifier.height(screenHeight * 0.05f))

            Email(email) { email = it }

            Spacer(modifier = Modifier.height(16.dp))

            Contrasena(password) { password = it }

            Spacer(modifier = Modifier.height(screenHeight * 0.08f))

            Entrar(
                email = email,
                password = password,
                isPreview = isPreview,
                context = context,
                navController = navController,
                onError = { mensaje -> errorMessage = mensaje }
            )

            Spacer(modifier = Modifier.height(8.dp))

            errorMessage?.let { MensajeError(it) }

            Spacer(modifier = Modifier.height(screenHeight * 0.05f))

            ContrasenaOlvidada {
                if (!isPreview) navController.navigate("recuperar")
            }

            Spacer(modifier = Modifier.height(24.dp)) // extra padding final
        }

    }
    }


@Composable
fun BotonAtrasL(navController: NavHostController){
    IconButton(onClick = { navController.popBackStack()}) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "atras",
            tint = Yellow
        )
    }
}

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
            if (email.isBlank() || password.isBlank()) {
                onError("Completa todos los campos")
                return@Button
            }

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
fun ContrasenaOlvidada(onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        Text("¿Has olvidado tu contraseña?",
            modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally),
            color = Color.White)
    }

}


//@PreviewFontScale
@PreviewScreenSizes
@Preview(showBackground = true)
@Composable
fun PreviewLoginPantalla() {
    val navController = rememberNavController()
    LoginPantalla(navController = navController, isPreview = true)
}
