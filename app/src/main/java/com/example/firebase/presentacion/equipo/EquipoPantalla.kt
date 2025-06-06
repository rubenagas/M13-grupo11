package com.example.firebase.presentacion.equipo

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.firebase.FooterBar
import com.example.firebase.R
import com.example.firebase.ui.theme.Grey
import com.example.firebase.ui.theme.MidnightBlue
import com.example.firebase.ui.theme.Secondary
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EquipoPantalla(
 navController: NavHostController,
 userRole: String = "jugador" // Cambia esto según el rol del usuario actual
) {
 // Lista de imágenes para el carrusel
 val imageList = listOf(
  R.drawable.equipo_imagen_1,
  R.drawable.equipo_imagen_2,
  R.drawable.equipo_imagen_3,
  R.drawable.equipo_imagen_4
 )

 val pagerState = rememberPagerState(pageCount = { imageList.size })

 // Auto-scroll del carrusel
 LaunchedEffect(pagerState) {
  while (true) {
   delay(3000) // Cambia de imagen cada 3 segundos
   val nextPage = (pagerState.currentPage + 1) % imageList.size
   pagerState.animateScrollToPage(nextPage)
  }
 }

 Scaffold(
  bottomBar = {
   FooterBar(navController = navController)
  }
 ) { innerPadding ->
  BoxWithConstraints(
   modifier = Modifier
    .fillMaxSize()
    .padding(innerPadding)
    .background(
     Brush.verticalGradient(
      listOf(Grey, MidnightBlue),
      startY = 0f,
      endY = 600f
     )
    )
  ) {
   val screenHeight = maxHeight

   Column(
    modifier = Modifier
     .fillMaxSize()
     .padding(horizontal = 16.dp)
   ) {
    Spacer(modifier = Modifier.height(screenHeight * 0.05f))

    // Título principal
    Text(
     text = "EQUIPO",
     fontSize = 32.sp,
     fontWeight = FontWeight.Bold,
     color = Secondary,
     modifier = Modifier.align(Alignment.Start)
    )

    Spacer(modifier = Modifier.height(8.dp))

    // Botón "+Invitar a miembros" solo para entrenador
    if (userRole == "entrenador") {
     Button(
      onClick = { /* Acción para invitar miembros */ },
      modifier = Modifier
       .fillMaxWidth()
       .height(40.dp),
      colors = ButtonDefaults.buttonColors(
       containerColor = Color.White,
       contentColor = Color.Black
      ),
      shape = RoundedCornerShape(20.dp)
     ) {
      Text(
       text = "+Invitar a miembros",
       fontSize = 14.sp
      )
     }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Sección "Miembros del equipo" con botón "Ver Todo"
    Row(
     modifier = Modifier.fillMaxWidth(),
     horizontalArrangement = Arrangement.SpaceBetween,
     verticalAlignment = Alignment.CenterVertically
    ) {
     Text(
      text = "Miembros del equipo",
      fontSize = 16.sp,
      color = Color.White
     )

     TextButton(
      onClick = { /* Navegar a lista completa */ }
     ) {
      Text(
       text = "Ver Todo",
       fontSize = 14.sp,
       color = Color.White
      )
     }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Carrusel de imágenes
    Box(
     modifier = Modifier
      .fillMaxWidth()
      .height(200.dp)
      .clip(RoundedCornerShape(12.dp))
      .background(Color.White.copy(alpha = 0.1f))
    ) {
     HorizontalPager(
      state = pagerState,
      modifier = Modifier.fillMaxSize()
     ) { page ->
      // Placeholder para las imágenes
      // Reemplaza esto con Image() cuando tengas las imágenes reales
      Box(
       modifier = Modifier
        .fillMaxSize()
        .background(Color.Gray.copy(alpha = 0.3f)),
       contentAlignment = Alignment.Center
      ) {
       Text(
        text = "Imagen ${page + 1}",
        color = Color.White,
        fontSize = 20.sp
       )
      }

      // Cuando tengas las imágenes, usa esto:
      /*
      Image(
          painter = painterResource(id = imageList[page]),
          contentDescription = "Imagen del equipo",
          modifier = Modifier.fillMaxSize(),
          contentScale = ContentScale.Crop
      )
      */
     }
    }

    // Indicadores del carrusel
    Row(
     modifier = Modifier
      .fillMaxWidth()
      .padding(top = 8.dp),
     horizontalArrangement = Arrangement.Center
    ) {
     repeat(imageList.size) { index ->
      Box(
       modifier = Modifier
        .padding(horizontal = 4.dp)
        .size(8.dp)
        .clip(CircleShape)
        .background(
         if (pagerState.currentPage == index) Color.White
         else Color.White.copy(alpha = 0.5f)
        )
      )
     }
    }

    Spacer(modifier = Modifier.height(24.dp))

    // Tarjetas de información según el rol
    if (userRole == "jugador") {
     // Vista del jugador
     MiembrosTarjeta()
     Spacer(modifier = Modifier.height(16.dp))
     CajaTarjeta()
    } else {
     // Vista del entrenador
     MiembrosTarjeta()
     Spacer(modifier = Modifier.height(16.dp))
     CajaEntrenadorTarjeta()
    }

    Spacer(modifier = Modifier.weight(1f))
   }
  }
 }
}

@Composable
fun MiembrosTarjeta() {
 Card(
  modifier = Modifier
   .fillMaxWidth()
   .height(80.dp),
  colors = CardDefaults.cardColors(
   containerColor = Color.White.copy(alpha = 0.9f)
  ),
  shape = RoundedCornerShape(12.dp)
 ) {
  Column(
   modifier = Modifier
    .fillMaxSize()
    .padding(16.dp),
   verticalArrangement = Arrangement.Center
  ) {
   Text(
    text = "Miembros",
    fontSize = 16.sp,
    fontWeight = FontWeight.Medium,
    color = Color.Gray
   )
   Text(
    text = "Total de miembros:",
    fontSize = 14.sp,
    color = Color.Gray,
    textAlign = TextAlign.Center
   )
  }
 }
}

@Composable
fun CajaTarjeta() {
 Card(
  modifier = Modifier
   .fillMaxWidth()
   .height(80.dp),
  colors = CardDefaults.cardColors(
   containerColor = Color.White.copy(alpha = 0.9f)
  ),
  shape = RoundedCornerShape(12.dp)
 ) {
  Column(
   modifier = Modifier
    .fillMaxSize()
    .padding(16.dp),
   verticalArrangement = Arrangement.Center
  ) {
   Text(
    text = "Caja",
    fontSize = 16.sp,
    fontWeight = FontWeight.Medium,
    color = Color.Gray
   )
   Text(
    text = "Saldo:",
    fontSize = 14.sp,
    color = Color.Gray,
    textAlign = TextAlign.Center
   )
  }
 }
}

@Composable
fun CajaEntrenadorTarjeta() {
 Card(
  modifier = Modifier
   .fillMaxWidth()
   .height(80.dp),
  colors = CardDefaults.cardColors(
   containerColor = Color.White.copy(alpha = 0.9f)
  ),
  shape = RoundedCornerShape(12.dp)
 ) {
  Column(
   modifier = Modifier
    .fillMaxSize()
    .padding(16.dp),
   verticalArrangement = Arrangement.Center
  ) {
   Text(
    text = "Caja",
    fontSize = 16.sp,
    fontWeight = FontWeight.Medium,
    color = Color.Gray
   )
   Text(
    text = "Saldo:....",
    fontSize = 14.sp,
    color = Color.Gray,
    textAlign = TextAlign.Center
   )
  }
 }
}

@Preview(showBackground = true, name = "Vista Jugador")
@Composable
fun PreviewEquipoPantallaJugador() {
 val navController = rememberNavController()
 EquipoPantalla(navController, userRole = "jugador")
}

@Preview(showBackground = true, name = "Vista Entrenador")
@Composable
fun PreviewEquipoPantallaEntrenador() {
 val navController = rememberNavController()
 EquipoPantalla(navController, userRole = "entrenador")
}