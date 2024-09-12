package com.bachphucngequy.bitbull.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackScreen(onNavigateToUserAccount: () -> Unit) {
    val feedbackOptions = listOf(
        "Difficulties with order execution",
        "App crashes during market volatility",
        "Inaccurate real-time price updates",
        "Issues with deposit/withdrawal processes",
        "Lack of advanced charting tools",
        "Insufficient educational resources for beginners",
        "Poor customer support response time",
        "Limited asset variety for trading",
        "Complicated account verification process",
        "Delayed notifications for trade executions",
        "Unresponsive mobile interface",
        "High fees on small transactions",
        "Inconsistent app performance across devices",
        "Account security concerns",
        "Limited payment options for deposits",
        "Frequent login issues",
        "Lack of dark mode for the app interface",
        "Unclear fee structures",
        "No demo trading option",
        "Lack of news integration for market updates"
    )

    var selectedOptions by remember { mutableStateOf(emptySet<String>()) }
    val context = LocalContext.current

    var userName by remember { mutableStateOf("") }
    var userEmail by remember { mutableStateOf("") }

    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()

    LaunchedEffect(Unit) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            userEmail = currentUser.email ?: ""
            firestore.collection("AppUsers").document(currentUser.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        userName = document.getString("name") ?: ""
                    }
                }
                .addOnFailureListener { e ->
                    println("Error fetching user data: ${e.message}")
                }
        }
    }

    Scaffold(
        topBar = {
            androidx.compose.material3.TopAppBar(
                title = { androidx.compose.material3.Text("Feedback") },
                navigationIcon = {
                    androidx.compose.material3.IconButton(onClick = { onNavigateToUserAccount() }) {
                        androidx.compose.material3.Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                    titleContentColor = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerpadding ->
        Column(
            modifier = Modifier
                .padding(innerpadding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Text(
                    "Response on quality of App",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    "The issues you select will be automatically included in the content of the email sent to us.",
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                feedbackOptions.forEach { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .clickable {
                                selectedOptions = if (option in selectedOptions) {
                                    selectedOptions - option
                                } else {
                                    selectedOptions + option
                                }
                            }
                    ) {
                        Checkbox(
                            checked = option in selectedOptions,
                            onCheckedChange = null // Remove the onCheckedChange here
                        )
                        Text(
                            text = option,
                            style = MaterialTheme.typography.body1,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onNavigateToUserAccount,
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                ) {
                    Text("Cancel")
                }
                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:")
                            putExtra(Intent.EXTRA_EMAIL, arrayOf("nguyentrongquy0978172149@gmail.com"))
                            putExtra(Intent.EXTRA_SUBJECT, "Feedback on Bitbull Trading App")
                            putExtra(Intent.EXTRA_TEXT, """
                                Dear Bitbull Trading App Support Team,

                                Please address the following issues regarding the quality of service:

                                - Customer Name: $userName
                                - Phone Number: [User's Phone Number]
                                - Account's Email: $userEmail

                                Issues:
                                ${selectedOptions.joinToString("\n")}

                                Detailed description of the issue:
                                [User can add more details here]

                                Thank you for your attention to these matters.

                                Best regards,
                                $userName
                            """.trimIndent())
                        }
                        context.startActivity(Intent.createChooser(intent, "Send Email"))
                    },
                    enabled = selectedOptions.isNotEmpty(),
                    modifier = Modifier.weight(1f).padding(start = 8.dp)
                ) {
                    Text("Send email")
                }
            }
        }
    }
}