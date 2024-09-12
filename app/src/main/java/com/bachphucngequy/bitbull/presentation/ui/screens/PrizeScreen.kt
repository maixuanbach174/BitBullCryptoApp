package com.bachphucngequy.bitbull.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrizeScreen(onNavigateToUserAccount: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Prize Station") },
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
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            PointsAndVouchersCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                item {
                    Text(
                        "Receive Rewards",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }

                items(getMissions()) { mission ->
                    MissionCard(mission)
                }
            }
        }
    }
}

@Composable
fun PointsAndVouchersCard(modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                PointsSection()
                VouchersSection()
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Rewards Store >", color = MaterialTheme.colorScheme.primary)
                Text("My Vouchers >", color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
fun PointsSection() {
    Column {
        Text("⭐ Points", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
        Text("0", fontWeight = FontWeight.Bold, fontSize = 24.sp)
    }
}

@Composable
fun VouchersSection() {
    Column {
        Text("🎟️ Vouchers", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
        Text("0", fontWeight = FontWeight.Bold, fontSize = 24.sp)
    }
}

@Composable
fun MissionCard(mission: Mission) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(mission.title, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(mission.reward, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(mission.progress, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                Text(mission.timeLeft, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { /* Handle mission action */ },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Do Mission")
            }
        }
    }
}

data class Mission(
    val title: String,
    val reward: String,
    val progress: String,
    val timeLeft: String
)

fun getMissions(): List<Mission> {
    return listOf(
        Mission(
            "Cryptocurrency transaction worth at least 10 USD",
            "50 USDT Transaction Fee Refund Voucher",
            "Progress: 0/10 USDT",
            "Time left: 09D : 22H : 00M"
        ),
        Mission(
            "Buy or send cryptocurrency worth at least 10 USD",
            "30 USDT Transaction Fee Refund Voucher",
            "Progress: 0/10 USDT",
            "Time left: 09D : 22H : 00M"
        ),
        Mission(
            "Complete KYC",
            "20 USDT Transaction Fee Refund Voucher",
            "Progress: 0/1",
            "Time left: 07D : 12H : 00M"
        ),
        Mission(
            "Refer friends",
            "15 USDT for each referral",
            "Progress: 0/5 people",
            "Time left: 30D : 00H : 00M"
        )
    )
}