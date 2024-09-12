package com.bachphucngequy.bitbull.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReferScreen(onNavigateToUserAccount: () -> Unit) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Step 1", "Step 2", "Step 3")
    val clipboardManager: ClipboardManager = LocalClipboardManager.current

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Refer") },
            actions = {
                IconButton(onClick = { /* TODO: Handle more options */ }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "More")
                }
                IconButton(onClick = { onNavigateToUserAccount() }) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )

        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Invite friends",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                "You both earn 100 USD",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Refer friends and help them deposit over 50 USD to both receive a transaction fee waiver voucher worth 100 USD. Learn more",
                fontSize = 14.sp
            )
        }

        TabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) }
                )
            }
        }

        when (selectedTabIndex) {
            0 -> StepContent(
                "Share referral link with friends",
                Icons.Default.Share
            )
            1 -> StepContent(
                "Invite friends to register and deposit over 50 USD within 14 days of registration",
                Icons.Default.AccountBalance
            )
            2 -> StepContent(
                "You both receive a 100 USD transaction fee waiver voucher",
                Icons.Default.CardGiftcard
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Column(modifier = Modifier.padding(16.dp)) {
            Text("Invite now", fontWeight = FontWeight.Bold)
            val referralCode = "CPA_00X7W9OGBM"
            OutlinedTextField(
                value = referralCode,
                onValueChange = {},
                label = { Text("Referral code") },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = {
                        clipboardManager.setText(AnnotatedString(referralCode))
                    }) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "Copy")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            val referralLink = "https://www...OGBM"
            OutlinedTextField(
                value = referralLink,
                onValueChange = {},
                label = { Text("Referral link") },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = {
                        clipboardManager.setText(AnnotatedString(referralLink))
                    }) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "Copy")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Share to", fontWeight = FontWeight.Bold)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SharingOption("Facebook", Icons.Default.Facebook)
                SharingOption("Contacts", Icons.Default.Contacts)
                SharingOption("Copy Link", Icons.Default.Link)
                SharingOption("QR Code", Icons.Default.QrCode)
                SharingOption("More", Icons.Default.MoreHoriz)
            }
        }
    }
}

@Composable
fun StepContent(description: String, icon: ImageVector) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(100.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(description, textAlign = TextAlign.Center)
    }
}

@Composable
fun SharingOption(label: String, icon: ImageVector) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(onClick = { /* TODO: Handle sharing */ }) {
            Icon(imageVector = icon, contentDescription = label)
        }
        Text(label, fontSize = 12.sp)
    }
}