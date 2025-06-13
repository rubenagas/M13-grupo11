package com.example.firebase.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.AuthCredential

object AuthService {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // REGISTRO con correo y contraseña
    //delegga a q lo llames desde RegsitroPantalla
    fun registrarConCorreo(
        email: String,
        password: String,
        onSuccess: (FirebaseUser) -> Unit,
        onError: (Exception) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                result.user?.let { onSuccess(it) }
            }
            .addOnFailureListener { onError(it) }
    }

    // Login con correo y contraseña
    fun loginConCorreo(
        email: String,
        password: String,
        onSuccess: (FirebaseUser) -> Unit,
        onError: (Exception) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                result.user?.let { onSuccess(it) }
            }
            .addOnFailureListener { onError(it) }
    }

    // Login con Google
    fun loginConGoogle(
        credential: AuthCredential,
        onSuccess: (FirebaseUser) -> Unit,
        onError: (Exception) -> Unit
    ) {
        auth.signInWithCredential(credential)
            .addOnSuccessListener { result ->
                result.user?.let { onSuccess(it) }
            }
            .addOnFailureListener { onError(it) }
    }

    // Obtener usuario actual
    fun usuarioActual(): FirebaseUser? = auth.currentUser


    // MARC ESTO TBM ES TUYO
    // Cerrar sesión
    fun cerrarSesion() {
        auth.signOut()
    }
}
