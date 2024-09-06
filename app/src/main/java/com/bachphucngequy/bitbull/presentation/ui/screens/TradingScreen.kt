package com.bachphucngequy.bitbull.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Window
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bachphucngequy.bitbull.presentation.ui.theme.Green100
import com.bachphucngequy.bitbull.presentation.ui.theme.Red100

@Composable
fun TradingScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopBar()
        PriceInfo()
        CandlestickChart()
        TradingInfo()
        OrderBook()
        BottomBar()
    }
}

@Composable
fun TopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 16.dp, top = 16.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back",
                    modifier = Modifier.size(20.dp)
                )
            }
            Text("BTC/USDT ▼", fontWeight = FontWeight.Bold,
                fontSize = 18.sp)
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(imageVector = Icons.Default.Star, contentDescription = "Favorite",
                modifier = Modifier.size(20.dp))
            Icon(imageVector = Icons.Default.Share, contentDescription = "Share",
                modifier = Modifier.size(20.dp))
            Icon(imageVector = Icons.Filled.Window, contentDescription = "More",
                modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
fun PriceInfo() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("59.459,00", fontSize = 24.sp, color = Color.Red)
        Text("≈ 59.459 $ -0,37%", color = Color.Gray)
        Row {
            Text("POW", color = Color.Gray)
            Text("Vol", color = Color.Gray)
            Text("Price Protection >", color = Color.Gray)
        }
    }
}

@Composable
fun CandlestickChart() {
    // Implement a custom candlestick chart here
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(Color.LightGray)
    ) {
        Text("Candlestick Chart Placeholder", modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
fun TradingInfo() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text("24h High", color = Color.Gray)
            Text("61.166,99")
        }
        Column {
            Text("24h Vol(BTC)", color = Color.Gray)
            Text("28.868,56")
        }
        Column {
            Text("24h Low", color = Color.Gray)
            Text("58.685,00")
        }
        Column {
            Text("24h Vol(USDT)", color = Color.Gray)
            Text("1,73B")
        }
    }
}

@Composable
fun OrderBook() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Order Book", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.weight(1f))
        Text("Trades")
    }

//    LinearProgressIndicator(
//        strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(8.dp),
//        color = Color.Green,
//        trackColor = Color.Red
//    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("40,88%", color = Color.Green)
        Text("59,12%", color = Color.Red)
    }
}

@Composable
fun BottomBar() {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp),
//        horizontalArrangement = Arrangement.SpaceBetween
//    ) {
//        Column(horizontalAlignment = Alignment.CenterHorizontally) {
//            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More")
//            Text("More")
//        }
//        Column(horizontalAlignment = Alignment.CenterHorizontally) {
//            Icon(imageVector = Icons.Default.Notifications, contentDescription = "Alert")
//            Text("Alert")
//        }
//        Column(horizontalAlignment = Alignment.CenterHorizontally) {
//            Icon(imageVector = Icons.Default.AccountBalance, contentDescription = "Margin")
//            Text("Margin")
//        }
//        Column(horizontalAlignment = Alignment.CenterHorizontally) {
//            Icon(imageVector = Icons.Default.GridOn, contentDescription = "Grid")
//            Text("Grid")
//        }
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp)
//        ) {
//            Button(
//                onClick = { /* Handle buy action */ },
//                modifier = Modifier
//                    .weight(1f)
//                    .height(48.dp),
//                colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
//            ) {
//                Text("Buy", color = Color.White)
//            }
//            Spacer(modifier = Modifier.width(16.dp))
//            Button(
//                onClick = { /* Handle sell action */ },
//                modifier = Modifier
//                    .weight(1f)
//                    .height(48.dp),
//                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
//            ) {
//                Text("Sell", color = Color.White)
//            }
//        }
//    }
    BottomAppBar (
        actions = {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(start = 10.dp)
                ) {
                    FilledTonalButton(
                        onClick = { /*TODO*/ },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Green100
                        )
                    ) {
                        Text("Buy", style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(end = 30.dp, start = 30.dp)
                        )
                    }
                    FilledTonalButton(onClick = { /*TODO*/ },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Red100
                        )) {
                        Text("Sell", style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(end = 30.dp, start = 30.dp)

                        )
                    }
                }
                Row {
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            Icons.Outlined.Notifications,
                            contentDescription = "Localized description",
                        )
                    }
                    IconButton(onClick = { /* do something */ }) {
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

@Preview
@Composable
fun TradingScreenPreview() {
    TradingScreen()
}