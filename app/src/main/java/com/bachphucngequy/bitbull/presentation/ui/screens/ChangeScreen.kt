package com.bachphucngequy.bitbull.presentation.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeScreen(onNavigateToUserAccount: () -> Unit) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    var fromCoin by remember { mutableStateOf("BTC") }
    var toCoin by remember { mutableStateOf("ETH") }
    var fromAmount by remember { mutableStateOf("") }
    var toAmount by remember { mutableStateOf("") }
    var showCoinSelection by remember { mutableStateOf(false) }
    var selectingTopCoin by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Exchange") },
                navigationIcon = {
                    IconButton(onClick = onNavigateToUserAccount) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TabRow(selectedTabIndex = selectedTabIndex) {
                Tab(
                    selected = selectedTabIndex == 0,
                    onClick = { selectedTabIndex = 0 },
                    text = { Text("Market") }
                )
                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 1 },
                    text = { Text("Limit") }
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                if (selectedTabIndex == 0) { // Market
                    Text("From", style = MaterialTheme.typography.titleMedium)
                    Row(
                        modifier = Modifier.clickable {
                            showCoinSelection = true
                            selectingTopCoin = true
                        },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(getCoinIcon(fromCoin), contentDescription = fromCoin)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(fromCoin)
                        Spacer(modifier = Modifier.weight(1f))
                        Text("▼")
                    }
                    NumberTextField(
                        value = fromAmount,
                        onValueChange = {
                            fromAmount = it
                            toAmount = if (it.isNotEmpty() && it != "0") Random.nextDouble(0.00001,1000000.0).toString() else ""
                        },
                        label = { Text("Amount") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Text("To", style = MaterialTheme.typography.titleMedium)
                    Row(
                        modifier = Modifier.clickable {
                            showCoinSelection = true
                            selectingTopCoin = false
                        },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(getCoinIcon(toCoin), contentDescription = toCoin)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(toCoin)
                        Spacer(modifier = Modifier.weight(1f))
                        Text("▼")
                    }
                    NumberTextField(
                        value = toAmount,
                        onValueChange = { },
                        label = { Text("Amount") },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                } else { // Limit

                }
            }

            Button(
                onClick = { /* Perform exchange */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Transaction")
            }
        }
    }

    if (showCoinSelection) {
        CoinSelectionDialog(
            onDismiss = { showCoinSelection = false },
            onCoinSelected = { coin ->
                if (selectingTopCoin) fromCoin = coin else toCoin = coin
                showCoinSelection = false
            }
        )
    }
}

@Composable
fun NumberTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            if (newValue.isEmpty() || newValue.toDoubleOrNull() != null) {
                onValueChange(newValue)
            }
        },
        label = label,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = modifier,
        readOnly = readOnly
    )
}

@Composable
fun CoinSelectionDialog(onDismiss: () -> Unit, onCoinSelected: (String) -> Unit) {
    val coins = listOf(
        "BTC", "ETH", "USDT", "BNB", "ADA", "XRP", "SOL", "DOT", "DOGE", "AVAX",
        "LUNA", "MATIC", "UNI", "LINK", "ALGO", "LTC", "BCH", "XLM", "VET", "ATOM",
        "FTT", "TRX", "FIL", "XTZ", "AAVE", "EOS", "CAKE", "THETA", "XMR", "EGLD"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Coin") },
        text = {
            LazyColumn {
                items(coins) { coin ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onCoinSelected(coin) }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(getCoinIcon(coin), contentDescription = coin)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(coin)
                    }
                }
            }
        },
        confirmButton = { }
    )
}

fun getCoinIcon(coin: String) = when (coin) {
    "BTC" -> Icons.Default.AttachMoney
    "ETH" -> Icons.Default.Euro
    "USDT" -> Icons.Default.MonetizationOn
    "BNB" -> Icons.Default.CurrencyBitcoin
    "ADA" -> Icons.Default.CurrencyExchange
    "XRP" -> Icons.Default.CurrencyYen
    "SOL" -> Icons.Default.CurrencyPound
    "DOT" -> Icons.Default.CurrencyRuble
    "DOGE" -> Icons.Default.CurrencyLira
    "AVAX" -> Icons.Default.Money
    else -> Icons.Default.Token
}