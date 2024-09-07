package com.bachphucngequy.bitbull.presentation.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.bachphucngequy.bitbull.presentation.viewmodel.AuthState
import com.bachphucngequy.bitbull.presentation.viewmodel.AuthViewModel
import com.bachphucngequy.bitbull.R

@Composable
fun UserAccountScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToSignIn: () -> Unit,
    authViewModel: AuthViewModel
) {
    val authState = authViewModel.authState.observeAsState()

    LaunchedEffect(authState.value) {
        when(authState.value){
            is AuthState.Unauthenticated -> onNavigateToSignIn.invoke()
            else -> Unit
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Return icon at the top-left corner
        Icon(
            painter = painterResource(R.drawable.ic_back),
            contentDescription = "Return",
            modifier = Modifier
                .align(Alignment.TopStart)
                .size(24.dp)
                .clickable { onNavigateToHome() }, // Navigate to home
            tint = Color.Black
        )

        // Center content
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Log out button
            Button(onClick = {
                authViewModel.signout()
            }) {
                Text("Sign out")
            }
        }
    }
}
