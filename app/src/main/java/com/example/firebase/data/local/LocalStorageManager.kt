package com.example.firebase.data.local

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.firebase.presentacion.evento.Evento
import com.example.firebase.presentacion.evento.Jugador

class LocalStorageManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("AppData", Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        private const val KEY_EVENTOS = "eventos"
        private const val KEY_JUGADORES = "jugadores"
    }

    // ========== EVENTOS ==========

    fun guardarEvento(evento: Evento) {
        val eventos = obtenerEventos().toMutableList()
        eventos.add(evento)
        guardarEventos(eventos)
    }

    fun obtenerEventos(): List<Evento> {
        val json = sharedPreferences.getString(KEY_EVENTOS, null) ?: return emptyList()
        val type = object : TypeToken<List<Evento>>() {}.type
        return try {
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun actualizarEvento(evento: Evento) {
        val eventos = obtenerEventos().toMutableList()
        val index = eventos.indexOfFirst { it.id == evento.id }
        if (index != -1) {
            eventos[index] = evento
            guardarEventos(eventos)
        }
    }

    fun eliminarEvento(eventoId: String) {
        val eventos = obtenerEventos().filter { it.id != eventoId }
        guardarEventos(eventos)
    }

    private fun guardarEventos(eventos: List<Evento>) {
        val json = gson.toJson(eventos)
        sharedPreferences.edit().putString(KEY_EVENTOS, json).apply()
    }

    // ========== JUGADORES ==========

    fun inicializarJugadoresPorDefecto() {
        // Solo inicializar si no hay jugadores guardados
        if (obtenerJugadores().isEmpty()) {
            val jugadoresPorDefecto = listOf(
                Jugador("1", "Juan", "Pérez", "10", "juan.perez@email.com"),
                Jugador("2", "María", "García", "7", "maria.garcia@email.com"),
                Jugador("3", "Carlos", "López", "23", "carlos.lopez@email.com"),
                Jugador("4", "Ana", "Martínez", "14", "ana.martinez@email.com"),
                Jugador("5", "Pedro", "Rodríguez", "5", "pedro.rodriguez@email.com"),
                Jugador("6", "Laura", "Sánchez", "9", "laura.sanchez@email.com"),
                Jugador("7", "Diego", "González", "11", "diego.gonzalez@email.com"),
                Jugador("8", "Sofia", "Díaz", "21", "sofia.diaz@email.com"),
                Jugador("9", "Luis", "Fernández", "3", "luis.fernandez@email.com"),
                Jugador("10", "Elena", "Ruiz", "18", "elena.ruiz@email.com"),
                Jugador("11", "Miguel", "Torres", "15", "miguel.torres@email.com"),
                Jugador("12", "Carmen", "Vega", "8", "carmen.vega@email.com"),
                Jugador("13", "Roberto", "Castro", "19", "roberto.castro@email.com"),
                Jugador("14", "Isabel", "Moreno", "12", "isabel.moreno@email.com"),
                Jugador("15", "Francisco", "Jiménez", "6", "francisco.jimenez@email.com")
            )
            guardarJugadores(jugadoresPorDefecto)
        }
    }

    fun obtenerJugadores(): List<Jugador> {
        val json = sharedPreferences.getString(KEY_JUGADORES, null) ?: return emptyList()
        val type = object : TypeToken<List<Jugador>>() {}.type
        return try {
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun agregarJugador(jugador: Jugador) {
        val jugadores = obtenerJugadores().toMutableList()
        jugadores.add(jugador)
        guardarJugadores(jugadores)
    }

    fun actualizarJugador(jugador: Jugador) {
        val jugadores = obtenerJugadores().toMutableList()
        val index = jugadores.indexOfFirst { it.id == jugador.id }
        if (index != -1) {
            jugadores[index] = jugador
            guardarJugadores(jugadores)
        }
    }

    fun eliminarJugador(jugadorId: String) {
        val jugadores = obtenerJugadores().filter { it.id != jugadorId }
        guardarJugadores(jugadores)
    }

    private fun guardarJugadores(jugadores: List<Jugador>) {
        val json = gson.toJson(jugadores)
        sharedPreferences.edit().putString(KEY_JUGADORES, json).apply()
    }

    // ========== UTILIDADES ==========

    fun limpiarTodo() {
        sharedPreferences.edit().clear().apply()
    }

    fun exportarDatos(): String {
        val datos = mapOf(
            "eventos" to obtenerEventos(),
            "jugadores" to obtenerJugadores()
        )
        return gson.toJson(datos)
    }

    fun importarDatos(jsonData: String): Boolean {
        return try {
            val type = object : TypeToken<Map<String, Any>>() {}.type
            val datos: Map<String, Any> = gson.fromJson(jsonData, type)

            // Importar eventos
            val eventosJson = gson.toJson(datos["eventos"])
            val eventosType = object : TypeToken<List<Evento>>() {}.type
            val eventos: List<Evento> = gson.fromJson(eventosJson, eventosType)
            guardarEventos(eventos)

            // Importar jugadores
            val jugadoresJson = gson.toJson(datos["jugadores"])
            val jugadoresType = object : TypeToken<List<Jugador>>() {}.type
            val jugadores: List<Jugador> = gson.fromJson(jugadoresJson, jugadoresType)
            guardarJugadores(jugadores)

            true
        } catch (e: Exception) {
            false
        }
    }
}