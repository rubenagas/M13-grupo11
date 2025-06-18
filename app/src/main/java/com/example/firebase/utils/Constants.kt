package com.example.firebase.utils

object Constants {
    // SharedPreferences
    const val PREFS_NAME = "AppData"
    const val KEY_EVENTOS = "eventos"
    const val KEY_JUGADORES = "jugadores"

    // Tipos de eventos
    const val TIPO_ENTRENAMIENTO = "Entrenamiento"
    const val TIPO_PARTIDO = "Partido"
    const val TIPO_REUNION = "Reunión"
    const val TIPO_OTRO = "Otro"

    // Formatos de fecha y hora
    const val DATE_FORMAT = "DD/MM/AAAA"
    const val TIME_FORMAT = "HH:MM"
    const val EXPORT_DATE_FORMAT = "yyyy-MM-dd_HH-mm"

    // Valores por defecto
    const val DEFAULT_ROLE = "jugador"
    const val TRAINER_ROLE = "entrenador"
}