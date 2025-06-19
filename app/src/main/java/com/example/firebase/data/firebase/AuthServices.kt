package com.example.firebase.data

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.provider.Settings.Global.getString
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.navigation.NavHostController
import com.example.firebase.R
import com.example.firebase.data.firebase.FirestoreService
import com.example.firebase.data.modelo.Usuario
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object AuthService {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()


    // Registro con correo y contraseña usando Firebase Auth
    // Llamado desde RegistroPantalla
    fun registrarCorreo(email: String, contraseña: String, onSuccess: (FirebaseUser) -> Unit, onError: (Exception) -> Unit) {
        auth.createUserWithEmailAndPassword(email, contraseña)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    // Envía correo de verificación (documentado en Firebase Auth)
                    user?.sendEmailVerification()
                        ?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d(TAG, "Email sent.")
                            }
                        }
                    if (user != null) onSuccess(user)
                } else {
                    onError(task.exception ?: Exception("Error desconocido"))
                }
            }


    }


    // Login con correo y contraseña
    fun loginCorreo(email: String, contraseña: String, navController: NavHostController, context: Context, onError: (Exception) -> Unit) {
        auth.signInWithEmailAndPassword(email, contraseña)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.reload()?.addOnSuccessListener {
                        if (user.isEmailVerified) {
                            // Obtenemos el usuario desde Firestore para verificar si el perfil está completo
                            FirestoreService.obtenerUsuario(user.uid,
                                onSuccess = { usuario ->
                                    val completo = !usuario.nombre.isNullOrBlank()
                                            && !usuario.rol.isNullOrBlank()
                                            && !usuario.anioNacimiento.isNullOrBlank()

                                    guardarSesion(context, email)

                                    // navegamos según estado del perfil
                                    if (completo) {
                                        navController.navigate("eventos")
                                    } else {
                                        navController.navigate("rol")
                                    }
                                },
                                onError = {
                                    guardarSesion(context, email)
                                    navController.navigate("rol")
                                }
                            )
                        } else {
                            onError(Exception("Debes verificar tu correo electrónico antes de continuar."))
                        }
                    }
                } else {
                    onError(task.exception ?: Exception("Error al iniciar sesión"))
                }
            }
    }


    // Verificacion de sesion abierta
    fun guardarSesion(context: Context, email: String) {
        val prefs = context.getSharedPreferences("sharedpreferences", Context.MODE_PRIVATE)
        prefs.edit().putString("Email", email).apply()
    }


    //ya está logueado?? si lo esta, esto revisa si hay un usuario en Firebase
    //me estoy quedando sin tiempo.No puede avanzar mas hasta que la pantalla perilpantalla marc lo haga
    //porque necesito el botn cerrar sesion activo. lo dejare para futruas mejoras
    //fun sesionAbierta(): Boolean = auth.currentUser != null



    // Login con Google. Se copia y modifica el codigo del sitio de la documentacion de acceso con google
    @SuppressLint("CoroutineCreationDuringComposition")
    fun loginGoogle(
        context: Context,
        coroutineScope: CoroutineScope,
        navController: NavHostController
    ) {
        val credentialManager = CredentialManager.create(context)
        val clientId = context.getString(R.string.android_cliente)

        val signInWithGoogleOption = GetSignInWithGoogleOption.Builder(clientId)
            .setNonce("nonce") // Se recomienda generar una nonce real para seguridad
            .build()
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(signInWithGoogleOption)
            .build()

        coroutineScope.launch {
            try {
                val result = credentialManager.getCredential(request = request, context = context)
                val credential = result.credential
                if (credential is CustomCredential &&
                    credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                ) {
                    try {
                        val googleCredential = GoogleIdTokenCredential.createFrom(credential.data)
                        val idToken = googleCredential.idToken
                        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)

                        FirebaseAuth.getInstance().signInWithCredential(firebaseCredential)
                            .addOnSuccessListener {
                                val user = Firebase.auth.currentUser
                                val email = user?.email ?: ""

                                if (user != null) {
                                    FirestoreService.obtenerUsuario(user.uid,
                                        onSuccess = { usuario ->
                                            val completo = !usuario.nombre.isNullOrBlank()
                                                    && !usuario.rol.isNullOrBlank()
                                                    && !usuario.anioNacimiento.isNullOrBlank()

                                            guardarSesion(context, email)

                                            if (completo) {
                                                navController.navigate("eventos")
                                            } else {
                                                navController.navigate("rol")
                                            }
                                        },
                                        onError = {
                                            guardarSesion(context, email)
                                            navController.navigate("rol")
                                        }
                                    )
                                }
                            }
                    } catch (e: GoogleIdTokenParsingException) {
                        // Registro del error con Crashlytics
                        FirebaseCrashlytics.getInstance().recordException(e)
                        Toast.makeText(context, "Token de Google inválido", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(context, "Credencial desconocida", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                FirebaseCrashlytics.getInstance().recordException(e)
                Toast.makeText(context, "Error inesperado", Toast.LENGTH_LONG).show()
            }
        }
    }


        //Marc, esto es para que obtener usuario actual
        fun usuarioActual(): FirebaseUser? = auth.currentUser



        // Cerrar sesión de firebase
        fun cerrarSesion() {
                    auth.signOut()

                }
            }






