package com.bachphucngequy.bitbull.SignInSignUp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bachphucngequy.bitbull.presentation.viewmodel.AuthViewModel
import com.bachphucngequy.bitbull.R

@Composable
fun StartScreen(onNavigateToSignIn: () -> Unit,onNavigateToSignUp: () -> Unit,authViewModel: AuthViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(75.dp))

        // Logo/Image
        Image(
            painter = painterResource(id = R.drawable.illustration),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(275.dp)
        )

        // Title
        Text(
            text = "Explore the app",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(top = 32.dp)
        )

        // Subtitle
        Text(
            text = "Now your finances are in one place\n        and always under control",
            fontSize = 16.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 8.dp)
        )

        // Sign In Button
        Button(
            onClick = onNavigateToSignIn,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            modifier = Modifier
                .padding(top = 30.dp)
                .fillMaxSize(0.7f)
        ) {
            Text(text = "Sign In")
        }

        // Create Account Button
        Button(
            onClick = {
                authViewModel.signout()
                onNavigateToSignUp.invoke()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxSize(0.7f)
                .border(1.dp, Color.Black)
                .background(Color.White)
        ) {
            Text(text = "Create account")
        }
    }
}