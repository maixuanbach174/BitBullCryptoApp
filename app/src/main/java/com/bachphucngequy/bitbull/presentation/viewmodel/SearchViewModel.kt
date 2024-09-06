package com.bachphucngequy.bitbull.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bachphucngequy.bitbull.data.entity.SymbolPriceTicker
import com.bachphucngequy.bitbull.data.repository.SymbolPriceTickerRepositoryImpl
import com.bachphucngequy.bitbull.domain.repository.SymbolPriceTickerRepository
import com.bachphucngequy.bitbull.retrofit.RetrofitInstance
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.bachphucngequy.bitbull.data.Result

class SearchViewModel(
    private val repository: SymbolPriceTickerRepository = SymbolPriceTickerRepositoryImpl(
        RetrofitInstance.binanceApi)
): ViewModel() {
    private val _ticker = MutableStateFlow(SymbolPriceTicker())

    private val _showErrorToastChannel = Channel<Boolean>()
    val showErrorToastChannel = _showErrorToastChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            repository.getSymbolPriceTicker().collectLatest{
                    result ->
                when(result) {
                    is Result.Error -> {
                        _showErrorToastChannel.send(true)
                    }
                    is Result.Success ->{
                        result.data?.let {ticker->
                            _ticker.update { ticker }
                        }

                    }

                    is Result.Loading -> TODO()
                }
            }
        }
    }


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
                tickers.filter {symbolPriceTickerItem->
                    symbolPriceTickerItem.price.toDouble() >= 0.01
                }.
                sortedByDescending { it.price.toDouble() }

            } else
            {
                tickers.filter {symbolPriceTickerItem->
                    symbolPriceTickerItem.doesMatchSearchQuery(text) &&
                            symbolPriceTickerItem.price.toDouble() >= 0.01
                }.sortedByDescending { it.price.toDouble() }
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



