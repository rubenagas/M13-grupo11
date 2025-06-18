package com.example.firebase.data.modelo


// Modelo de datos para representar un usuario dentro de la app
// Este objeto se utiliza para guardar y recuperar la información del usuario desde Firebase Firestore
data class Usuario(
    val id: String = "",
    val nombre: String = "",
    val correo: String = "",
    val rol: String = "",
    val anioNacimiento: String = ""
)
