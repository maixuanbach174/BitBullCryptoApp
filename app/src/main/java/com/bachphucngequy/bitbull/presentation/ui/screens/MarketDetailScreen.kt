package com.bachphucngequy.bitbull.presentation.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bachphucngequy.bitbull.data.entity.Crypto
import com.bachphucngequy.bitbull.domain.model.Ticker
import com.bachphucngequy.bitbull.news.NewsScreen
import com.bachphucngequy.bitbull.news.NewsViewModel
import com.bachphucngequy.bitbull.news.api.Article
import com.bachphucngequy.bitbull.presentation.ui.components.marketdetail.MarketStatisticHead
import com.bachphucngequy.bitbull.presentation.ui.components.MarketTracker.PartialBottomSheet
import com.bachphucngequy.bitbull.presentation.ui.components.home.TabRow
import com.bachphucngequy.bitbull.presentation.ui.components.marketdetail.BuySellBar
import com.bachphucngequy.bitbull.presentation.ui.components.marketdetail.OrderBookUI
import com.bachphucngequy.bitbull.presentation.ui.components.marketdetail.SymbolAppBar
import com.bachphucngequy.bitbull.presentation.ui.components.marketdetail.TradingChart
import com.bachphucngequy.bitbull.presentation.viewmodel.TickerViewModel

@Composable
fun MarketDetailScreen(
    onNavigateToBuySell: () -> Unit,
    onBackClick: () -> Unit,
    newsViewModel: NewsViewModel,
    onNavigateToNewsDetails: (Article) -> Unit,
    onFavouriteClick: () -> Unit,
    crypto: Crypto,
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val tickerViewModel: TickerViewModel = hiltViewModel()
    val uiState by tickerViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        tickerViewModel.getCryptos(crypto.symbol)
    }
    var t1=crypto.code
    var t2=crypto.quoteCode
    var price=0.0
    if(uiState.data.isNotEmpty()) {
        price=uiState.data[0].lastPrice
    }

    Log.d("CryptoInfo", "t1: $t1")
    Log.d("CryptoInfo", "t2: $t2")
    //Log.d("CryptoInfo", "Initial price: $price")
    // Comment: base coin (btc)
    // code: crypto.code
    // Comment: quote coin (usdt)
    // Code: crypto.quoteCode
    // Comment: price
    // Code: uiState.data[0].lastPrice
    // nho check empty list hay khong nha vi cai do la cai list cua ticker nen co the bi null
    // Co the implement theo kieu nay
//    if(uiStateData.isNotEmpty()) {
//        uiStateData[0].let {ticker -> {code cua Nghe}}

    Scaffold(
        topBar = {
            Column {
                SymbolAppBar(
                    marketName = crypto.code + "/" + crypto.quoteCode,
                    companyName = crypto.fullName,
                    onBackClick = onBackClick,
                    onFavouriteClick = onFavouriteClick,
                    isCoinFavourite = false
                )
                TabRow(tabTitles = listOf("Price", "Info", "Trading Data", "News"), onClick = { index ->
                    selectedTabIndex = index
                })
            }
        },
        bottomBar = {
            BuySellBar()
        }
    ) {innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedTabIndex) {
                0 -> PriceContent(
                    modifier = Modifier.fillMaxSize(),
                    uiStateData = uiState.data,
                    symbol = crypto.symbol
                )
                1 -> CoinDetailScreen(coinId = crypto.code.lowercase() + "-" + crypto.fullName.lowercase())
                2 -> TradingDataContent()
                3 -> NewsScreen(
                    newsViewModel = newsViewModel,
                    navigateToDetails = onNavigateToNewsDetails,
                    searchQuery = crypto.symbol
                )
            }
        }
    }
}

@Composable
fun PriceContent(
    modifier: Modifier,
    uiStateData: List<Ticker>,
    symbol: String
) {

    var selectedTimeframe by remember { mutableStateOf("15") }
    var selectedChartType by remember { mutableStateOf("1") }

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {

        MarketStatisticHead(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp), uiStateData = uiStateData)
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

@Composable
fun InfoContent() {
    // Implement info content here
    Text("Info Content")
}

@Composable
fun TradingDataContent() {
    // Implement trading data content here
    Text("Trading Data Content")
}

@Composable
fun SquareContent() {
    // Implement square content here
    Text("Square Content")
}