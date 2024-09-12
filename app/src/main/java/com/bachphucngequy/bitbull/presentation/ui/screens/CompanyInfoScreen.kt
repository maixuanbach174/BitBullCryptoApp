package com.bachphucngequy.bitbull.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyInfoScreen(
    onNavigateToUserAccount: () -> Unit
) {
    val uriHandler = LocalUriHandler.current
    val facebookUrl = "https://www.facebook.com/profile.php?id=61565766118892"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Company Information") },
                navigationIcon = {
                    IconButton(onClick = onNavigateToUserAccount) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            InfoSection("Company Name", "BitBull Trading Company")
            InfoSection("Address", "123 Trading Street, Financial District,\nNew York City, NY 10005, United States")
            InfoSection("Representatives", """
                Nguyen Trong Quy - Chief Executive Officer
                Mai Xuan Bach - Chief Operating Officer
                Nguyen Duy Phuc - Chief Financial Officer
                Bui Danh Nghe - Head of Trading
            """.trimIndent())
            InfoSection("Phone Number", "+1 (555) 123-4567")
            InfoSection("Email", "info@bitbulltrading.com")
            InfoSection("Tax ID Number", "87-1234567")
            InfoSection("Registration Date", "01-15-2023")
            InfoSection("Issuing Authority", "New York State Department of Financial Services")
            InfoSection("Licenses", """
                Securities Trading License No. 123456 issued by SEC on 02/01/2023
                Commodities Trading License No. 789012 issued by CFTC on 02/15/2023
            """.trimIndent())
            InfoSection("Website", "https://www.bitbulltrading.com")

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { uriHandler.openUri(facebookUrl) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Follow BitBull Trading on Facebook")
            }
        }
    }
}

@Composable
fun InfoSection(title: String, content: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = content, fontSize = 14.sp)
    }
}