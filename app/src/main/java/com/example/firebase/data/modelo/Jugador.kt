package com.example.firebase.data.modelo

data class Jugador(
    val id: String,
    val nombre: String,
    val apellido: String,
    val numero: String,
    val email: String = ""
)