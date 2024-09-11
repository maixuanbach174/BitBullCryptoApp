package com.bachphucngequy.bitbull.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.res.painterResource
import com.bachphucngequy.bitbull.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeNicknameScreen(
    onNavigateToUserAccount: () -> Unit
) {
    var currentNickname by remember { mutableStateOf("") }
    var newNickname by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()

    val currentUser = auth.currentUser ?: throw Exception("User not authenticated")
    val currentUserId = currentUser.uid

    LaunchedEffect(currentUserId) {
            firestore.collection("AppUsers").document(currentUserId)
                .get().addOnSuccessListener {
                    if(it!=null){
                        currentNickname = it.data?.get("name")?.toString().toString()
                        newNickname = currentNickname
                    }
                }
                .addOnFailureListener{
                    println("Error fetching user data")
                }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Change Nickname") },
                navigationIcon = {
                    IconButton(onClick = onNavigateToUserAccount) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Go to User Account")
                    }
                },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = newNickname,
                onValueChange = {
                    newNickname = it
                    errorMessage = null  // Clear error when user types
                },
                label = { Text("New Nickname") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Button(
                onClick = {
                    updateNicknameInDatabase(newNickname)
                    onNavigateToUserAccount()
                },
                enabled = newNickname != currentNickname && newNickname.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                    Text("Change Nickname")
            }
        }
    }
}

fun updateNicknameInDatabase(newNickname: String) {
    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()

    val currentUser = auth.currentUser ?: throw Exception("User not authenticated")
    val userId = currentUser.uid

    val updateMap = mapOf(
        "name" to newNickname
    )

    firestore.collection("AppUsers")
        .document(userId)
        .update(updateMap)
}