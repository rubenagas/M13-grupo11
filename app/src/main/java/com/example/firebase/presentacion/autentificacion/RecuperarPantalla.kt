package com.example.firebase.presentacion.autentificacion

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Yellow
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

@Composable
fun RecuperarPantalla(navController: NavHostController, isPreview: Boolean = false) {
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
        var email by remember { mutableStateOf("") }
        var mensajeVisible by remember { mutableStateOf(isPreview) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            Text(
                text = "RECUPERAR TU CONTRASEÑA",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Yellow,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(screenHeight * 0.05f))

            Text(text = "Correo electrónico", color = Color.White)
            Spacer(modifier = Modifier.height(screenHeight * 0.01f))

            TextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Introduce tu correo") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(screenHeight * 0.08f))

            Button(
                onClick = {
                    if (!isPreview) {
                        // Aquí puedes agregar la lógica real con Firebase en el futuro
                        mensajeVisible = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkIndigo)
            ) {
                Text(text = "Enviar", color = Surface)
            }

            Spacer(modifier = Modifier.height(screenHeight * 0.01f))

            if (mensajeVisible) {
                Text(
                    text = "¡Listo! El correo ha sido enviado",
                    color = Color.White,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@PreviewFontScale
@PreviewScreenSizes
@Preview(showBackground = true)
@Composable
fun PreviewRecuperarPantalla() {
    val navController = rememberNavController()
    RecuperarPantalla(navController = navController, isPreview = true)
}
