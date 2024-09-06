package com.bachphucngequy.bitbull.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

// Phuc Nguyen
@Composable
fun BuySellScreen(
    onNavigateToTradingSheet: () -> Unit
) {
    Box(
        modifier = Modifier
            .background(color = Color.Blue)
            .fillMaxSize(),
        contentAlignment = Alignment.Center // Dùng contentAlignment thay cho verticalArrangement và horizontalAlignment
    ) {
        Button(onClick = onNavigateToTradingSheet) {
            Text("Go to trading sheet screen")
        }
    }

}