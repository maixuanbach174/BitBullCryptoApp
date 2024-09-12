package com.bachphucngequy.bitbull.presentation.ui.screens
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.currentCompositionErrors
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bachphucngequy.bitbull.data.repository.CoinRepositoryImpl
import com.bachphucngequy.bitbull.domain.model.Coin
import com.bachphucngequy.bitbull.firebase.FirebaseRepository
import com.bachphucngequy.bitbull.firebase.FirestoreHelper
import com.bachphucngequy.bitbull.firebase.OrderManager
import com.bachphucngequy.bitbull.firebase.user
import com.bachphucngequy.bitbull.news.NewsScreen
import com.bachphucngequy.bitbull.news.NewsViewModel
import com.bachphucngequy.bitbull.news.api.Article
import com.bachphucngequy.bitbull.presentation.ui.components.MarketTracker.MarketStatisticHead
import com.bachphucngequy.bitbull.presentation.ui.components.MarketTracker.PartialBottomSheet
import com.bachphucngequy.bitbull.presentation.ui.components.home.TabRow
//import com.bachphucngequy.bitbull.presentation.ui.components.marketdetail.BuySellBar
import com.bachphucngequy.bitbull.presentation.ui.components.marketdetail.OrderBookUI
import com.bachphucngequy.bitbull.presentation.ui.components.marketdetail.SymbolAppBar
import com.bachphucngequy.bitbull.presentation.ui.components.marketdetail.TradingChart
import com.bachphucngequy.bitbull.presentation.ui.screens.LightThemeColors.Surface
import com.bachphucngequy.bitbull.presentation.ui.theme.Green100
import com.bachphucngequy.bitbull.presentation.ui.theme.Red100
import com.bachphucngequy.bitbull.presentation.viewmodel.CoinDetailViewModel
import com.bachphucngequy.bitbull.retrofit.RetrofitInstance

@Composable
fun MarketDetailScreen(
    onNavigateToBuySell: () -> Unit,
    onBackClick: () -> Unit,
    marketName : String = "BTC-USD",
    coin: Coin = Coin(),
    pairCoin: String = "",
    newsViewModel: NewsViewModel,
    onNavigateToNewsDetails: (Article) -> Unit,
    onFavouriteClick: () -> Unit
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val coinDetailViewModel: CoinDetailViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return CoinDetailViewModel(CoinRepositoryImpl(RetrofitInstance.coinPaprikaApi), coin.id) as T
            }
        }
    )

    val coinDetail by coinDetailViewModel.coinDetail.collectAsState()
    var showBottomSheet by remember { mutableStateOf(false) }
    var isBuy by remember { mutableStateOf(true) } // To track Buy/Sell button click
    Scaffold(
        topBar = {
            Column {
                SymbolAppBar(
                    marketName = marketName,
                    companyName = coin.name,
                    onBackClick = onBackClick,
                    onFavouriteClick = onFavouriteClick,
                    isCoinFavourite = coin.isFavourite
                )
                TabRow(tabTitles = listOf("Price", "Info", "Trading Data", "News"), onClick = { index ->
                    selectedTabIndex = index
                })
            }
        },
        bottomBar = {
            BuySellBar(
                onBuyClick = {
                    isBuy = true
                    showBottomSheet = true
                },
                onSellClick = {
                    isBuy = false
                    showBottomSheet = true
                }
            )
        }
    ) {innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedTabIndex) {
                0 -> PriceContent(
                    modifier = Modifier.fillMaxSize(),
                    symbol = pairCoin,
                )
                1 -> CoinDetailScreen(coin = coinDetail)
                2 -> TradingDataContent()
                3 -> NewsScreen(
                    newsViewModel = newsViewModel,
                    navigateToDetails = onNavigateToNewsDetails,
                    searchQuery = coin.name
                )
            }
            if (showBottomSheet) {
                OrderBottomSheet(
//                    btcCurrency = "BTC",
//                    usdtCurrency = "USDT",
//                    price = 56000.0,
                    isBuySelected = isBuy,
                    onPlaceOrderClick = { amount, isBuySelected ->
                        showBottomSheet = false
                    },
                    onDismiss = {
                        showBottomSheet = false
                    }
                )
            }
        }
    }
}

