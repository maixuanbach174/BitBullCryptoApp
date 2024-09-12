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
import androidx.compose.ui.unit.dp

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
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Checkbox(
                            checked = option in selectedOptions,
                            onCheckedChange = { checked ->
                                selectedOptions = if (checked) {
                                    selectedOptions + option
                                } else {
                                    selectedOptions - option
                                }
                            }
                        )
                        Text(
                            text = option,
                            style = MaterialTheme.typography.body1,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }

            // Button row is now outside the scrollable area
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
                    onClick = onNavigateToUserAccount,
                    enabled = selectedOptions.isNotEmpty(),
                    modifier = Modifier.weight(1f).padding(start = 8.dp)
                ) {
                    Text("Send email")
                }
            }
        }
    }
}