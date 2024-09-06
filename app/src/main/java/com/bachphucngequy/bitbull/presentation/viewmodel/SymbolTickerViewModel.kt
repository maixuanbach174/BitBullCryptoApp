package com.bachphucngequy.bitbull.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bachphucngequy.bitbull.data.entity.SymbolPriceTicker
import com.bachphucngequy.bitbull.domain.repository.SymbolPriceTickerRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.update
import com.bachphucngequy.bitbull.data.Result

class SymbolTickerViewModel(
    private val repository: SymbolPriceTickerRepository
) : ViewModel() {
    private val _ticker = MutableStateFlow(SymbolPriceTicker())
    val ticker = _ticker.asStateFlow()

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

}
