package com.bachphucngequy.bitbull.presentation.ui.components.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bachphucngequy.bitbull.R

@Composable
fun CryptoList(cryptoData: List<Crypto>) {
    LazyColumn {
        items(cryptoData) { crypto ->
            CryptoItem(crypto)
        }
    }
}

@Composable
fun CryptoItem(crypto: Crypto) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {}
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = crypto.icon),
            contentDescription = null,
            modifier = Modifier.size(40.dp)
        )
        Column(modifier = Modifier.padding(start = 16.dp)) {
            Text(text = crypto.name, fontSize = 16.sp)
            Text(text = crypto.volume, color = Color.Gray, fontSize = 14.sp)
        }
        Spacer(modifier = Modifier.weight(1f))
        Column(horizontalAlignment = Alignment.End) {
            Text(text = crypto.price, fontSize = 16.sp)
            Text(text = crypto.change, color = if (crypto.change.startsWith("+")) Color.Green else Color.Red, fontSize = 14.sp)
        }
    }
}

data class Crypto(val icon: Int, val name: String, val volume: String, val price: String, val change: String)

val sampleData = mutableListOf(
    Crypto(R.drawable.btc, "BTC", "$464,70M", "$59.749", "-0,42%"),
    Crypto(R.drawable.eth, "ETH", "$390,94M", "$2.666,4", "-0,65%"),
)
