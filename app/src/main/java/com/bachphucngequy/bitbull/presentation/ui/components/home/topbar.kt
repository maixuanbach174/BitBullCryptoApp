package com.bachphucngequy.bitbull.presentation.ui.components.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Headset
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.bachphucngequy.bitbull.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(onSearchClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.onlylogo),
            contentDescription = "App Logo",
            modifier = Modifier.size(40.dp)
        )
        SearchBar(onSearchClick = onSearchClick)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(Icons.Default.Fullscreen, contentDescription = "Fullscreen")
            Icon(Icons.Default.Headset, contentDescription = "Support")
            BadgedBox(
                badge = {
                        Badge {
                            Text(text = "9")
                        }

                }) {
                Icon(
                    Icons.Default.Notifications,
                    contentDescription = "notification"
                )
            }
            Icon(Icons.Default.Payment, contentDescription = "Payment")
        }
    }
}
