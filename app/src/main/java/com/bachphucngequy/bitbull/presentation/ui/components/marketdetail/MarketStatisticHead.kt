package com.bachphucngequy.bitbull.presentation.ui.components.marketdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bachphucngequy.bitbull.domain.model.Ticker
import com.bachphucngequy.bitbull.presentation.ui.theme.DarkRed
import com.bachphucngequy.bitbull.presentation.ui.theme.Green100
import com.bachphucngequy.bitbull.presentation.ui.theme.Red100
import com.bachphucngequy.bitbull.presentation.viewmodel.TickerViewModel
import java.text.DecimalFormat

@Composable
fun MarketStatisticHead(
    modifier: Modifier = Modifier,
    uiStateData: List<Ticker>
) {

    if(uiStateData.isNotEmpty()) {
        uiStateData[0].let {ticker ->
            val asc = (ticker.openPrice < ticker.lastPrice)
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1.2f),
                ) {
                    Text(
                        ticker.lastPrice.toString(),
                        color = if (asc) Green100 else Red100,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineMedium
                    )
                    val gainSymbol = if (ticker.priceChangePercent > 0) "▲" else "▼"
                    val sign = if (ticker.priceChange > 0) "+" else ""
                    Text(
                        "$gainSymbol${ticker.priceChange}  $sign${ticker.priceChangePercent}%",
                        fontWeight = FontWeight.Bold,
                        color = if (asc) Green100 else DarkRed,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                        ) {
                            Text(
                                "24h High",
                                color = Color.Gray,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                ticker.highPrice.toString(),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.End,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        Column(
                            modifier = Modifier.weight(1.5f),
                        ) {
                            Text(
                                "24h Vol(${ticker.productCode})",
                                color = Color.Gray,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                DecimalFormat("#,###.0").format(ticker.baseAssetVolume),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.End,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                "24h Low",
                                color = Color.Gray,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.End,
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 10.sp
                            )
                            Text(
                                ticker.lowPrice.toString(),
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.End,
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 10.sp
                            )
                        }
                        Column(
                            modifier = Modifier.weight(1.5f)
                        ) {
                            Text(
                                "24h Vol(${ticker.quoteCode})",
                                color = Color.Gray,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.End,
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 10.sp
                            )
                            Text(
                                DecimalFormat("#,###.0").format(ticker.quoteAssetVolume),
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.End,
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }
        }
    }
}