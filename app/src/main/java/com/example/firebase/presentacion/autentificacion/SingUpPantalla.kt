package com.example.firebase.presentacion.autentificacion

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.firebase.ui.theme.DarkIndigo
import com.example.firebase.ui.theme.Grey
import com.example.firebase.ui.theme.MidnightBlue
import com.example.firebase.ui.theme.Surface
import com.google.firebase.auth.FirebaseAuth

@Preview
@Composable
fun SignUpPantalla(navController: NavHostController) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    var email by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var confirmContr by remember { mutableStateOf("") }
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

            Spacer(modifier = Modifier.height(screenHeight * 0.05f))

            Text(
                text = "CREA UNA CUENTA",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Yellow,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(screenHeight * 0.05f))

            // Correo
            Text(text = "Correo electrónico", color = Color.White)
            Spacer(modifier = Modifier.height(screenHeight * 0.01f))
            TextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Introduce tu correo") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Contraseña
            Text(text = "Contraseña", color = Color.White)
            Spacer(modifier = Modifier.height(screenHeight * 0.01f))
            TextField(
                value = contrasena,
                onValueChange = { contrasena = it },
                placeholder = { Text("Introduce tu contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Confirmar contraseña
            Text(text = "Confirmar contraseña", color = Color.White)
            Spacer(modifier = Modifier.height(screenHeight * 0.01f))
            TextField(
                value = confirmContr,
                onValueChange = { confirmContr = it },
                placeholder = { Text("Confirma tu contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(screenHeight * 0.08f))

            // Botón de registro
            Button(
                onClick = {
                    when {
                        email.isBlank() || contrasena.isBlank() || confirmContr.isBlank() -> {
                            errorMessage = "Por favor completa todos los campos"
                        }
                        !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                            errorMessage = "Correo electrónico no válido"
                        }
                        contrasena != confirmContr -> {
                            errorMessage = "Las contraseñas no coinciden"
                        }
                        contrasena.length < 6 -> {
                            errorMessage = "La contraseña debe tener al menos 6 caracteres"
                        }
                        else -> {
                            auth.createUserWithEmailAndPassword(email, contrasena)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(context, "Usuario registrado", Toast.LENGTH_LONG).show()
                                        navController.navigate("eventos")
                                    } else {
                                        errorMessage = task.exception?.message ?: "Error desconocido"
                                    }
                                }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkIndigo)
            ) {
                Text("Continuar", color = Surface)
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

