package com.example.firebase.presentacion.autentificacion

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.firebase.ui.theme.DarkIndigo
import com.example.firebase.ui.theme.Grey
import com.example.firebase.ui.theme.MidnightBlue
import com.example.firebase.ui.theme.Surface


@Composable
fun RecuperarPantalla (navController: NavHostController) {

    //desde aqui...
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
        var email by remember { mutableStateOf("")}
        var mensajeVisible by remember { mutableStateOf(false) }

        //hasta aqui lo repito. porque 1. es el fondo y sus dimensiones y 2. es poder hacer scrooll cuando este en horisontal


    // variables

    Column(
        modifier = Modifier
            .fillMaxSize()
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

        // Campo de correo
        Text(text = "Correo electrónico",
             color = Color.White)
        Spacer(modifier = Modifier.height(screenHeight * 0.01f))

        TextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("Introduce tu correo") },
            modifier = Modifier.fillMaxWidth()
        )

        //Campo boton
        Spacer(modifier = Modifier.height(screenHeight * 0.08f))

        Button(
            onClick = {  // Aquí iría la lógica para enviar el correo
                 mensajeVisible = true},

            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DarkIndigo)
        ) {
            Text(text = "Enviar", color = Surface)
        }

        Spacer(modifier = Modifier.height(screenHeight * 0.01f))


        // Mensaje que debe salir al realizar correctamemnte la recuperacion del correo
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

// sin conectar a ala firebase usar esto para visualizar
@Preview(showBackground = true)
@Composable
fun PreviewRecuperarPantalla(){

    val navController = rememberNavController()
    RecuperarPantalla(navController)
}