@Composable
fun BuySellBar(onBuyClick: () -> Unit, onSellClick: () -> Unit) {
    BottomAppBar(
        containerColor = Color.White,
        actions = {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(start = 10.dp)
                ) {
                    FilledTonalButton(
                        onClick = onBuyClick,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Green100
                        )
                    ) {
                        Text(
                            "Buy",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(horizontal = 30.dp)
                        )
                    }
                    FilledTonalButton(
                        onClick = onSellClick,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Red100
                        )
                    ) {
                        Text(
                            "Sell",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(horizontal = 30.dp)
                        )
                    }
                }
                Row {
                    IconButton(onClick = {}) {
                        Icon(
                            Icons.Outlined.Notifications,
                            contentDescription = "Localized description",
                        )
                    }
                    IconButton(onClick = {}) {
                        Icon(
                            Icons.Outlined.MoreVert,
                            contentDescription = "Localized description",
                        )
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderBottomSheet(

    isBuySelected: Boolean,
    onPlaceOrderClick: (String, Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    var amount by remember { mutableStateOf("") }
    var isBuy by remember { mutableStateOf(isBuySelected) }
    var availableBalance by remember { mutableStateOf(0.0) }
    var btcCurrency = "BTC"
    var usdtCurrency = "USDT"
    var price= 56000.0

    val userId = user.usid

    LaunchedEffect(userId, isBuy) {
        if (userId != null) {
            val currency = if (isBuy) usdtCurrency else btcCurrency
            FirebaseRepository.fetchAvailableBalance(
                userId = userId,
                currency = currency,
                onSuccess = { balance ->
                    availableBalance = balance
                },
                onFailure = { exception ->
                    Log.e("FirebaseRepository", "Error fetching balance", exception)
                }
            )
        }
    }

    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
        },
        sheetState = bottomSheetState
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    FilledTonalButton(
                        onClick = {
                            isBuy = true
                            amount = ""
                            val currency = usdtCurrency
                            FirebaseRepository.fetchAvailableBalance(
                                userId = userId,
                                currency = currency,
                                onSuccess = { balance ->
                                    availableBalance = balance
                                },
                                onFailure = { exception ->
                                    Log.e("FirebaseRepository", "Error fetching balance", exception)
                                }
                            )
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isBuy) Green100 else Color.Gray
                        )
                    ) {
                        Text("Buy", style = MaterialTheme.typography.bodyMedium)
                    }

                    FilledTonalButton(
                        onClick = {
                            isBuy = false
                            amount = ""
                            val currency = btcCurrency
                            FirebaseRepository.fetchAvailableBalance(
                                userId = userId,
                                currency = currency,
                                onSuccess = { balance ->
                                    availableBalance = balance
                                },
                                onFailure = { exception ->
                                    Log.e("FirebaseRepository", "Error fetching balance", exception)
                                }
                            )
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (!isBuy) Red100 else Color.Gray
                        )
                    ) {
                        Text("Sell", style = MaterialTheme.typography.bodyMedium)
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextField(
                        value = amount,
                        onValueChange = { inputAmount ->
                            if (inputAmount.toDoubleOrNull() != null && inputAmount.toDouble() <= availableBalance) {
                                amount = inputAmount
                            }
                        },
                        modifier = Modifier.weight(1f),
                        label = { Text("Amount (Max: $availableBalance)") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        )
                    )

                    Text(text = if (isBuy) usdtCurrency else btcCurrency)

                    FilledTonalButton(
                        onClick = {
                            amount = availableBalance.toString()
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                    ) {
                        Text("Max")
                    }
                }

                val context = LocalContext.current

                FilledTonalButton(
                    onClick = {
                        if (amount.isBlank() || amount.toDoubleOrNull() == null || amount.toDouble() <= 0.0) {
                            Toast.makeText(context, "Please enter a valid amount!", Toast.LENGTH_SHORT).show()
                            return@FilledTonalButton
                        }
                        FirebaseRepository.fetchAvailableBalance(
                            userId = userId,
                            currency = "BTC",
                            onSuccess = { currentAmountBuyCurrency ->
                                // Step 2: Fetch Sell Currency Balance
                                FirebaseRepository.fetchAvailableBalance(
                                    userId = userId,
                                    currency = "USDT",
                                    onSuccess = { currentAmountSellCurrency ->
                                        val (newAmountBuyCurrency, newAmountSellCurrency) = OrderManager.handleOrder(
                                            currentAmountBuyCurrency = currentAmountBuyCurrency,
                                            currentAmountSellCurrency = currentAmountSellCurrency,
                                            inputAmount = amount.toDouble(),
                                            price = price,
                                            isBuy = isBuy
                                        )
                                        Log.d("OrderDebug1", "1: $currentAmountBuyCurrency")
                                        Log.d("OrderDebug1", "2: $currentAmountSellCurrency")
                                        FirebaseRepository.updateBalances(
                                            userId = userId,
                                            buyCurrency = "BTC",
                                            sellCurrency = "USDT",
                                            newAmountBuyCurrency = newAmountBuyCurrency,
                                            newAmountSellCurrency = newAmountSellCurrency,
                                            onSuccess = {
                                                Toast.makeText(context, "Order executed successfully!", Toast.LENGTH_SHORT).show()
                                                onDismiss()
                                            },
                                            onFailure = { exception ->
                                                Toast.makeText(context, "Order failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                                            }
                                        )
                                    },
                                    onFailure = { exception ->
                                        Log.e("FirebaseRepository", "Error fetching BTC balance", exception)
                                        Toast.makeText(context, "Failed to fetch BTC balance", Toast.LENGTH_SHORT).show()
                                    }
                                )
                            },
                            onFailure = { exception ->
                                Log.e("FirebaseRepository", "Error fetching USDT balance", exception)
                                Toast.makeText(context, "Failed to fetch USDT balance", Toast.LENGTH_SHORT).show()
                            }
                        )
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Place Order", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}



@Composable
fun InfoContent() {
    Text("Info Content")
}

@Composable
fun TradingDataContent() {
    Text("Trading Data Content")
}

@Composable
fun SquareContent() {
    Text("Square Content")
}

@Composable
fun PriceContent(
    modifier: Modifier,
    symbol: String,
    marketName: String = "BTC-USD"
) {

    val scrollState = rememberScrollState()

    var selectedTimeframe by remember { mutableStateOf("15") }
    var selectedChartType by remember { mutableStateOf("1") }

    Column(
        modifier = modifier.verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {

        MarketStatisticHead(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp), productId = marketName)
        PartialBottomSheet(modifier = Modifier.padding(16.dp), onLineTypeClick = { selectedChartType = it }, onTimeSpanClick = { selectedTimeframe = it })
        Divider(
            thickness = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        TradingChart(
            symbol = symbol,
            selectedTimeFrame = selectedTimeframe,
            selectedChartType = selectedChartType
        )
        Divider(
            thickness = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        OrderBookUI(modifier = Modifier.padding(horizontal = 16.dp))
    }
}