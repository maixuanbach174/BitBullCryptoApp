package com.example.pitbulltradingapp.ui.components.navigationbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Feed
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Feed
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Sell
import androidx.compose.material.icons.outlined.Wallet
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavigationItem (
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean,
    val badgeCount: Int? = null
)

object ItemData {
    val items = listOf(
        BottomNavigationItem(
            title = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home ,
            hasNews = false
        ),
        BottomNavigationItem(
            title = "Markets",
            selectedIcon = Icons.Filled.BarChart,
            unselectedIcon = Icons.Outlined.BarChart,
            hasNews = false
        ),
        BottomNavigationItem(
            title = "Trade",
            selectedIcon = Icons.Filled.Sell,
            unselectedIcon = Icons.Outlined.Sell,
            hasNews = false
        ),
        BottomNavigationItem(
            title = "Feeds",
            selectedIcon = Icons.Filled.Feed,
            unselectedIcon = Icons.Outlined.Feed,
            hasNews = true,
            badgeCount = 2
        ),
        BottomNavigationItem(
            title = "Wallets",
            selectedIcon = Icons.Filled.Wallet,
            unselectedIcon = Icons.Outlined.Wallet,
            hasNews = false
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationBar(
    selectedItemIndex: Int,
    onBottomTabSelect: (Int) -> Unit
) {
    NavigationBar {
        ItemData.items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItemIndex == index,
                onClick = {
                    onBottomTabSelect(index)
                    //navcontroller.navigate(route = item.route)
                },
                label = {
                        Text(text = item.title)
                },
                icon = {
                    BadgedBox(
                        badge = {
                            if(item.badgeCount != null) {
                                Badge {
                                    Text(text = item.badgeCount.toString())
                                }
                            } else if (item.hasNews) {
                                Badge ()
                            }
                    }) {
                        Icon(
                            imageVector = if (selectedItemIndex == index)
                                item.selectedIcon else item.unselectedIcon,
                            contentDescription = item.title
                        )
                    }

                }
            )
        }
    }
}