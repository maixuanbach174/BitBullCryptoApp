package com.bachphucngequy.bitbull.SignInSignUp

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import com.bachphucngequy.bitbull.presentation.viewmodel.AuthViewModel
import com.bachphucngequy.bitbull.R
//import papaya.`in`.sendmail.SendMail

@Composable
fun SignInPhoneScreen(onNavigateToSignIn: () -> Unit,onNavigateToEnterCode: (String,String) -> Unit,authViewModel: AuthViewModel) {
    var country by remember { mutableStateOf("France") }
    var email by remember { mutableStateOf("") }
    var syncContacts by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        // Back Button and Logo
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateToSignIn) {
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
        Text(text = "Log in", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Please confirm your email so that code would be sent for signing in", fontSize = 14.sp, color = Color.Gray)

//        Spacer(modifier = Modifier.height(20.dp))

        // Country Selector (Placeholder)
//        OutlinedTextField(
//            value = country,
//            onValueChange = { country = it },
//            label = { Text("Country") },
//            singleLine = true,
//            modifier = Modifier.fillMaxWidth(),
//            readOnly = true,
//            trailingIcon = {
//                Icon(painter = painterResource(R.drawable.ic_france), contentDescription = null)
//            }
//        )

        Spacer(modifier = Modifier.height(16.dp))

        // Phone Number Input
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("@gmail.com") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Sync Contacts Toggle
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Sync Contacts", fontSize = 14.sp)
            Spacer(modifier = Modifier.weight(1f))
            Switch(
                checked = syncContacts,
                onCheckedChange = { syncContacts = it }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Continue Button
        Button(
            onClick = {
                if(email.isNotEmpty()) {
                    val oldPassword = getPasswordFromFile(email)
                    if (oldPassword != null) {
                        val otp = randomSix(email).toString()
                        onNavigateToEnterCode(otp, email)
                    }
                    else {
                        Toast.makeText(
                            context,
                            "Account containing email is not hashing stored on phone",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                else{
                    Toast.makeText(
                        context,
                        "Email can't be empty",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Continue")
        }
    }
}

fun randomSix(email:String) : Int{
    val random=(100000..999999).random()
    var mail= SendMail("nguyentrongquy0978172149@gmail.com","ofvjkcqqckngtrxh",email,"Pitbull Trading app's OTP for signing in",
        "Your OTP is \n -> $random")
    mail.execute()
    return random
}