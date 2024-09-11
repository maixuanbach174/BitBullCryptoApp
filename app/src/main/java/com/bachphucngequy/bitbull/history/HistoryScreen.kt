package com.bachphucngequy.bitbull.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bachphucngequy.bitbull.history.components.HistoryListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier,
    uiState: HistoryUiState,
    onUiAction: (HistoryUiAction) -> Unit,
    navigateUp: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "History") },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back button"
                        )
                    }
                }
            )
        },
        content = {paddingValues ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                HistoryTabRow(
                    modifier = modifier,
                    uiState = uiState,
                    onUiAction = onUiAction
                )

                if(uiState.isLoading) {
                    LinearProgressIndicator(modifier = modifier.fillMaxWidth())
                }

                when(uiState.topTabIndex) {
                    0 -> LazyColumn {
                        items(uiState.depositList) {listItem ->
                            HistoryListItem(historyItem = listItem)
                        }
                    }

                    1 -> LazyColumn {
                        items(uiState.withdrawalList) {listItem ->
                            HistoryListItem(historyItem = listItem)
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun HistoryTabRow(
    modifier: Modifier = Modifier,
    uiState: HistoryUiState,
    onUiAction: (HistoryUiAction) -> Unit
) {
    TabRow(
        selectedTabIndex = uiState.topTabIndex,
        modifier = modifier.fillMaxWidth()
    ) {
        topTabs.forEachIndexed { index, text ->
            Tab(
                selected = uiState.topTabIndex == index,
                onClick = { onUiAction(HistoryUiAction.TopTabSelect(index)) },
                text = { Text(text = text, style = MaterialTheme.typography.titleSmall) }
            )
        }
    }
}

@Preview
@Composable
fun HistoryScreenPreview() {
    HistoryScreen(
        uiState = HistoryUiState(),
        onUiAction = {},
        navigateUp = {}
    )
}

