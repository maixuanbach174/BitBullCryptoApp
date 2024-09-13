package com.bachphucngequy.bitbull.SignInSignUp

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bachphucngequy.bitbull.presentation.viewmodel.AuthState
import com.bachphucngequy.bitbull.presentation.viewmodel.AuthViewModel
import com.bachphucngequy.bitbull.R
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.bachphucngequy.bitbull.firebase.user
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.firestore.auth.User


@Composable
fun SignInScreen(onNavigateToSignInPhone: () -> Unit,onNavigateToForgotPassword: () -> Unit,onNavigateToSignUp: () -> Unit,onNavigateToViewMarket: () -> Unit,authViewModel: AuthViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            authViewModel.firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            Toast.makeText(context, "Google sign-in failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Authenticated -> {
                user.fetchUserId() // Fetch the user ID when authenticated
                onNavigateToViewMarket.invoke()
            }
            is AuthState.Error -> Toast.makeText(
                context,
                (authState.value as AuthState.Error).message,
                Toast.LENGTH_SHORT
            ).show()
            else -> Unit
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(56.dp))

        // Top Image/Logo
        Image(
            painter = painterResource(id = R.drawable.ic_star),
            contentDescription = "App Logo",
            modifier = Modifier
                .height(80.dp)
                .align(Alignment.End)
                .size(50.dp)
        )

        Text(text = "Log in", fontSize = 32.sp, fontWeight = Bold)

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email address") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = null)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onNavigateToForgotPassword,modifier = Modifier.align(Alignment.End)) {
            Text(text = "Forgot password?")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                authViewModel.login(email,password)
            },
            enabled = authState.value != AuthState.Loading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Log in")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Or Login with",modifier = Modifier.align(Alignment.CenterHorizontally))

//        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), // Optional padding around the Row
            horizontalArrangement = Arrangement.SpaceBetween // Distributes space evenly between items
        ) {
            IconButton(
                onClick = { /* TODO: Handle Facebook Login */ },
//                modifier = Modifier.size(48.dp) // Adjust the size of the IconButton
                modifier = Modifier
                    .size(95.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_facebook),
                    contentDescription = "Facebook",
                    modifier = Modifier.fillMaxSize() // Adjust the size of the Image within the IconButton
                )
            }
            IconButton(
                onClick = {
                    authViewModel.onGoogleSignIn()
                    val signInIntent = authViewModel.googleSignInClient.signInIntent
                    googleSignInLauncher.launch(signInIntent)
                },
                modifier = Modifier.size(95.dp) // Adjust the size of the IconButton
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = "Google",
                    modifier = Modifier.fillMaxSize() // Adjust the size of the Image within the IconButton
                )
            }
            IconButton(
                onClick = onNavigateToSignInPhone,
                modifier = Modifier.size(95.dp) // Adjust the size of the IconButton
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_apple),
                    contentDescription = "Apple",
                    modifier = Modifier.fillMaxSize() // Adjust the size of the Image within the IconButton
                )
            }
        }


        Spacer(modifier = Modifier.height(72.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth() // Makes the Box take up the full screen
        ) {
            TextButton(
                onClick = onNavigateToSignUp,
                modifier = Modifier
                    .align(Alignment.BottomCenter) // Aligns the button at the bottom center
            ) {
                Text(text = "Don’t have an account? Sign up")
            }
        }
    }
}