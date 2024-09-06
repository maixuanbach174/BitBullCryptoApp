package com.bachphucngequy.bitbull.SignInSignUp

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bachphucngequy.bitbull.R

@Composable
fun EnterCodePasswordScreen(onNavigateToForgotPassword: () -> Unit,onNavigateToResetPassword: (String) -> Unit,otp: String,email: String) {
    var code by remember { mutableStateOf("") }
    var resendTimer by remember { mutableStateOf(60) }

    // Timer Logic for Resend Button
    LaunchedEffect(resendTimer) {
        if (resendTimer > 0) {
            kotlinx.coroutines.delay(1000L)
            resendTimer--
        }
    }

    val context = LocalContext.current
    var otpcur=otp

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
        Text(text = "Please check your email", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "We’ve sent a code to",
            fontSize = 14.sp,
            color = Color.Gray
        )
        Text(
            text = email,
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Code Input Fields
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            for (i in 0 until 4) {
                OutlinedTextField(
                    value = if (i < code.length) code[i].toString() else "",
                    onValueChange = {
                        if (it.length <= 1) {
                            code = code.take(i) + it + code.drop(i + 1)
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .width(48.dp)
                        .padding(4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Verify Button
        Button(
            onClick = {
                if (code == otpcur) {
                    val email2=email
                    onNavigateToResetPassword(email2)
                } else {
                    Toast.makeText(context, "OTP is not correct $otpcur", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(text = "Verify")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Resend Code and Timer
        TextButton(
            onClick = {
//                if (resendTimer == 0) {
//                    // Handle resend code action
//                    resendTimer = 60
//                    otpcur = random(email).toString()
//                }
            },
            enabled = resendTimer == 0
        ) {
            Text(
                text = if (resendTimer > 0) "Send code again  00:$resendTimer" else "Send code again",
                color = if (resendTimer > 0) Color.Gray else MaterialTheme.colorScheme.primary
            )
        }
    }
}