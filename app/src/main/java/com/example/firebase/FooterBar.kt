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

            //  EVENTOS
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

            //  EQUIPOS
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick = { navController.navigate("equipos") }) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_action_equipo),
                        contentDescription = "Equipos",
                        modifier = Modifier.size(28.dp)
                    )
                }
                Text("Equipos", color = Secondary, fontSize = 12.sp)
            }

            //  PERFIL
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



@Preview(showBackground = true)
@Composable
fun PreviewPantallaConFooter() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            FooterBar(navController = navController)
        }
    ) {
        // Contenido de prueba
        Text("Contenido de la pantalla", modifier = Modifier.padding(it))
    }
}
