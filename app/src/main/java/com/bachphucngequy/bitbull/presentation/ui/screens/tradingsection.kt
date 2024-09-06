package com.bachphucngequy.bitbull.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Define light theme colors
object LightThemeColors {
    val Background = Color.White
    val Surface = Color(0xFFF5F5F5)
    val Primary = Color(0xFF1E88E5)
    val PrimaryVariant = Color(0xFF1565C0)
    val Secondary = Color(0xFFFF6F00)
    val TextPrimary = Color.Black
    val TextSecondary = Color.Gray
    val Green = Color(0xFF4CAF50)
    val Red = Color(0xFFF44336)
}

@Composable
fun TradingSection() {
    var orderType by remember { mutableStateOf("Buy") }
    var marketType by remember { mutableStateOf("Market") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightThemeColors.Background)
            .padding(16.dp)
    ) {
        TopBarSection()
        Spacer(modifier = Modifier.height(16.dp))
        TradingPairInfo()
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OrderBookSection()
            TradingForm(orderType, { orderType = it }, marketType, { marketType = it })
        }
        Spacer(modifier = Modifier.height(16.dp))
        BottomSection()
    }
}

@Composable
fun TopBarSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Convert", color = LightThemeColors.TextSecondary)
            Text("Spot", fontWeight = FontWeight.Bold, color = LightThemeColors.Primary)
            Text("Margin", color = LightThemeColors.TextSecondary)
            Text("Bots", color = LightThemeColors.TextSecondary)
            Text("Copy", color = LightThemeColors.TextSecondary)
            Text("Buy", color = LightThemeColors.TextSecondary)
            Text("P2P", color = LightThemeColors.TextSecondary)
        }
        Text("...", color = LightThemeColors.TextPrimary)
    }
}

@Composable
fun TradingPairInfo() {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "BTC/USDT",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = LightThemeColors.TextPrimary
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text("-1.71%", color = LightThemeColors.Red)
    }
}

@Composable
fun OrderBookSection() {
    Column(
        modifier = Modifier
            .width(200.dp)
            .background(LightThemeColors.Surface)
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Price (USDT)", color = LightThemeColors.TextSecondary, fontSize = 12.sp)
            Text("Amount (BTC)", color = LightThemeColors.TextSecondary, fontSize = 12.sp)
        }
        Spacer(modifier = Modifier.height(8.dp))
        OrderBookList(listOf(58400, 58300, 58200, 58100, 58000), LightThemeColors.Red)
        Text(
            "57,944.00",
            color = LightThemeColors.Green,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        OrderBookList(listOf(57900, 57800, 57700, 57600, 57500), LightThemeColors.Green)
    }
}

@Composable
fun OrderBookList(prices: List<Int>, color: Color) {
    LazyColumn {
        items(prices) { price ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(String.format("%.2f", price.toFloat()), color = color)
                Text(String.format("%.5f", (Math.random() * 10)), color = color)
            }
        }
    }
}

@Composable
fun TradingForm(
    orderType: String,
    onOrderTypeChange: (String) -> Unit,
    marketType: String,
    onMarketTypeChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .width(200.dp)
            .background(LightThemeColors.Surface)
            .padding(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { onOrderTypeChange("Buy") },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (orderType == "Buy") LightThemeColors.Green else LightThemeColors.Surface
                )
            ) {
                Text("Buy", color = if (orderType == "Buy") Color.White else LightThemeColors.TextPrimary)
            }
            Button(
                onClick = { onOrderTypeChange("Sell") },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (orderType == "Sell") LightThemeColors.Red else LightThemeColors.Surface
                )
            ) {
                Text("Sell", color = if (orderType == "Sell") Color.White else LightThemeColors.TextPrimary)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(marketType, color = LightThemeColors.TextPrimary)
            DropdownMenu(
                expanded = false,
                onDismissRequest = { },
            ) {
                DropdownMenuItem(onClick = { onMarketTypeChange("Market") }) {
                    Text("Market")
                }
                DropdownMenuItem(onClick = { onMarketTypeChange("Limit") }) {
                    Text("Limit")
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = "",
            onValueChange = { },
            label = { Text("Market Price") },
            enabled = marketType != "Market",
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = LightThemeColors.TextPrimary,
                disabledTextColor = LightThemeColors.TextSecondary,
                backgroundColor = LightThemeColors.Background,
                focusedBorderColor = LightThemeColors.Primary,
                unfocusedBorderColor = LightThemeColors.TextSecondary
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = "",
            onValueChange = { },
            label = { Text("Total USDT") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = LightThemeColors.TextPrimary,
                backgroundColor = LightThemeColors.Background,
                focusedBorderColor = LightThemeColors.Primary,
                unfocusedBorderColor = LightThemeColors.TextSecondary
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        TradingInfoRow("Avbl", "0 USDT")
        TradingInfoRow("Max Buy", "0.00000 BTC")
        TradingInfoRow("Est. Fee", "-- BTC")
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(backgroundColor = LightThemeColors.Primary)
        ) {
            Text("Buy BTC", color = Color.White)
        }
    }
}

@Composable
fun TradingInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = LightThemeColors.TextSecondary)
        Text(value, color = LightThemeColors.TextPrimary)
    }
}

@Composable
fun BottomSection() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Open Orders (0)", fontWeight = FontWeight.Bold, color = LightThemeColors.TextPrimary)
                Text("Holdings", color = LightThemeColors.TextSecondary)
                Text("Spot Grid", color = LightThemeColors.TextSecondary)
            }
            // Add an icon here if needed
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Available Funds: 0.00 USDT", color = LightThemeColors.TextPrimary)
        Text("Transfer funds to your Spot wallet to trade", color = LightThemeColors.TextSecondary)
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(backgroundColor = LightThemeColors.Primary)
            ) {
                Text("Increase Balance", color = Color.White)
            }
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(backgroundColor = LightThemeColors.Secondary)
            ) {
                Text("Learn", color = Color.White)
            }
        }
    }
}