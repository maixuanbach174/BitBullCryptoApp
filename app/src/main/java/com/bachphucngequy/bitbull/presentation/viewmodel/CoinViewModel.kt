package com.bachphucngequy.bitbull.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bachphucngequy.bitbull.domain.model.Coin
import com.bachphucngequy.bitbull.domain.repository.CoinRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.bachphucngequy.bitbull.data.Result

class CoinViewModel(
    private val repository: CoinRepository
) : ViewModel() {
    private val _coins = MutableStateFlow(List<Coin>(0){Coin()})
    val coin = _coins.asStateFlow()

    private val _showErrorToastChannel = Channel<Boolean>()
    val showErrorToastChannel = _showErrorToastChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            repository.getCoins().collectLatest{
                    result ->
                when(result) {
                    is Result.Error -> {
                        _showErrorToastChannel.send(true)
                    }
                    is Result.Success ->{
                        result.data?.let {coin->
                            _coins.update { coin}
                        }

                    }

                    is Result.Loading -> TODO()
                }
            }
        }
    }

}
