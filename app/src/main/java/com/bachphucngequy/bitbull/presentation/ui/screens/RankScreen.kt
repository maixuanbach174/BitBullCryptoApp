package com.bachphucngequy.bitbull.presentation.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bachphucngequy.bitbull.presentation.viewmodel.AuthViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import java.util.Calendar
import java.util.concurrent.TimeUnit
import androidx.compose.ui.res.painterResource
import com.bachphucngequy.bitbull.R
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RankScreen(
    onNavigateToUserAccount: () -> Unit,
    authViewModel: AuthViewModel
) {
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    var userRank by remember { mutableStateOf<Rank?>(null) }
    var daysRegistered by remember { mutableStateOf(0) }
    var userName by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val creationTimestamp = currentUser.metadata?.creationTimestamp
            if (creationTimestamp != null) {
                val creationDate = Date(creationTimestamp)
                val today = Calendar.getInstance().time
                daysRegistered = TimeUnit.MILLISECONDS.toDays(today.time - creationDate.time).toInt()
                userRank = when {
                    daysRegistered >= 5 -> Rank.MASTER
                    daysRegistered == 4 -> Rank.DIAMOND
                    daysRegistered == 3 -> Rank.PLATINUM
                    daysRegistered == 2 -> Rank.GOLD
                    daysRegistered == 1 -> Rank.SILVER
                    else -> Rank.BRONZE
                }
            }

            // Fetch user name
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
            TopAppBar(
                title = { Text("Member Rank") },
                navigationIcon = {
                    IconButton(onClick = onNavigateToUserAccount) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Return")
                    }
                },
                actions = {
                    Image(
                        painter = painterResource(id = R.drawable.mylogo),
                        contentDescription = "App Logo",
                        modifier = Modifier
                            .size(40.dp)
                            .padding(end = 16.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Fixed header with RankBanner, UserName, and RankInfo
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = userName.ifEmpty { "Loading..." },
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                )
                RankBanner(userRank)
                RankInfo(userRank, daysRegistered)
            }

            // Scrollable content
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                item { RankBenefits(userRank) }
                item { AllRanks() }
            }
        }
    }
}

@Composable
fun RankBanner(rank: Rank?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        when (rank) {
            Rank.BRONZE -> Icon(Icons.Default.AccountCircle, contentDescription = "Bronze Rank", modifier = Modifier.size(120.dp))
            Rank.SILVER -> Icon(Icons.Default.AccountBox, contentDescription = "Silver Rank", modifier = Modifier.size(120.dp))
            Rank.GOLD -> Icon(Icons.Default.Star, contentDescription = "Gold Rank", modifier = Modifier.size(120.dp))
            Rank.PLATINUM -> Icon(Icons.Default.StarBorder, contentDescription = "Platinum Rank", modifier = Modifier.size(120.dp))
            Rank.DIAMOND -> Icon(Icons.Default.Diamond, contentDescription = "Diamond Rank", modifier = Modifier.size(120.dp))
            Rank.MASTER -> Icon(Icons.Default.EmojiEvents, contentDescription = "Master Rank", modifier = Modifier.size(120.dp))
            null -> CircularProgressIndicator()
        }
    }
}

@Composable
fun RankInfo(rank: Rank?, daysRegistered: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = rank?.name ?: "Loading...",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Member for $daysRegistered days",
            fontSize = 16.sp
        )
    }
}

@Composable
fun RankBenefits(rank: Rank?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Rank Benefits",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            when (rank) {
                Rank.BRONZE -> ListItem(headlineContent = { Text("5% Cashback on trades") }, leadingContent = { Icon(Icons.Default.MonetizationOn, contentDescription = null) })
                Rank.SILVER -> ListItem(headlineContent = { Text("7% Cashback on trades") }, leadingContent = { Icon(Icons.Default.MonetizationOn, contentDescription = null) })
                Rank.GOLD -> ListItem(headlineContent = { Text("10% Cashback on trades") }, leadingContent = { Icon(Icons.Default.MonetizationOn, contentDescription = null) })
                Rank.PLATINUM -> ListItem(headlineContent = { Text("12% Cashback on trades") }, leadingContent = { Icon(Icons.Default.MonetizationOn, contentDescription = null) })
                Rank.DIAMOND -> ListItem(headlineContent = { Text("15% Cashback on trades") }, leadingContent = { Icon(Icons.Default.MonetizationOn, contentDescription = null) })
                Rank.MASTER -> ListItem(headlineContent = { Text("20% Cashback on trades") }, leadingContent = { Icon(Icons.Default.MonetizationOn, contentDescription = null) })
                null -> ListItem(headlineContent = { Text("Loading...") })
            }
            // Add more benefits as needed
        }
    }
}

@Composable
fun AllRanks() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "All Ranks",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            RankItem(Rank.BRONZE, "0 days", Icons.Default.AccountCircle)
            RankItem(Rank.SILVER, "1 day", Icons.Default.AccountBox)
            RankItem(Rank.GOLD, "2 days", Icons.Default.Star)
            RankItem(Rank.PLATINUM, "3 days", Icons.Default.StarBorder)
            RankItem(Rank.DIAMOND, "4 days", Icons.Default.Diamond)
            RankItem(Rank.MASTER, "5+ days", Icons.Default.EmojiEvents)
        }
    }
}

@Composable
fun RankItem(rank: Rank, days: String, icon: ImageVector) {
    ListItem(
        headlineContent = { Text(rank.name) },
        supportingContent = { Text(days) },
        leadingContent = { Icon(icon, contentDescription = null) }
    )
}

enum class Rank {
    BRONZE, SILVER, GOLD, PLATINUM, DIAMOND, MASTER
}