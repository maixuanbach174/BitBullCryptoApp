package com.bachphucngequy.bitbull.history.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bachphucngequy.bitbull.history.model.HistoryItem

@Composable
fun HistoryListItem(
    modifier: Modifier = Modifier,
    historyItem: HistoryItem
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = historyItem.coinCode,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = historyItem.date,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = if(historyItem.amount > 0){
                            "+" + historyItem.amount.toString()
                        } else {
                            historyItem.amount.toString()
                        },
                style = MaterialTheme.typography.bodyLarge,
                color = if(historyItem.amount > 0){
                            Color.Green
                        } else {
                            Color.Red
                        },
            )
            Text(text = historyItem.status, style = MaterialTheme.typography.bodyMedium)
        }
    }
}