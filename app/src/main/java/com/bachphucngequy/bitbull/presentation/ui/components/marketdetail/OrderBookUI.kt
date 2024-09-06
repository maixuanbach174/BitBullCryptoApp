package com.bachphucngequy.bitbull.presentation.ui.components.marketdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class OrderBookEntry(
    val bid: String,
    val bidPrice: String,
    val askPrice: String,
    val ask: String
)

@Composable
fun OrderBookUI(
    modifier: Modifier = Modifier
) {
    val orderBookData = remember { generateSampleData() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        OrderBookHeader()
        Spacer(modifier = Modifier.height(8.dp))
        OrderBookProgressBar()
        Spacer(modifier = Modifier.height(8.dp))
        OrderBookTable(orderBookData)
    }
}

@Composable
fun OrderBookHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            "Order Book",
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Text(
            "Trades",
            color = Color.Gray
        )
    }
}

@Composable
fun OrderBookProgressBar() {
    val bidPercentage = 24.85f
    val askPercentage = 75.15f

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            "${String.format("%.2f", bidPercentage)}%",
            color = Color(0xFF00C087),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        LinearProgressIndicator(
            progress = bidPercentage / 100,
            modifier = Modifier
                .weight(1f)
                .height(4.dp),
            color = Color(0xFF00C087),
            trackColor = Color(0xFFF6465D)
        )
        Text(
            "${String.format("%.2f", askPercentage)}%",
            color = Color(0xFFF6465D),
            textAlign = TextAlign.End,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun OrderBookTable(data: List<OrderBookEntry>) {
    Column {
        OrderBookTableHeader()
        Column {
            data.forEach { entry ->
                OrderBookRow(entry)
            }
        }
    }
}

@Composable
fun OrderBookTableHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("Bid", color = Color.Gray, modifier = Modifier.weight(1f))
        Text("Ask", color = Color.Gray, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
        Text("0.01 ▼", color = Color.Gray, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
    }
}

@Composable
fun OrderBookRow(entry: OrderBookEntry) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(entry.bid, color = Color.Black, modifier = Modifier.weight(1f), fontSize = 14.sp)
        Text(entry.bidPrice, color = Color(0xFF00C087), modifier = Modifier
            .weight(1f)
            .padding(end = 2.dp), textAlign = TextAlign.End, fontSize = 14.sp)
        Text(entry.askPrice, color = Color(0xFFF6465D), modifier = Modifier
            .weight(1f)
            .padding(start = 2.dp), textAlign = TextAlign.Start, fontSize = 14.sp)
        Text(entry.ask, color = Color.Black, modifier = Modifier.weight(1f), textAlign = TextAlign.End, fontSize = 14.sp)
    }
}

fun generateSampleData(): List<OrderBookEntry> {
    return listOf(
        OrderBookEntry("3,43654", "59.524,98", "59.524,99", "11,24936"),
        OrderBookEntry("0,00009", "59.524,75", "59.525,01", "0,01553"),
        OrderBookEntry("0,00330", "59.524,74", "59.525,05", "0,24335"),
        OrderBookEntry("0,00010", "59.524,72", "59.525,20", "0,01680"),
        OrderBookEntry("0,01990", "59.524,02", "59.525,60", "0,00009"),
        // Add more sample data here to match the image
        OrderBookEntry("3,43654", "59.524,98", "59.524,99", "11,24936"),
        OrderBookEntry("0,00009", "59.524,75", "59.525,01", "0,01553"),
        OrderBookEntry("0,00330", "59.524,74", "59.525,05", "0,24335"),
        OrderBookEntry("0,00010", "59.524,72", "59.525,20", "0,01680"),
        OrderBookEntry("0,01990", "59.524,02", "59.525,60", "0,00009"),
        OrderBookEntry("3,43654", "59.524,98", "59.524,99", "11,24936"),
        OrderBookEntry("0,00009", "59.524,75", "59.525,01", "0,01553"),
        OrderBookEntry("0,00330", "59.524,74", "59.525,05", "0,24335"),
        OrderBookEntry("0,00010", "59.524,72", "59.525,20", "0,01680"),
        OrderBookEntry("0,01990", "59.524,02", "59.525,60", "0,00009"),
        OrderBookEntry("3,43654", "59.524,98", "59.524,99", "11,24936"),
        OrderBookEntry("0,00009", "59.524,75", "59.525,01", "0,01553"),
        OrderBookEntry("0,00330", "59.524,74", "59.525,05", "0,24335"),
        OrderBookEntry("0,00010", "59.524,72", "59.525,20", "0,01680"),
        OrderBookEntry("0,01990", "59.524,02", "59.525,60", "0,00009"),
        OrderBookEntry("3,43654", "59.524,98", "59.524,99", "11,24936"),
        OrderBookEntry("0,00009", "59.524,75", "59.525,01", "0,01553"),
        OrderBookEntry("0,00330", "59.524,74", "59.525,05", "0,24335"),
        OrderBookEntry("0,00010", "59.524,72", "59.525,20", "0,01680"),
        OrderBookEntry("0,01990", "59.524,02", "59.525,60", "0,00009"),


        )
}
