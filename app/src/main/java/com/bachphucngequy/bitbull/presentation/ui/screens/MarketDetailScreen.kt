package com.bachphucngequy.bitbull.presentation.ui.screens

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bachphucngequy.bitbull.data.entity.Crypto
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

@Composable
fun MarketDetailScreen(
    onNavigateToBuySell: () -> Unit,
    onBackClick: () -> Unit,
    newsViewModel: NewsViewModel,
    onNavigateToNewsDetails: (Article) -> Unit,
    onFavouriteClick: () -> Unit,
    symbol: String,
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            Column {
                SymbolAppBar(
                    marketName = symbol.uppercase(),
                    companyName = Crypto.getCompanyName(symbol),
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
                    symbol = symbol,
                )
//                1 -> CoinDetailScreen(coin = coinDetail)
                2 -> TradingDataContent()
                3 -> NewsScreen(
                    newsViewModel = newsViewModel,
                    navigateToDetails = onNavigateToNewsDetails,
                    searchQuery = symbol
                )
            }
        }
    }
}

@Composable
fun PriceContent(
    modifier: Modifier,
    symbol: String,
) {

    val scrollState = rememberScrollState()

    var selectedTimeframe by remember { mutableStateOf("15") }
    var selectedChartType by remember { mutableStateOf("1") }

    Column(
        modifier = modifier.verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {

        MarketStatisticHead(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp), symbol = symbol)
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