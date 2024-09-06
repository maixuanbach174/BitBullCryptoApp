package com.bachphucngequy.bitbull.presentation.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bachphucngequy.bitbull.data.entity.SymbolPriceTickerItem
import com.bachphucngequy.bitbull.presentation.ui.components.search.SearchIconBar
import com.bachphucngequy.bitbull.presentation.viewmodel.SearchViewModel
import java.text.DecimalFormat

@Composable
fun SearchScreen(
    onCancelClick: () -> Unit,
    onSearchItemClick: (String) -> Unit
) {
    val searchModel = viewModel<SearchViewModel>()
    val searchText by searchModel.searchText.collectAsState()
    val cryptos by searchModel.tickers.collectAsState()
    val isSearching by searchModel.isSearching.collectAsState()
    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp),
        ) {
            SearchIconBar(searchText, searchModel::onSearchTextChange, onCancelClick)
            Spacer(modifier = Modifier.height(16.dp))
            if(isSearching) {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(cryptos) { crypto ->
                        SymbolTickerPriceItemCard(crypto, onClick = {
                            onSearchItemClick(crypto.symbol)
                        })
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

            }


        }
    }
}

@Composable
fun SymbolTickerPriceItemCard(
    symbolPriceTickerItem: SymbolPriceTickerItem,
    onClick : () -> Unit
){
    val dec = DecimalFormat("#,###.00")
    val roundedPrice = dec.format(symbolPriceTickerItem.price.toDouble())
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = symbolPriceTickerItem.symbol,
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text = roundedPrice,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }

}


