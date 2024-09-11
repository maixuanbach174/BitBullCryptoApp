package com.bachphucngequy.bitbull.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bachphucngequy.bitbull.core.di.annotation.HiltDispatchers
import com.bachphucngequy.bitbull.domain.model.ConnectionState
import com.bachphucngequy.bitbull.domain.model.Ticker
import com.bachphucngequy.bitbull.domain.usecase.GetTickersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TickerViewModel @Inject constructor(
    val getTickersUseCase: GetTickersUseCase,
    @HiltDispatchers.IO private val coroutineDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val combinedPrices = mutableMapOf<String, Ticker>()

    private val _uiState = MutableStateFlow<TickerUiState>(TickerUiState(data = emptyList()))
    val uiState = _uiState.asStateFlow()

    init {
        Timber.e("Init VM")
    }

    fun getCryptos(symbol: String? = null) {
        viewModelScope.launch(coroutineDispatcher) {
            getTickersUseCase()
                .onStart {
                    _uiState.update { it.copy(isLoading = true) }
                }
                .onEach { event ->
                    when (event) {
                        is ConnectionState.Connected -> {
                            _uiState.update {
                                it.copy(isLoading = false, isOnline = true)
                            }
                            Timber.e("Connected")
                        }
                        is ConnectionState.Success -> {
                            combinedPrices[event.data.symbol] = event.data
                            _uiState.update { tickerState ->
                                val list = if (symbol != null) {
                                    combinedPrices.values.filter { ticker -> ticker.symbol == symbol }
                                        .toList()
                                } else combinedPrices.values.toList()

                                tickerState.copy(data = list)
                            }

                        }
                        else -> {
                            _uiState.update {
                                it.copy(isLoading = false, isOnline = false)
                            }
                        }
                    }
                }.collect()
        }
    }

}


data class TickerUiState(
    val data: List<Ticker>,
    val isLoading: Boolean = true,
    val isOnline: Boolean = false,
    val error: String = ""
)
