package com.bachphucngequy.bitbull.firebase

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BlankBox() {
    Box(
        modifier = Modifier
            .size(100.dp) // Define the size of the box
            .background(Color.White) // Optional background color for visibility
    )
}
