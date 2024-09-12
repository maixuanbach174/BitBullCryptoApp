package com.bachphucngequy.bitbull.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bachphucngequy.bitbull.core.di.annotation.HiltDispatchers
import com.bachphucngequy.bitbull.domain.model.ConnectionState
import com.bachphucngequy.bitbull.domain.model.RemoteFavourite
import com.bachphucngequy.bitbull.domain.model.Ticker
import com.bachphucngequy.bitbull.domain.usecase.GetTickersUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TickerViewModel @Inject constructor(
    val getTickersUseCase: GetTickersUseCase,
    @HiltDispatchers.IO private val coroutineDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val favouriteCoinCollection = firestore.collection("favourite")

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

    private fun fetchFavouriteCoinsFromFirebase(): List<String> {
        var favouriteCoinList: List<String> = listOf()
        viewModelScope.launch {
            val currentUserId = auth.currentUser?.uid

            if(currentUserId != null) {
                val favouriteSnapshot =
                    favouriteCoinCollection
                        .whereEqualTo("userId", currentUserId)
                        .get()
                        .await()

                val remoteFavouriteList = favouriteSnapshot.documents.mapNotNull {doc ->
                    doc.toObject(RemoteFavourite::class.java)
                }

                favouriteCoinList = remoteFavouriteList.map { it.favouriteCoinSymbol }
            }
        }
        return favouriteCoinList
    }
}


data class TickerUiState(
    val data: List<Ticker>,
    val isLoading: Boolean = true,
    val isOnline: Boolean = false,
    val error: String = ""
)
