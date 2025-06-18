package com.example.firebase.data.modelo

import androidx.compose.ui.graphics.Color

data class Evento(
    val id: String = "",
    val nombre: String = "",
    val fecha: String = "",
    val tipo: String = "",
    val horaReunion: String = "",
    val horaInicio: String = "",
    val horaFin: String = "",
    val direccion: String = "",
    val descripcion: String = "",
    val participantes: List<String> = emptyList(),
    val asistentes: List<String> = emptyList(),
    val creadorId: String = "",
    val timestamp: Long = 0L,
    val activo: Boolean = true
)
