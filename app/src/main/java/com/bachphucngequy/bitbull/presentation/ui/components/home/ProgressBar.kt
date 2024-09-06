package com.bachphucngequy.bitbull.presentation.ui.components.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ProgressBar() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text("Verification")
        Text("Deposit")
        Text("Trade")
    }
    Spacer(modifier = Modifier.height(10.dp))
    LinearProgressIndicator(
        progress = 0.25f,
        modifier = Modifier.fillMaxWidth(),
        trackColor = Color.LightGray.copy(alpha = 0.2f),
        color = Color.Black,
        strokeCap = StrokeCap.Round

    )

}
