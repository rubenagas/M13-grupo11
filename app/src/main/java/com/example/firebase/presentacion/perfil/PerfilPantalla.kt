package com.example.firebase.presentacion.perfil

import androidx.compose.material3.*
import androidx.navigation.compose.*
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.firebase.ui.theme.Grey
import com.example.firebase.ui.theme.MidnightBlue
import com.example.firebase.ui.theme.Secondary
import com.google.firebase.auth.FirebaseAuth


@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun PerfilPantalla(navController: NavHostController){
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
    ){
//hola
        val auth = FirebaseAuth.getInstance()
        val screenHeight = maxHeight
        val scrollState = rememberScrollState()


        TextoMiperfil()
        Spacer(modifier = Modifier.height(screenHeight * 0.18f))
        Mainscreen(navController)
        Spacer(modifier = Modifier.height(screenHeight * 0.28f))

//sssss
    }
}




// CORRECTO:

@Composable
fun TextoMiperfil() {
    Text(
        text = "MI PERFIL",
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        color = Secondary
    )



}




@Composable
fun Mainscreen(navController: NavHostController) {

    // Estado para el nombre editable. Inicializado con "Marc"
    var nombreInput by remember { mutableStateOf("Marc") }

    // Estado para el apellido editable. Inicializado con "Alegria"
    var apellidoInput by remember { mutableStateOf("Alegria") }

    var EmailInput by remember { mutableStateOf("a@a.com") }

    // Estados para almacenar los valores "guardados" (simulación)
    // En una app real, estos podrían venir de un ViewModel o ser actualizados en Firebase.
    var nombreGuardado by remember { mutableStateOf("Marc") }
    var apellidoGuardado by remember { mutableStateOf("Alegria") }
    var EmailGuardado by remember { mutableStateOf("a@a.com") }


    Column(
        modifier = Modifier
            .fillMaxWidth() // Para que la columna ocupe el ancho disponible
            .padding(78.dp), // Padding general para el contenido de la columna
        horizontalAlignment = Alignment.CenterHorizontally // Centrar los elementos horizontalmente
    ) {

        OutlinedTextField(
            textStyle = TextStyle(color = Color.White),
            value = nombreInput,
            onValueChange = { nombreInput = it }, // Actualiza el estado del nombre
            label = { Text("Nombre",color = White)},
            modifier = Modifier
                .fillMaxWidth() // Que el campo ocupe el ancho
                .padding(vertical = 48.dp) // Espaciado vertical
        )


        OutlinedTextField(
            textStyle = TextStyle(color = Color.White),
            value = apellidoInput,
            onValueChange = { apellidoInput = it }, // Actualiza el estado del apellido
            label = { Text("Apellido", color = White)},
            modifier = Modifier
                .fillMaxWidth() // Que el campo ocupe el ancho
                .padding(vertical = 8.dp) // Espaciado vertical
        )

        OutlinedTextField(
            textStyle = TextStyle(color = Color.White),
            value = EmailInput,
            onValueChange = { EmailInput = it }, // Actualiza el estado del nombre
            label = { Text("Email",color = White)},
            modifier = Modifier
                .fillMaxWidth() // Que el campo ocupe el ancho
                .padding(vertical = 28.dp) // Espaciado vertical
        )


        Spacer(modifier = Modifier.height(16.dp)) // Espacio antes del botón

        Button(
            onClick = {
                // Lógica para "guardar" los valores
                // En una app real, aquí llamarías a tu ViewModel o función para actualizar Firebase, etc.
                if (nombreInput.isNotBlank()) {
                    nombreGuardado = nombreInput
                    println("Nombre guardado: $nombreGuardado")
                }
                if (apellidoInput.isNotBlank()) {
                    apellidoGuardado = apellidoInput
                    println("Apellido guardado: $apellidoGuardado")
                }
                // Aquí podrías mostrar un mensaje de "Guardado con éxito" (Snackbar, Toast)
            },
            modifier = Modifier.fillMaxWidth() // Que el botón ocupe el ancho
        ) {
            Text(text = "Guardar", color = White)
        }
        Button(
            modifier = Modifier.fillMaxWidth() ,// Que el botón ocupe el ancho
            onClick = {
                navController.navigate("inicio") {
                    FirebaseAuth.getInstance().signOut()
                }
            }
        ) {
            Text("Cerrar sesión")
        }
    }

}


//ssss
@Preview(showBackground = true)
@Composable
fun PreviewPerfilPantalla (){
    val navController= rememberNavController()
    PerfilPantalla(navController)

}