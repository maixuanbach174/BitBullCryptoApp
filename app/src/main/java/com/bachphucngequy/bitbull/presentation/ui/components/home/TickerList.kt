package com.bachphucngequy.bitbull.presentation.ui.components.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.bachphucngequy.bitbull.domain.model.Ticker
import java.text.DecimalFormat
import com.bachphucngequy.bitbull.presentation.ui.theme.DarkRed
import com.bachphucngequy.bitbull.presentation.ui.theme.Green100


@Composable
fun TickerList(data: List<Ticker>, onNavigateToDetail: (String) -> Unit) {
    LazyColumn(
        Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(data.sortedBy { ticker -> ticker.productId }) { item ->
            TickerItem(item, onNavigateToDetail)
        }
    }
}

@Composable
fun TickerItem(item: Ticker, onNavigateToDetail: (String) -> Unit) {
    val asc = (item.openPrice < item.price)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {}
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = getProductResource(item.productCode),
            contentDescription = null,
            modifier = Modifier.size(40.dp),
        )
        Column(modifier = Modifier.padding(start = 16.dp)) {
            Text(
                text = item.productCode,
                fontWeight = FontWeight.W500,
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 16.sp,
            )
            Text(
                text = "Vol. ${DecimalFormat("#.###").format(item.volume24)}",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.W400,
                fontSize = 14.sp
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Column(horizontalAlignment = Alignment.End) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        color = if (asc) Green100 else DarkRed,
                    )
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ){
                Text(
                    text = item.price.toString(),
                    fontSize = 14.sp,
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    fontWeight = FontWeight.W500
                )
            }
            Text(
                text = "$${item.openPrice} 24h",
                modifier = Modifier.fillMaxWidth(),
                fontSize = 14.sp,
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.W400
            )
        }
    }
}

fun getProductResource(productCode: String): String {
    return when (productCode) {
        "BTC" -> "https://static.coinpaprika.com/coin/btc-bitcoin/logo.png"
        "ETH" -> "https://static.coinpaprika.com/coin/eth-ethereum/logo.png"
        "ADA" -> "https://static.coinpaprika.com/coin/ada-cardano/logo.png"
        "LINK" -> "https://static.coinpaprika.com/coin/link-chainlink/logo.png"
        "LTC" -> "https://static.coinpaprika.com/coin/ltc-litecoin/logo.png"
        else -> {
            "https://static.coinpaprika.com/coin/bnb-binance-coin/logo.png"
        }
    }
}
