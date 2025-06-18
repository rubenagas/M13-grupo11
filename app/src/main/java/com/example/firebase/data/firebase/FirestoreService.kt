package com.example.firebase.data.firebase


import android.annotation.SuppressLint
import com.example.firebase.data.modelo.Usuario
import com.google.firebase.firestore.FirebaseFirestore

// Servicio para interactuar con Firebase Firestore (guardar y leer usuarios)
object FirestoreService {

    // Instancia de la base de datos Firestore
    @SuppressLint("StaticFieldLeak")
    private val db = FirebaseFirestore.getInstance()

    // Guarda un objeto Usuario en la colección "usuarios" con el ID del usuario como clave
    fun guardarUsuario(
        usuario: Usuario,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        db.collection("usuarios")
            .document(usuario.id) // usa el UID como ID del documento
            .set(usuario) // guarda el objeto completo
            .addOnSuccessListener { onSuccess() } // callback si salió bien
            .addOnFailureListener { onError(it) }  // callback si hubo un error
    }


    // marc si no lo usas borrralo
    // Obtiene los datos del usuario desde Firestore usando su UID
    fun obtenerUsuario(
        uid: String, // UID del usuario (provisto por Firebase Auth)
        onSuccess: (Usuario) -> Unit, // Se ejecuta si los datos fueron cargados correctamente
        onError: (Exception) -> Unit  // Se ejecuta si ocurre un error
    ) {
        db.collection("usuarios")
            .document(uid) // Accede al documento que corresponde a ese UID
            .get() // Intenta obtenerlo
            .addOnSuccessListener { doc ->
                val usuario = doc.toObject(Usuario::class.java) // convierte el documento en un objeto Usuario

                if (usuario != null) {
                    onSuccess(usuario) // usuario encontrado y convertido correctamente
                } else {
                    onError(Exception("Usuario no encontrado")) // documento vacío o mal formateado
                }
            }
            .addOnFailureListener { onError(it) } // error al conectarse o consultar Firestore
    }
}