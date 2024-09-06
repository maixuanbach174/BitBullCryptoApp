package com.bachphucngequy.bitbull.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bachphucngequy.bitbull.domain.model.CoinDetail
import com.bachphucngequy.bitbull.presentation.ui.components.coindetail.CoinTag
import com.bachphucngequy.bitbull.presentation.ui.components.coindetail.TeamListItem
import com.bachphucngequy.bitbull.presentation.ui.theme.Green100
import com.bachphucngequy.bitbull.presentation.ui.theme.Red100

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CoinDetailScreen(
//    coinId: String
    coin: CoinDetail
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(20.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${coin.rank}. ${coin.name} (${coin.symbol})",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.weight(8f)
                    )
                    Text(
                        text = if(coin.isActive) "active" else "inactive",
                        color = if(coin.isActive) Green100 else Red100,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End,
                        modifier = Modifier
                            .align(CenterVertically)
                            .weight(2f)
                    )
                }
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = coin.description,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = "Tags",
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(15.dp))
                FlowRow(
                    maxItemsInEachRow = 3,
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    coin.tags.forEach { tag ->
                        CoinTag(tag = tag)
                    }
                }
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = "Team members",
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(15.dp))
            }
            items(coin.team) { teamMember ->
                TeamListItem(
                    teamMember = teamMember,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                )
                Divider()
            }
        }
    }
}