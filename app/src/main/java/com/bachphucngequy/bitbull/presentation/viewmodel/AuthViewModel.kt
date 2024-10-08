package com.bachphucngequy.bitbull.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bachphucngequy.bitbull.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import java.io.File

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    lateinit var googleSignInClient: GoogleSignInClient

    init {
        checkAuthStatus()
    }

    fun checkAuthStatus() {
        if (auth.currentUser == null) {
            _authState.value = AuthState.Unauthenticated
        } else {
            _authState.value = AuthState.Authenticated
        }
    }

    fun login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email or password can't be empty")
            return
        }
        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value =
                        AuthState.Error(task.exception?.message ?: "Something went wrong")
                }
            }
    }

    fun signup(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email or password can't be empty")
            return
        }
        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        // Create a new user document in Firestore
                        val userMap = hashMapOf(
                            "name" to "Investor",
                            "email" to email
                        )
                        firestore.collection("AppUsers").document(user.uid)
                            .set(userMap)
                            .addOnSuccessListener {
                                _authState.value = AuthState.Authenticated
                                savePasswordToFile(email, password)
                            }
                            .addOnFailureListener { e ->
                                _authState.value = AuthState.Error("Failed to store user data: ${e.message}")
                            }
                    } else {
                        _authState.value = AuthState.Error("User creation successful but user is null")
                    }
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Something went wrong")
                }
            }
    }

    fun signout() {
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }

    fun configureGoogleSignIn(context: Context) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(context, gso)
    }

    fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { signInTask ->
                if (signInTask.isSuccessful) {
                    val user = signInTask.result?.user
                    if (user != null) {
                        // User is signed in
                        _authState.value = AuthState.Authenticated
                        // Check if the user is new
                        if (signInTask.result?.additionalUserInfo?.isNewUser == true) {
                            handleNewUser(user)
                            // Create a new user document in Firestore
                            val userMap = hashMapOf(
                                "name" to "Investor",
                                "email" to user.email
                            )
                            firestore.collection("AppUsers").document(user.uid)
                                .set(userMap)
                        }
                        if (signInTask.result?.additionalUserInfo?.isNewUser == false){
                            handleNewUser(user)
                        }
                    } else {
                        _authState.value = AuthState.Error("Failed to get user from Google Sign In.")
                    }
                } else {
                    handleSignInError(signInTask.exception)
                }
            }
    }

    private fun handleNewUser(user: FirebaseUser) {
        // Generate a default password (you might want to use a more secure method)
        val defaultPassword = generateDefaultPassword()

        // Link the Google account with a password
        val credential = EmailAuthProvider.getCredential(user.email!!, defaultPassword)
        user.linkWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Password authentication added successfully
                    // You might want to store this information or notify the user
                    savePasswordToFile(user.email!!, defaultPassword)
                } else {
                    // Handle the error
                    }
            }
    }

    private fun generateDefaultPassword(): String {
        // This is a simple example. In a real app, use a more secure method to generate passwords
        return "DefaultPass" + (1000..9999).random()
    }

    private fun handleSignInError(exception: Exception?) {
        when (exception) {
            is FirebaseAuthInvalidUserException -> {
                // This shouldn't happen as signInWithCredential creates a new account if one doesn't exist
                _authState.value = AuthState.Error("Invalid user: ${exception.message}")
            }
            is FirebaseAuthInvalidCredentialsException -> {
                _authState.value = AuthState.Error("Invalid credentials: ${exception.message}")
            }
            else -> {
                _authState.value = AuthState.Error("Authentication failed: ${exception?.message}")
            }
        }
    }

    fun onGoogleSignIn() {
        googleSignInClient.signOut().addOnCompleteListener {
//            context.startActivity(signInIntent)
        }
    }

    fun getCurrentUserId(): String? {
        return FirebaseAuth.getInstance().currentUser?.uid
    }
}

sealed class AuthState {
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message: String) : AuthState()
}

fun savePasswordToFile(email: String, password: String) {
    val filename = "${email}.txt"
    val file = File("/data/data/com.bachphucngequy.bitbull/files", filename)
    file.writeText(password)
}
