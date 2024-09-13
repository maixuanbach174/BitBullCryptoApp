package com.bachphucngequy.bitbull.history.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bachphucngequy.bitbull.history.model.BuySellHistoryItem

@Composable
fun BuySellListItem(
    modifier: Modifier = Modifier,
    buySellHistoryItem: BuySellHistoryItem
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = buySellHistoryItem.t1 + "/" + buySellHistoryItem.t2,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black
            )
            Text(
                text = buySellHistoryItem.type,
                style = MaterialTheme.typography.bodyMedium,
                color = if(buySellHistoryItem.type == "Buy") Color.Green else Color.Red
            )
            Text(
                text = "Amount",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray
            )
            Text(
                text = "Price(" + buySellHistoryItem.t2 + ")",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray
            )
        }
        Column {
            Text(
                text = buySellHistoryItem.date,
                style = MaterialTheme.typography.bodySmall,
                color = Color.DarkGray
            )
            Text(
                text = buySellHistoryItem.status,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray
            )
            Text(
                text = buySellHistoryItem.amount.toString(),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray
            )
            Text(
                text = buySellHistoryItem.price.toString(),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )
        }
    }
}