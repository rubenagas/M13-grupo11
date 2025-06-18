package com.example.firebase.presentacion.perfil

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.navigation.compose.*
import android.R
import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.firebase.ui.theme.Grey
import com.example.firebase.ui.theme.MidnightBlue
import com.example.firebase.ui.theme.Secondary
import com.google.firebase.auth.FirebaseAuth
import java.nio.file.WatchEvent


val currentUser = FirebaseAuth.getInstance().currentUser
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


        val screenHeight = maxHeight
        val scrollState = rememberScrollState()


      TextoMiperfil()
       Spacer(modifier = Modifier.height(screenHeight * 0.18f))
        Mainscreen()
        Spacer(modifier = Modifier.height(screenHeight * 0.28f))
        FooterPerfil(navController = navController)

            }
        }




// CORRECTO:
@Composable // Asegúrate de que este Composable esté anotado
fun FooterPerfil(navController: NavHostController) {
    Box(
        modifier = Modifier.fillMaxSize(), // Es buena práctica pasar el Modifier como parámetro
        contentAlignment = Alignment.Center
    ) {
        Button(
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
            fun Mainscreen(
                // Opcional: Podrías pasar los valores iniciales como parámetros
                // initialName: String = "Marc",
                // initialApellido: String = "Alegria"
            ) {

                // Estado para el nombre editable. Inicializado con "Marc"
                var nombreInput by remember { mutableStateOf("Marc") }

                // Estado para el apellido editable. Inicializado con "Alegria"
                var apellidoInput by remember { mutableStateOf("Alegria") }

                // Estados para almacenar los valores "guardados" (simulación)
                // En una app real, estos podrían venir de un ViewModel o ser actualizados en Firebase.
                var nombreGuardado by remember { mutableStateOf("Marc") }
                var apellidoGuardado by remember { mutableStateOf("Alegria") }


                Column(
                    modifier = Modifier
                        .fillMaxWidth() // Para que la columna ocupe el ancho disponible
                        .padding(16.dp), // Padding general para el contenido de la columna
                    horizontalAlignment = Alignment.CenterHorizontally // Centrar los elementos horizontalmente
                ) {

                    OutlinedTextField(
                        value = nombreInput,
                        onValueChange = { nombreInput = it }, // Actualiza el estado del nombre
                        label = { Text("Nombre") },
                        modifier = Modifier
                            .fillMaxWidth() // Que el campo ocupe el ancho
                            .padding(vertical = 8.dp) // Espaciado vertical
                    )

                    OutlinedTextField(
                        value = apellidoInput,
                        onValueChange = { apellidoInput = it }, // Actualiza el estado del apellido
                        label = { Text("Apellido") },
                        modifier = Modifier
                            .fillMaxWidth() // Que el campo ocupe el ancho
                            .padding(vertical = 8.dp) // Espaciado vertical
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
                        Text(text = "Guardar")
                    }
                }
            }

@Preview(showBackground = true)
@Composable
fun PreviewPerfilPantalla (){
    val navController= rememberNavController()
    PerfilPantalla(navController)

}