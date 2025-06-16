package com.example.firebase.presentacion.autentificacion

import androidx.compose.foundation.layout.Row
import android.util.Patterns
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
import androidx.compose.ui.text.input.VisualTransformation
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

@Composable
fun RegistroPantalla(navController: NavHostController, isPreview: Boolean = false) {
    val context = if (!isPreview) LocalContext.current else null

    var email by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var confirmar by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

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
            Spacer(modifier = Modifier.height(screenHeight * 0.05f))

            TituloPantalla("CREA UNA CUENTA", navController)
            
            Spacer(modifier = Modifier.height(screenHeight * 0.05f))

            BloqueRegistro("Correo electrónico", email) { email = it }
            Spacer(modifier = Modifier.height(16.dp))

            BloqueRegistro("Contraseña", contrasena, esPassword = true) { contrasena = it }
            Spacer(modifier = Modifier.height(16.dp))

            BloqueRegistro("Confirmar contraseña", confirmar, esPassword = true) { confirmar = it }

            Spacer(modifier = Modifier.height(screenHeight * 0.08f))

            BottContinuar {
                if (isPreview) return@BottContinuar

                when {
                    email.isBlank() || contrasena.isBlank() || confirmar.isBlank() -> {
                        error = "Por favor completa todos los campos"
                    }
                    !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                        error = "Correo electrónico no válido"
                    }
                    contrasena != confirmar -> {
                        error = "Las contraseñas no coinciden"
                    }
                    contrasena.length < 6 -> {
                        error = "La contraseña debe tener al menos 6 caracteres"
                    }
                    else -> {
                        AuthService.registrarCorreo(
                            email = email,
                            contraseña = contrasena,
                            onSuccess = {
                                Toast.makeText(context, "Registro exitoso. Te hemos enviado un correo de verificación. Inicia sesión luego de confirmar.", Toast.LENGTH_LONG).show()
                                navController.popBackStack() // Volver al login
                            },
                            onError = {
                                error = it.message ?: "Error desconocido al registrar"
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            error?.let {
                MensajeErrorRegistro(it)
            }
        }
    }
}

@Composable
fun BotonAtras(navController: NavHostController){
    IconButton(onClick = { navController.popBackStack()}) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "atras",
            tint = Yellow
        )
    }
}

@Composable
fun TituloPantalla(texto: String, navController:NavHostController) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        BotonAtras(navController)
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
fun BloqueRegistro(etiqueta: String, valor: String, esPassword: Boolean = false, onCambio: (String) -> Unit) {
    Text(text = etiqueta, color = Color.White)
    Spacer(modifier = Modifier.height(4.dp))
    TextField(
        value = valor,
        onValueChange = onCambio,
        placeholder = { Text("$etiqueta") },
        visualTransformation = if (esPassword) PasswordVisualTransformation() else VisualTransformation.None,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun BottContinuar(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        colors = ButtonDefaults.buttonColors(containerColor = DarkIndigo)
    ) {
        Text("Continuar", color = Surface)
    }
}

@Composable
fun MensajeErrorRegistro(mensaje: String) {
    Text(
        text = mensaje,
        color = MaterialTheme.colorScheme.error,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally)
    )
}

//@PreviewFontScale
@PreviewScreenSizes
@Preview(showBackground = true)
@Composable
fun PreviewRegistroPantalla() {
    RegistroPantalla(navController = rememberNavController(), isPreview = true)
}
