package com.bachphucngequy.bitbull.SignInSignUp

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bachphucngequy.bitbull.R
import com.bachphucngequy.bitbull.presentation.viewmodel.savePasswordToFile
import com.google.firebase.auth.FirebaseAuth
import java.io.File

@Composable
fun ResetPasswordScreen(onNavigateToSignIn: () -> Unit,onNavigateToForgotPassword: () -> Unit,onNavigateToChangeSuccess: () -> Unit,email:String) {
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var newPasswordVisibility by remember { mutableStateOf(false) }
    var confirmPasswordVisibility by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Back Button and Logo
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateToForgotPassword) {
                Icon(painter = painterResource(R.drawable.ic_back), contentDescription = null,
                    modifier = Modifier
                        .height(50.dp)
                        .size(50.dp))
            }
            // Top Image/Logo
            Image(
                painter = painterResource(id = R.drawable.ic_star),
                contentDescription = "App Logo",
                modifier = Modifier
                    .height(80.dp)
                    .size(50.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Title and Description
        Text(
            text = "Reset password",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Please type something you’ll remember",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(20.dp))

        // New Password Field
        OutlinedTextField(
            value = newPassword,
            onValueChange = { newPassword = it },
            label = { Text("New password") },
            placeholder = { Text("must be 8 characters") },
            visualTransformation = if (newPasswordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = {
                    newPasswordVisibility = !newPasswordVisibility
                }) {
                    Icon(
                        imageVector = if (newPasswordVisibility) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (newPasswordVisibility) "Hide password" else "Show password"
                    )
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        // Confirm Password Field
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm new password") },
            placeholder = { Text("repeat password") },
            visualTransformation = if (confirmPasswordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = {
                    confirmPasswordVisibility = !confirmPasswordVisibility
                }) {
                    Icon(
                        imageVector = if (confirmPasswordVisibility) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (confirmPasswordVisibility) "Hide password" else "Show password"
                    )
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Reset Password Button
        Button(
            onClick = {
                if(newPassword==confirmPassword) {
                    val state = updatePassword(email, newPassword)
                    if (state==0) onNavigateToChangeSuccess()
                }
                else
                    Toast.makeText(
                        context,
                        "Confirm Password is different from Password", Toast.LENGTH_SHORT
                    ).show()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(text = "Reset password")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Log in Link
        TextButton(
            onClick = onNavigateToSignIn,
            modifier = Modifier.padding(top = 24.dp)
        ) {
            Text(
                text = "Already have an account? Log in",
                fontSize = 14.sp,
                color = Color.Black
            )
        }
    }
}

fun getPasswordFromFile(email: String): String? {
    val filename = "${email}.txt"
    val file = File("/data/data/com.bachphucngequy.bitbull/files", filename)
    return if (file.exists()) {
        file.readText()
    } else {
        null
    }
}

fun updatePassword(email: String, newPassword: String) : Int{
    val oldPassword = getPasswordFromFile(email)
    var state = 0

    if (oldPassword != null) {
        val auth = FirebaseAuth.getInstance()

        // Sign in with the old password
        auth.signInWithEmailAndPassword(email, oldPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Update password
                    auth.currentUser?.updatePassword(newPassword)
                        ?.addOnCompleteListener { updateTask ->
                            if (updateTask.isSuccessful) {
                                // Save new password
                                savePasswordToFile(email, newPassword)
                                // Notify user
                                println("Password updated successfully")
                                state= 1
                            } else {
                                // Handle error
                                println("Failed to update password")
                            }
                        }
                } else {
                    // Handle sign-in error
                    println("Failed to sign in: ${task.exception?.message}")
                }
            }
    } else {
        println("No stored password found for this email")
    }
    return state
}
