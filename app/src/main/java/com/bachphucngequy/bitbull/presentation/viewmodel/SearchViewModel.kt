package com.bachphucngequy.bitbull.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import com.bachphucngequy.bitbull.data.entity.Crypto

class SearchViewModel(): ViewModel() {
    private val _ticker = MutableStateFlow( Crypto.values().toList())

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    @OptIn(FlowPreview::class)
    val tickers = searchText
        .debounce(1000L)
        .onEach { _isSearching.update { true } }
        .combine(_ticker) {
                text,tickers ->
            if(text.isBlank()) {
                tickers
            } else {
                tickers.filter { it.symbol.contains(text, ignoreCase = true) || it.fullName.contains(text, ignoreCase = true) }
            }

        }
        .onEach { _isSearching.update { false } }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _ticker.value
        )

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }
}



