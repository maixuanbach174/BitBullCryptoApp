package com.bachphucngequy.bitbull.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.bachphucngequy.bitbull.presentation.ui.components.home.getProductResource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CryptoWalletScreen(
    onNavigateToUserAccount: () -> Unit
) {
    val firestore = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val currentUserId = auth.currentUser?.uid

    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Crypto", "Fiat")
    var hideZeroBalances by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val cryptoList = remember { MutableStateFlow<List<Crypto>>(emptyList()) }
    val totalAssetUSD = remember { MutableStateFlow(0.0) }
    val totalAssetBTC = remember { MutableStateFlow(0.0) }

    LaunchedEffect(Unit) {
        if (currentUserId != null) {
            fetchUserCryptoData(firestore, currentUserId, cryptoList, totalAssetUSD, totalAssetBTC)
        } else {
            // Handle the case where the user is not logged in
            // You might want to navigate to a login screen or show an error message
        }
    }

    val cryptoListState by cryptoList.collectAsState()
    val totalAssetUSDState by totalAssetUSD.collectAsState()
    val totalAssetBTCState by totalAssetBTC.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Top bar
        TopAppBar(
            title = { Text("Funding Account", color = Color.White) },
            navigationIcon = {
                IconButton(onClick = onNavigateToUserAccount) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
            },
            actions = {
                IconButton(onClick = { /* Handle info action */ }) {
                    Icon(Icons.Default.Info, contentDescription = "Info", tint = Color.White)
                }
                IconButton(onClick = { /* Handle refresh action */ }) {
                    Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = Color.White)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
        )

        // Total Assets
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Total Assets", color = Color.Gray)
                Text("${String.format("%.2f", totalAssetUSDState)} USD", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text("≈ ${String.format("%.8f", totalAssetBTCState)} BTC", color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Available Balance", color = Color.Gray)
                Text("${String.format("%.2f", totalAssetUSDState)} USD ≈ ${String.format("%.8f", totalAssetBTCState)} BTC", color = Color.White)
            }
        }

        // HODL USDe Banner
        Button(
            onClick = { /* Handle HODL action */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C2C2C))
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AttachMoney, contentDescription = null, tint = Color.White)
                Text("HODL USDe to Enjoy Up to 10% APR!", color = Color.White)
                Spacer(modifier = Modifier.weight(1f))
                Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color.White)
            }
        }

        // Action Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ActionButton(text = "Deposit", icon = Icons.Default.Add)
            ActionButton(text = "Withdraw", icon = Icons.Default.Remove)
            ActionButton(text = "Transfer", icon = Icons.Default.Send)
            ActionButton(text = "Convert", icon = Icons.Default.SwapHoriz)
        }

        // Tabs
        TabRow(
            selectedTabIndex = selectedTab,
            contentColor = Color.White,
            containerColor = Color.Black,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = Color.White
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    selectedContentColor = Color.White,
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
                    checkedColor = Color.White,
                    uncheckedColor = Color.Gray
                )
            )
            Text("Hide zero balances", color = Color.White, modifier = Modifier.weight(1f))
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search", color = Color.Gray) },
                modifier = Modifier.width(120.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    focusedBorderColor = Color.White,
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
    cryptoList: MutableStateFlow<List<Crypto>>,
    totalAssetUSD: MutableStateFlow<Double>,
    totalAssetBTC: MutableStateFlow<Double>
) {
    firestore.collection("hold").get().addOnSuccessListener { querySnapshot ->
        val userCryptos = mutableListOf<Crypto>()
        var totalUSD = 0.0

        for (document in querySnapshot.documents) {
            if (document.getString("userID") == currentUserId) {
                val currency = document.getString("currency") ?: continue
                val amount = document.getDouble("amount") ?: 0.0
                val randomUsdRate = Random.nextDouble(0.1, 1000.0)
                val usdValue = amount * randomUsdRate
                totalUSD += usdValue

                userCryptos.add(
                    Crypto(
                        symbol = currency,
                        name = getCryptoName(currency),
                        balance = amount.toString(),
                        value = "≈ ${String.format("%.2f", usdValue)} USD",
                        usdRate = randomUsdRate
                    )
                )
            }
        }

        cryptoList.value = userCryptos
        totalAssetUSD.value = totalUSD

        // Calculate BTC equivalent
        val btcCrypto = userCryptos.find { it.symbol == "BTC" }
        totalAssetBTC.value = if (btcCrypto != null) {
            totalUSD / btcCrypto.usdRate
        } else {
            totalUSD / Random.nextDouble(20000.0, 60000.0) // Random BTC price if user doesn't have BTC
        }
    }.addOnFailureListener { exception ->
        // Handle error
        println("Error fetching data: ${exception.message}")
    }
}

@Composable
fun ActionButton(text: String, icon: ImageVector) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(
            onClick = { /* Handle action */ },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C2C2C))
        ) {
            Icon(icon, contentDescription = null, tint = Color.White)
        }
        Text(text, color = Color.White)
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
            Text(crypto.symbol, fontWeight = FontWeight.Bold, color = Color.White)
            Text(crypto.name, color = Color.Gray)
        }
        Spacer(modifier = Modifier.weight(1f))
        Column(horizontalAlignment = Alignment.End) {
            Text(crypto.balance, fontWeight = FontWeight.Bold, color = Color.White)
            Text(crypto.value, color = Color.Gray)
        }
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
        Icon(fiat.icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(40.dp))
        Column(modifier = Modifier.padding(start = 16.dp)) {
            Text(fiat.symbol, fontWeight = FontWeight.Bold, color = Color.White)
            Text(fiat.name, color = Color.Gray)
        }
        Spacer(modifier = Modifier.weight(1f))
        Column(horizontalAlignment = Alignment.End) {
            Text(fiat.balance, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

data class Crypto(val symbol: String, val name: String, val balance: String, val value: String, val usdRate: Double)
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
    return com.bachphucngequy.bitbull.data.entity.Crypto.values().find { crypto -> crypto.code == symbol }?.fullName ?: "Unknown"
}