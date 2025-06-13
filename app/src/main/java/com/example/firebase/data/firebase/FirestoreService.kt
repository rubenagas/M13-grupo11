package com.example.firebase.data.firebase

import com.example.firebase.data.modelo.Usuario
import com.google.firebase.firestore.FirebaseFirestore

object FirestoreService {

    private val db = FirebaseFirestore.getInstance()

    fun guardarUsuario(
        usuario: Usuario,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        db.collection("usuarios")
            .document(usuario.id)
            .set(usuario)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }


    // MAAAAAAAAAAAAARC DEBES DE USAR ESTE METODO PARA MOSTRAR LA INFO DEL USUARIO
    // Esta función sirve para obtener la información de un usuario desde la base de datos de Firebase Firestore.
    // Por ejemplo, después de que el usuario haya iniciado sesión, quiero saber su nombre, año de nacimiento y rol.

    //  Se usa el "uid", que es el identificador único del usuario que da Firebase cuando alguien se registra o inicia sesión.
    //  Esta función trabaja de forma automática y nos avisa si va bien o si hay algún problema.

    fun obtenerUsuario(
        uid: String, // Aquí le paso el UID del usuario que quiero buscar
        onSuccess: (Usuario) -> Unit, // Si todo va bien, se ejecuta esta parte y me da los datos del usuario
        onError: (Exception) -> Unit  // Si algo sale mal, me muestra el error
    ) {
        // Voy a la colección llamada "usuarios" que está en Firestore
        db.collection("usuarios")
            .document(uid) // Busco el documento que tiene como nombre el UID del usuario
            .get() // Intento obtener ese documento
            .addOnSuccessListener { doc ->
                // Si lo consigo, intento convertir el documento en un objeto de tipo Usuario
                val usuario = doc.toObject(Usuario::class.java)

                // Si no está vacío (es decir, sí lo encontró), lo devuelvo con éxito
                if (usuario != null) {
                    onSuccess(usuario) // Aquí le paso el usuario a quien haya llamado esta función
                } else {
                    // Si no encontró nada, lanzo un mensaje de error
                    onError(Exception("Usuario no encontrado"))
                }
            }
            // Si hubo un problema al intentar conectarse o buscar (como no hay internet), lo paso al onError
            .addOnFailureListener { onError(it) }
    }

}