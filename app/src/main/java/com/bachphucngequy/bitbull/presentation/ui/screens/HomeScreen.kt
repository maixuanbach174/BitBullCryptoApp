package com.bachphucngequy.bitbull.presentation.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bachphucngequy.bitbull.data.entity.Crypto
import com.bachphucngequy.bitbull.presentation.ui.components.home.ProgressBar
import com.bachphucngequy.bitbull.presentation.ui.components.home.TabRow
import com.bachphucngequy.bitbull.presentation.ui.components.home.TickerList
import com.bachphucngequy.bitbull.presentation.ui.components.home.TopBar
import com.bachphucngequy.bitbull.presentation.ui.components.navigationbar.BottomNavigationBar
import com.bachphucngequy.bitbull.presentation.viewmodel.TickerViewModel

@Composable
fun HomeScreen(
    onSearchClick: () -> Unit,
    onNavigateToFeeds: () -> Unit,
    onNavigateToDeposit: () -> Unit,
    onNavigateToWithdraw: () -> Unit,
    onNavigateToUserAccount: () -> Unit,
    onNavigateToTrade: () ->Unit,
    onNavigateToDetail: (Crypto) -> Unit,
    onNavigateToMarket : () -> Unit
) {
    val viewModel: TickerViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getCryptos()
    }

    var bottomTabIndex by remember {
        mutableIntStateOf(0)
    }
    Scaffold(
        topBar = {
            Column {
                Spacer(modifier = Modifier.height(25.dp))
                TopBar(onSearchClick = onSearchClick)
            }
        },
        bottomBar = {
            BottomNavigationBar(
                selectedItemIndex = bottomTabIndex,
                onBottomTabSelect = {bottomTabIndex = it}
            )
        }

    ) {paddingValues ->
        when(bottomTabIndex) {
            0 -> Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp, 0.dp)
                ) {
                    ProgressBar(
                        onDepositClick = onNavigateToDeposit,
                        onWithdrawClick = onNavigateToWithdraw
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    TabRow(
                        listOf("Favorites", "Hot", "Gainers", "Losers", "New Listings", "24h"),
                        onClick = {}
                    )
                    if(uiState.isLoading) {

                    }
                    else {
                        Spacer(modifier = Modifier.height(20.dp))
//                    CryptoList(sampleData)
                        TickerList(
                            data = uiState.data,
                            onNavigateToDetail =  onNavigateToDetail
                        )
                    }
                }
            }
            1 -> {
                onNavigateToMarket()
                bottomTabIndex = 0
            }
            2-> {
                onNavigateToTrade()
                bottomTabIndex=0
            }
            3 -> {
                onNavigateToFeeds()
                bottomTabIndex = 0
            }

            4 -> {
                onNavigateToUserAccount()
                bottomTabIndex = 0
            }
        }

    }
}
