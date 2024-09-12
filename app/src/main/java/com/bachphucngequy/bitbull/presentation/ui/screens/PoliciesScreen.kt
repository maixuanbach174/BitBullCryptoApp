package com.bachphucngequy.bitbull.presentation.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PoliciesScreen(onNavigateToUserAccount: () -> Unit) {
    val policies = remember {
        listOf(
            PolicyItem(
                title = "Terms and Conditions",
                content = "Our terms and conditions outline the rules and regulations for using our trading platform. This includes user responsibilities, account management, trading procedures, and our liability limitations."
            ),
            PolicyItem(
                title = "Privacy Policy",
                content = "We are committed to protecting your personal and financial information. Our privacy policy details how we collect, use, and safeguard your data, including trading history, personal details, and financial information."
            ),
            PolicyItem(
                title = "Trading Rules",
                content = "Our trading rules govern how trades are executed, including order types, market hours, margin requirements, and position limits. These rules ensure fair and orderly trading for all users."
            ),
            PolicyItem(
                title = "Dispute Resolution",
                content = "In case of any disputes regarding trades, account issues, or platform functionality, our dispute resolution process provides a fair and transparent method for addressing and resolving conflicts."
            ),
            PolicyItem(
                title = "Security Measures",
                content = "We employ state-of-the-art security measures to protect your account and trades. This includes two-factor authentication, encryption of sensitive data, and regular security audits to ensure the safety of your assets and information."
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Terms and Policies") },
                navigationIcon = {
                    IconButton(onClick = onNavigateToUserAccount) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            items(policies) { policy ->
                ExpandablePolicyItem(policy)
            }
        }
    }
}

@Composable
fun ExpandablePolicyItem(policy: PolicyItem) {
    var expanded by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = policy.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        Icons.Default.KeyboardArrowDown,
                        contentDescription = if (expanded) "Collapse" else "Expand",
                        modifier = Modifier.rotate(rotationState)
                    )
                }
            }
            AnimatedVisibility(visible = expanded) {
                Text(
                    text = policy.content,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

data class PolicyItem(val title: String, val content: String)