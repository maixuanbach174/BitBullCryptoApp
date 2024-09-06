package com.bachphucngequy.bitbull.SignInSignUp

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
//import papaya.`in`.sendmail.SendMail
//import javax.mail.MessagingException

//import java.util.*
//import javax.mail.*
//import javax.mail.internet.InternetAddress
//import javax.mail.internet.MimeMessage
//import kotlin.concurrent.thread

@Composable
fun ForgotPasswordScreen(onNavigateToEnterCodePassword: (String,String) -> Unit,onNavigateToSignIn: () -> Unit) {
    var email by remember { mutableStateOf("") }
    val context = LocalContext.current
    val firebaseAuth = FirebaseAuth.getInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Forgot password?",
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            color = Color.Black,
            modifier = Modifier.padding(0.dp)
        )

        Text(
            text = "Don’t worry! It happens. Please enter the email associated with your account.",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        BasicTextField(
            value = email,
            onValueChange = { email = it },
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .border(1.dp, Color.Gray)
                        .padding(12.dp)
                ) {
                    if (email.isEmpty()) {
                        Text(
                            text = "Enter your email address",
                            color = Color.Gray
                        )
                    }
                    innerTextField()
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
//                if(email.isNotEmpty()) {
//                    try {
//                        val oldPassword = getPasswordFromFile(email)
//                        if (oldPassword != null) {
//                            // Check if the email is associated with an existing user
//                            firebaseAuth.fetchSignInMethodsForEmail(email)
//                                .addOnCompleteListener { fetchTask ->
//                                    if (fetchTask.isSuccessful) {
////                            val signInMethods = fetchTask.result?.signInMethods ?: emptyList()
////                            if (signInMethods.isNotEmpty()) {
//                                        val otp =
//                                            random(email).toString()
//                                        onNavigateToEnterCodePassword(otp, email)
////                            }
////                            else {
////                                // Email is not associated with any account
////                                Toast.makeText(context, "No account associated with this email.", Toast.LENGTH_SHORT).show()
////                            }
//                                    } else {
//                                        // The operation failed
//                                        val exception = fetchTask.exception
//                                        Toast.makeText(
//                                            context,
//                                            "Failed to check email: ${exception?.message}",
//                                            Toast.LENGTH_SHORT
//                                        ).show()
//                                    }
//                                }
//                        }
//                        else {
//                            Toast.makeText(
//                                context,
//                                "Account containing email is not hashing stored on phone",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                    } catch (e: MessagingException) {
//                        onError(e)
//                    }
//                }
//                else{
//                    Toast.makeText(
//                        context,
//                        "Email can't be empty",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(text = "Send code")
        }

        Spacer(modifier = Modifier.height(24.dp))

        TextButton(
            onClick = onNavigateToSignIn,
            modifier = Modifier.padding(top = 24.dp)
        ) {
            Text(
                text = "Remember password? Log in",
                fontSize = 14.sp,
                color = Color.Black,
                textDecoration = TextDecoration.Underline
            )
        }
    }
}

//fun onError(e: MessagingException) {
//    println("Failed to send email: ${e}")
//}

//fun random(email:String) : Int{
//    val random=(1000..9999).random()
//    var mail= SendMail("nguyentrongquy0978172149@gmail.com","ofvjkcqqckngtrxh",email,"Pitbull Trading app's OTP for resetting password",
//        "Your OTP is \n -> $random")
//    mail.execute()
//    return random
//}