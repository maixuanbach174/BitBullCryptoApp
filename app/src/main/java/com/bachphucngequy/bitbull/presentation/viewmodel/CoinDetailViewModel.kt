package com.bachphucngequy.bitbull.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bachphucngequy.bitbull.domain.model.CoinDetail
import com.bachphucngequy.bitbull.domain.repository.CoinRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.bachphucngequy.bitbull.data.Result
import dagger.hilt.android.lifecycle.HiltViewModel

class CoinDetailViewModel(
    private val repository: CoinRepository,
    private val coinId: String
) : ViewModel() {
    private val _coinDetail = MutableStateFlow(CoinDetail())
    val coinDetail = _coinDetail.asStateFlow()

    private val _showErrorToastChannel = Channel<Boolean>()
    val showErrorToastChannel = _showErrorToastChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            repository.getCoinById(coinId).collectLatest{
                    result ->
                when(result) {
                    is Result.Error -> {
                        _showErrorToastChannel.send(true)
                    }
                    is Result.Success ->{
                        result.data?.let {coinDetail->
                            _coinDetail.update { coinDetail }
                        }

                    }

                    is Result.Loading -> TODO()
                }
            }
        }
    }

}
