package com.bachphucngequy.bitbull.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
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
    private val _state = mutableStateOf(CoinDetailState())
    val state: State<CoinDetailState> = _state

    init {
        viewModelScope.launch {
            repository.getCoinById(coinId).collectLatest{
                    result ->
                when(result) {
                    is Result.Error -> {
                        _state.value = CoinDetailState(
                            error = result.message ?: "An unexpected error occured"
                        )
                    }
                    is Result.Success ->{
                        result.data?.let {coinDetail->
                            _state.value = CoinDetailState(coin = coinDetail)
                        }
                    }
                    is Result.Loading -> {
                        _state.value = CoinDetailState(isLoading = true)
                    }
                }
            }
        }
    }

}

data class CoinDetailState(
    val isLoading: Boolean = false,
    val coin: CoinDetail? = null,
    val error: String = ""
)
