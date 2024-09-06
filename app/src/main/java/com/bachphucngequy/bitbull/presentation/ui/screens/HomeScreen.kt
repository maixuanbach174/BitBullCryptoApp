package com.bachphucngequy.bitbull.presentation.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bachphucngequy.bitbull.presentation.ui.components.home.CryptoList
import com.bachphucngequy.bitbull.presentation.ui.components.home.ProgressBar
import com.bachphucngequy.bitbull.presentation.ui.components.home.TabRow
import com.bachphucngequy.bitbull.presentation.ui.components.home.TopBar
import com.bachphucngequy.bitbull.presentation.ui.components.home.sampleData
import com.bachphucngequy.bitbull.presentation.ui.components.navigationbar.BottomNavigationBar

@Composable
fun HomeScreen(
    onSearchClick: () -> Unit,
    onNavigateToFeeds: () -> Unit,
    onNavigateToDeposit: () -> Unit,
    onNavigateToWithdraw: () -> Unit,
    onNavigateToUserAccount: () -> Unit
) {
    var bottomTabIndex by remember {
        mutableIntStateOf(0)
    }
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedItemIndex = bottomTabIndex,
                onBottomTabSelect = {bottomTabIndex = it}
            )
        },
        topBar = {
             TopBar(onSearchClick = onSearchClick)
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
                    Spacer(modifier = Modifier.height(20.dp))
                    CryptoList(sampleData)
                }
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
