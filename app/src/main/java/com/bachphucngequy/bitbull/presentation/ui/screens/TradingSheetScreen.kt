package com.bachphucngequy.bitbull.presentation.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bachphucngequy.bitbull.R

@Composable
fun TradingSheetScreen(
    onNavigateToBuySellScreen: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        //Return
        ReturnIcon(onNavigateToBuySellScreen)

        // Top Bar
        TopBar2()

//        Spacer(modifier = Modifier.weight(1f))

        // Trading Details Section
        TradingDetailsSection()

        Spacer(modifier = Modifier.height(15.dp))

        // Stock Information Section
        StockInfoSection()

//        Spacer(modifier = Modifier.weight(1f))

        // Bottom Actions
        BottomActionsSection()
    }
}

@Composable
fun ReturnIcon( onNavigateToBuySellScreen: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().background(Color(0xFFD8F8E1)).padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(50.dp))

        // Return Icon
        Image(
            painter = painterResource(id = R.drawable.ic_return), // Replace with your actual icon resource
            contentDescription = "Return",
            modifier = Modifier
                .size(24.dp)
                .clickable(onClick = onNavigateToBuySellScreen)
        )
    }
}


@Composable
fun TopBar2() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().background(Color(0xFFD8F8E1)).padding(16.dp)
    ) {
        // Stock Logo and Name
        Image(
            painter = painterResource(id = R.drawable.ic_stock_logo), // Replace with stock logo
            contentDescription = "Stock Logo",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(text = "AAPL", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(text = "Apple Inc", fontSize = 14.sp, color = Color.Gray)
        }

        Spacer(modifier = Modifier.weight(1f))

        // Status Badge
        Text(
            text = "Filled",
            color = Color(0xFFD8F8E1),
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .background(Color.LightGray, shape = CircleShape)
                .padding(8.dp)
        )
    }
}

@Composable
fun TradingDetailsSection() {
    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth().padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        RowInfo(label = "Side", value = "Buy")
        Spacer(modifier = Modifier.height(5.dp))
        RowInfo(label = "Total Quantity", value = "2")
        Spacer(modifier = Modifier.height(5.dp))
        RowInfo(label = "Filled Quantity", value = "2")
        Spacer(modifier = Modifier.height(5.dp))
        RowInfo(label = "Avg Price", value = "$221.63")
        Spacer(modifier = Modifier.height(5.dp))
        RowInfo(label = "Order Filled", value = "07/31/2024 09:50:55 EDT")
        Spacer(modifier = Modifier.height(5.dp))
        RowInfo(label = "Order Type", value = "LIMIT")
        Spacer(modifier = Modifier.height(5.dp))
        RowInfo(label = "Limit Price", value = "$221.74")
        Spacer(modifier = Modifier.height(5.dp))
        RowInfo(label = "Time-in-Force", value = "Day")
        Spacer(modifier = Modifier.height(5.dp))
        RowInfo(label = "Trading Hours", value = "Only Regular Hours")
        Spacer(modifier = Modifier.height(5.dp))
        RowInfo(label = "Order Placed", value = "07/31/2024 09:50:53 EDT")
    }
}

@Composable
fun RowInfo(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(text = label, modifier = Modifier.weight(1f), color = Color.Gray)
        Text(text = value, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun StockInfoSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White).padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Last Price", fontWeight = FontWeight.Medium, color = Color.Gray)
            Text(
                text = "221.63 +1.29%",
                color = Color.Green,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF1F1F1), RoundedCornerShape(8.dp)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Bid Section
            Row(
                modifier = Modifier
                    .weight(1f)
                    .background(Color(0xFFD8F8E1), RoundedCornerShape(8.dp))
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Bid", color = Color.Green, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "200", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "221.61", fontWeight = FontWeight.Bold)
            }

            // Divider between Bid and Ask
            Spacer(modifier = Modifier.width(4.dp))

            // Ask Section
            Row(
                modifier = Modifier
                    .weight(1f)
                    .background(Color(0xFFFFE8E8), RoundedCornerShape(8.dp))
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "221.64", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "120", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Ask", color = Color.Red, fontWeight = FontWeight.Bold)
            }
        }
    }
}



@Composable
fun BottomActionsSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(onClick = { /* Cancel Order action */ }, modifier = Modifier.weight(1f),colors = ButtonDefaults.buttonColors(Color.Gray)) {
            Text("Cancel")
        }

        Spacer(modifier = Modifier.width(8.dp))

        Button(onClick = { /* Modify Order action */ }, modifier = Modifier.weight(1f),colors = ButtonDefaults.buttonColors(Color.Gray)) {
            Text("Modify")
        }

        Spacer(modifier = Modifier.width(8.dp))

        Button(onClick = { /* Quotes action */ }, modifier = Modifier.weight(1f),colors = ButtonDefaults.buttonColors(Color.Gray)) {
            Text("Quotes")
        }
    }
}
