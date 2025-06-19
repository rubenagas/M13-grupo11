package com.example.firebase

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.firebase.ui.theme.Secondary


// Este bloque define el footer de la app, o sea, la barra de navegación inferior.
// Tiene tres botones: uno para ir a la sección de Eventos, otro para Equipos y otro para Perfil.
// Cada botón tiene un ícono personalizado (como una pestaña) y un texto debajo.
// Todo está dentro de un BottomAppBar con fondo oscuro y una altura fija.


@Composable
fun FooterBar(navController: NavHostController) {
    BottomAppBar(
        containerColor = Color(0xFF0C1317),
        tonalElevation = 5.dp,
        modifier = Modifier.height(72.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            //  // Botón para sección de EVENTOS
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick = { navController.navigate("eventos") }) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_action_evento),
                        contentDescription = "Eventos",
                        modifier = Modifier.size(28.dp)
                    )
                }
                Text("Eventos", color = Secondary, fontSize = 12.sp)
            }

            // Botón para sección de EQUIPOS
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick = { navController.navigate("equipo") }) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_action_equipo),
                        contentDescription = "Equipo",
                        modifier = Modifier.size(28.dp)
                    )
                }
                Text("Equipo", color = Secondary, fontSize = 12.sp)
            }

            // Botón para sección de PERFIL
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick = { navController.navigate("perfil") }) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_action_usuario),
                        contentDescription = "Perfil",
                        modifier = Modifier.size(28.dp)
                    )
                }
                Text("Perfil", color = Secondary, fontSize = 12.sp)
            }
        }
    }
}


// Vista previa del FooterBar con un Scaffold de ejemplo
@Preview(showBackground = true)
@Composable
fun PreviewPantallaConFooter() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            FooterBar(navController = navController)
        }
    ) {
        //  // Contenido de prueba (texto de ejemplo para ver cómo se ajusta con el footer)
        Text("Contenido de la pantalla", modifier = Modifier.padding(it))
    }
}
