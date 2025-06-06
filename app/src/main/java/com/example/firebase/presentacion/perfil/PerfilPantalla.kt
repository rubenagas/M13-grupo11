package com.example.firebase.presentacion.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.firebase.ui.theme.Grey
import com.example.firebase.ui.theme.MidnightBlue


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
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPerfilPantalla (){
    val navController= rememberNavController()
    PerfilPantalla(navController)

}