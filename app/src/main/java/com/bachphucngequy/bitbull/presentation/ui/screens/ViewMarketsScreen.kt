package com.bachphucngequy.bitbull.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bachphucngequy.bitbull.presentation.ui.components.home.CryptoList
import com.bachphucngequy.bitbull.presentation.ui.components.home.sampleData

@Composable
fun ViewMarketScreen(
    onNavigateToMarketDetail: () -> Unit
) {
    var isSearchVisible by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf(TextFieldValue()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        TopAppBar(
            isSearchVisible = isSearchVisible,
            onSearchClick = { isSearchVisible = !isSearchVisible },
            onSearchQueryChange = { searchQuery = it }
        )
        if (isSearchVisible) {
            SearchBar(
                searchQuery = searchQuery,
                onQueryChange = { searchQuery = it }
            )
        }

        TopMetrics()
        SortT()
        SortOptions()
        CryptoList(cryptoData = sampleData)
    }
}

@Composable
fun TopAppBar(
    isSearchVisible: Boolean,
    onSearchClick: () -> Unit,
    onSearchQueryChange: (TextFieldValue) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Markets",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        IconButton(onClick = onSearchClick) {
            Icon(
                Icons.Outlined.Search,
                contentDescription = "Search",
                tint = Color.White
            )
        }
    }
}

@Composable
fun SearchBar(
    searchQuery: TextFieldValue,
    onQueryChange: (TextFieldValue) -> Unit
) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onQueryChange,
        placeholder = { Text("Search") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color(0xFF1E1E1E)),
        textStyle = TextStyle(color = Color.White),
    )
}

@Composable
fun TopMetrics() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        MetricItem("24h Volume", "$976,62M")
        MetricItem("Open Interest", "$95,54M")
        MetricItem("Trades", "404.143")
    }
}

@Composable
fun MetricItem(title: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = title, color = Color.Gray, fontSize = 14.sp)
        Text(text = value, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun SortT() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Button(onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2B2B2B))) {
            Text("All", color = Color.White)
        }
        Spacer(modifier = Modifier.width(5.dp))
        Button(onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2B2B2B))) {
            Text("Favorites", color = Color.White)
        }
        Spacer(modifier = Modifier.width(5.dp))
        Button(onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2B2B2B))) {
            Text("Layer 1", color = Color.White)
        }
        Spacer(modifier = Modifier.width(5.dp))
        Button(onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2B2B2B))) {
            Text("Defi", color = Color.White)
        }
    }
}

@Composable
fun SortOptions() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Sort by",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier
                .weight(1.5f)
                .padding(end = 20.dp)
        )
        Text(
            text = "Volume",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier
                .clickable {}
                .weight(1f)
        )
        Text(
            text = "Gainers",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier
                .clickable {}
                .weight(1f)
        )
        Text(
            text = "Losers",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier
                .clickable {}
                .weight(1f)
        )
        Text(
            text = "Funding",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier
                .clickable {}
                .weight(1f)
        )
        Text(
            text = "Price",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier
                .clickable {}
                .weight(1f)
        )
    }
}

