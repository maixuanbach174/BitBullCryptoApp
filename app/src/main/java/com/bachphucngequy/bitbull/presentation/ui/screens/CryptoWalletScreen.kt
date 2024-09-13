package com.bachphucngequy.bitbull.presentation.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.bachphucngequy.bitbull.R
import com.bachphucngequy.bitbull.presentation.ui.components.home.getProductResource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CryptoWalletScreen(
    onNavigateToUserAccount: () -> Unit,
    onNavigateToDeposit: () -> Unit,
    onNavigateToWithdraw: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToChange: () -> Unit
) {
    val firestore = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val currentUserId = auth.currentUser?.uid

    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Crypto", "Fiat")
    var hideZeroBalances by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val cryptoList = remember { MutableStateFlow<List<Crypto>>(emptyList()) }

    LaunchedEffect(Unit) {
        if (currentUserId != null) {
            fetchUserCryptoData(firestore, currentUserId, cryptoList)
        } else {
            // Handle the case where the user is not logged in
            // You might want to navigate to a login screen or show an error message
        }
    }

    val cryptoListState by cryptoList.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .navigationBarsPadding()
    ) {
        // Top bar
        TopAppBar(
            title = { Text("Spot Account", color = Color.Black) },
            navigationIcon = {
                IconButton(onClick = onNavigateToUserAccount) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.Black)
                }
            },
            actions = {
                IconButton(onClick = { /* Handle info action */ }) {
                    Icon(Icons.Default.Info, contentDescription = "Info", tint = Color.Black)
                }
                IconButton(onClick = { /* Handle refresh action */ }) {
                    Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = Color.Black)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
        )

        // App Logo
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.mylogo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        // HODL USDe Banner
        Button(
            onClick = { /* Handle HODL action */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AttachMoney, contentDescription = null, tint = Color.Black)
                Text("HODL USDe to Enjoy Up to 10% APR!", color = Color.Black)
                Spacer(modifier = Modifier.weight(1f))
                Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color.Black)
            }
        }

        // Action Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ActionButton(text = "Deposit", icon = Icons.Default.Add, onNavigateToDeposit)
            ActionButton(text = "Withdraw", icon = Icons.Default.Remove, onNavigateToWithdraw)
            ActionButton(text = "History", icon = Icons.Default.History, onNavigateToHistory)
            ActionButton(text = "Convert", icon = Icons.Default.SwapHoriz, onNavigateToChange)
        }

        // Tabs
        TabRow(
            selectedTabIndex = selectedTab,
            contentColor = Color.Black,
            containerColor = Color.White,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = Color.Black
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    selectedContentColor = Color.Black,
                    unselectedContentColor = Color.Gray
                )
            }
        }

        // Checkbox and Search
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = hideZeroBalances,
                onCheckedChange = { hideZeroBalances = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = Color.Black,
                    uncheckedColor = Color.Gray
                )
            )
            Text("Hide zero balances", color = Color.Black, modifier = Modifier.weight(1f))
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search", color = Color.Gray) },
                modifier = Modifier.width(120.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Gray
                )
            )
        }

        // Crypto or Fiat List
        LazyColumn {
            if (selectedTab == 0) { // Crypto
                items(cryptoListState.filter {
                    (if (hideZeroBalances) it.balance.toDoubleOrNull() ?: 0.0 > 0.0 else true) &&
                            it.name.contains(searchQuery, ignoreCase = true)
                }) { crypto ->
                    CryptoListItem(crypto)
                }
            } else { // Fiat
                items(getFiatList().filter {
                    (if (hideZeroBalances) it.balance.toDoubleOrNull() ?: 0.0 > 0.0 else true) &&
                            it.name.contains(searchQuery, ignoreCase = true)
                }) { fiat ->
                    FiatListItem(fiat)
                }
            }
        }
    }
}

fun fetchUserCryptoData(
    firestore: FirebaseFirestore,
    currentUserId: String,
    cryptoList: MutableStateFlow<List<Crypto>>
) {
    firestore.collection("hold").get().addOnSuccessListener { querySnapshot ->
        val userCryptos = mutableListOf<Crypto>()

        for (document in querySnapshot.documents) {
            if (document.getString("userID") == currentUserId) {
                val currency = document.getString("currency") ?: continue
                val amount = document.getDouble("amount") ?: 0.0

                userCryptos.add(
                    Crypto(
                        symbol = currency,
                        name = getCryptoName(currency),
                        balance = amount.toString()
                    )
                )
            }
        }

        cryptoList.value = userCryptos
    }.addOnFailureListener { exception ->
        // Handle error
        println("Error fetching data: ${exception.message}")
    }
}

@Composable
fun ActionButton(text: String, icon: ImageVector, onNavigateTo: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(
            onClick = { onNavigateTo() },
            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
        ) {
            Icon(icon, contentDescription = null, tint = Color.Black)
        }
        Text(text, color = Color.Black)
    }
}

@Composable
fun CryptoListItem(crypto: Crypto) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = getProductResource(crypto.symbol, crypto.name),
            contentDescription = null,
            modifier = Modifier.size(40.dp)
        )
        Column(modifier = Modifier.padding(start = 16.dp)) {
            Text(crypto.symbol, fontWeight = FontWeight.Bold, color = Color.Black)
            Text(crypto.name, color = Color.Gray)
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(crypto.balance, fontWeight = FontWeight.Bold, color = Color.Black)
    }
}

@Composable
fun FiatListItem(fiat: Fiat) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(fiat.icon, contentDescription = null, tint = Color.Black, modifier = Modifier.size(40.dp))
        Column(modifier = Modifier.padding(start = 16.dp)) {
            Text(fiat.symbol, fontWeight = FontWeight.Bold, color = Color.Black)
            Text(fiat.name, color = Color.Gray)
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(fiat.balance, fontWeight = FontWeight.Bold, color = Color.Black)
    }
}

data class Crypto(val symbol: String, val name: String, val balance: String)
data class Fiat(val symbol: String, val name: String, val balance: String, val icon: ImageVector)

fun getFiatList(): List<Fiat> {
    return listOf(
        Fiat("USD", "US Dollar", "0.00", Icons.Default.AttachMoney),
        Fiat("EUR", "Euro", "0.00", Icons.Default.Euro),
        Fiat("GBP", "British Pound", "0.00", Icons.Default.CurrencyPound),
        Fiat("JPY", "Japanese Yen", "0.00", Icons.Default.MonetizationOn)
    )
}

fun getCryptoName(symbol: String): String {
    return com.bachphucngequy.bitbull.data.entity.Crypto.values().find { crypto -> crypto.code == symbol }?.fullName ?: "Tether"
}