package com.example.firebase.presentacion.perfil

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
import java.nio.file.WatchEvent



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
        FooterPerfil()
    }

}

@Composable
fun FooterPerfil() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "mi footer", color = White)}
        }


@Composable
fun TextoMiperfil() {
    Text(
        modifier = Modifier.height(16.dp),
        text = "Mi Perfil",
        color = Yellow,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
    )


}

@Composable
fun Mainscreen() {

    var  name by remember {
        mutableStateOf(value = "")
    }
    var name2 by remember {
        mutableStateOf(value = "Marc")
    }
    var  apellido by remember {
        mutableStateOf(value = "")
    }
    var apellido2 by remember {
        mutableStateOf(value = "Alegria")
    }


    Column {

        TextField(value = name2, onValueChange = {name = it}, modifier = Modifier.padding(50.dp))
        TextField(value = apellido2, onValueChange = {name = it},modifier = Modifier.padding(46.dp) )

        Button(onClick = {if (name.isNotBlank()){name2 = name} }) { Text(text = "Guardar")}
        }

    }



@Preview(showBackground = true)
@Composable
fun PreviewPerfilPantalla (){
    val navController= rememberNavController()
    PerfilPantalla(navController)

}