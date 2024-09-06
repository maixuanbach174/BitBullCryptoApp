package com.example.pitbulltradingapp.ui.components.MarketTracker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AccessTimeFilled
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ViewList
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HorizontalOptionsList(
    onTimeSpanClick: () -> Unit,
    onLineTypeClick: () -> Unit,
    onChartToolBoxClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.Start
    ) {
        // time span options
        IconButton(onClick = { onTimeSpanClick() }) {
            Icon(imageVector = Icons.Filled.AccessTimeFilled
                , contentDescription = "time span options",
                modifier = Modifier.size(20.dp))
        }
        // Line type
        IconButton(onClick = { onLineTypeClick() }) {
            Icon(imageVector = Icons.Filled.BarChart
                , contentDescription = "Line type",
                modifier = Modifier.size(20.dp))
        }
        // Chart tool box
        IconButton(onClick = { onChartToolBoxClick() }) {
            Icon(imageVector = Icons.Filled.ViewList
                , contentDescription = "Chart tool box",
                modifier = Modifier.size(20.dp))
        }
    }
}
