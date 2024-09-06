package com.example.pitbulltradingapp.ui.components.MarketTracker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.DecimalFormat

@Composable
fun MarketStatisticHead(
    modifier: Modifier = Modifier
) {
    val currentPrice = 59179.10
    val currentPriceChange = 124.65
    val currentPriceChangePercent = 0.25
    val dec = DecimalFormat("#,###.00")
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1.2f),
        ) {
            Text(dec.format(currentPrice), fontSize = 30.sp, color = Color.Red, fontWeight = FontWeight.Bold)
            Text("≈ $currentPriceChange $ -$currentPriceChangePercent%", fontWeight = FontWeight.Bold)
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Column(
                    modifier = Modifier.weight(1f),
                ) {
                    Text("24h High", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("61.166,99", fontSize = 12.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.End)
                }
                Column(
                    modifier = Modifier.weight(1.3f),
                ) {
                    Text("24h Vol(BTC)", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("28.868,56", fontSize = 12.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.End)
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text("24h Low", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.End)
                    Text("58.685,00", fontSize = 12.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.End)
                }
                Column(
                    modifier = Modifier.weight(1.3f)
                ) {
                    Text("24h Vol(USDT)", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.End)
                    Text("1,73B", fontSize = 12.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.End)
                }
            }
        }
    }
}