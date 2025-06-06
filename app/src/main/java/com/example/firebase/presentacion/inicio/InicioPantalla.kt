package com.example.firebase.presentacion.inicio

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.firebase.R
import com.example.firebase.ui.theme.DarkIndigo
import com.example.firebase.ui.theme.Grey
import com.example.firebase.ui.theme.MidnightBlue
import com.example.firebase.ui.theme.Surface


@Composable
fun InicioPantalla(navController: NavHostController) {
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
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(screenHeight * 0.05f))
// copiar hasta el spacer
            Text(
                text = "MA·AT",
                color = Yellow,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(screenHeight * 0.05f))

            Image(
                painter = painterResource(id = R.drawable.maat),
                contentDescription = "Logo",
                modifier = Modifier
                    .width(180.dp)
                    .padding(10.dp)
            )

            Spacer(modifier = Modifier.height(screenHeight * 0.05f))

            Text(
                text = "Bienvenidos",
                color = Yellow,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                "Confía en MA·AT y organicemos tus eventos",
                color = White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(screenHeight * 0.08f))

            Button(
                onClick = {  navController.navigate("login") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkIndigo)
            ) {
                Text(text = "Iniciar Sesión", color = Surface)
            }


            Spacer(modifier = Modifier.height(screenHeight * 0.02f))

            Button(
                onClick = { navController.navigate("registro") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkIndigo)
            ) {
                Text(text = "Registrate", color = Surface)
            }

            Spacer(modifier = Modifier.height(16.dp))

            BotonGoogle(
                modifier = Modifier.padding(bottom = screenHeight * 0.08f),
                painter = painterResource(id = R.drawable.google),
                title = "Continuar con Google",
                onClick = { /*logica */}
            )
        }
    }
}


@Composable
fun BotonGoogle(
    modifier: Modifier = Modifier,
    painter: Painter,
    title: String,
    onClick: () -> Unit
){

    ElevatedButton(
        onClick = onClick,
        modifier =  modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
            shape = CircleShape,
            colors =ButtonDefaults.buttonColors(containerColor = DarkIndigo),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .size(20.dp)
                    .padding(end = 8.dp)
            )
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color.White
            )
        }
    }
}

// composable para la visualizacion. preview sioin parametros
@Preview(showBackground = true)
@Composable
fun PreviewEventosPantalla() {
    val navController = rememberNavController()
    InicioPantalla(navController)
}
