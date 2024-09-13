package com.bachphucngequy.bitbull.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bachphucngequy.bitbull.core.di.annotation.HiltDispatchers
import com.bachphucngequy.bitbull.data.entity.Crypto
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

    private val _uiState = MutableStateFlow<TickerUiState>(TickerUiState(data = emptyList(), displayIdList = emptyList()))
    val uiState = _uiState.asStateFlow()

    init {
        Timber.e("Init VM")

        fetchFavouriteCoinsFromFirebase()
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

    private fun fetchFavouriteCoinsFromFirebase() {
        viewModelScope.launch {
            val currentUserId = auth.currentUser?.uid

            if(currentUserId != null) {
                try {
                    val favouriteSnapshot =
                        favouriteCoinCollection
                            .whereEqualTo("userId", currentUserId)
                            .get()
                            .await()

                    val remoteFavouriteList = favouriteSnapshot.documents.mapNotNull {doc ->
                        doc.toObject(RemoteFavourite::class.java)
                    }

                    if (remoteFavouriteList.isEmpty()) {
                        // Select the first 5 cryptos as default favorites
                        val defaultFavouriteCryptos = Crypto.values().take(5)

                        // Create a list of RemoteFavourite objects for default cryptos
                        val defaultFavouriteList = defaultFavouriteCryptos.map { crypto ->
                            RemoteFavourite(cryptoSymbol = crypto.symbol, userId = currentUserId)
                        }

                        // Batch write the default favourites to the Firestore collection
                        val batch = FirebaseFirestore.getInstance().batch()

                        defaultFavouriteList.forEach { favourite ->
                            val docRef = favouriteCoinCollection.document() // create a new document reference
                            batch.set(docRef, favourite)
                        }

                        // Commit the batch write
                        batch.commit().await()

                        // Update the isFavourite status in the enum
                        defaultFavouriteCryptos.forEach { crypto ->
                            crypto.isFavourite = true
                        }

                        Timber.e("Create default favourite successfully")
                    } else {
                        // Get the list of favorite symbols
                        val favouriteSymbols = remoteFavouriteList.map { it.cryptoSymbol }

                        // Update the isFavourite status of each Crypto based on the fetched data
                        Crypto.values().forEach { crypto ->
                            crypto.isFavourite = favouriteSymbols.contains(crypto.symbol)
                        }
                        Timber.e("Fetch favourite successfully")
                    }

                    // Get displayIdList for favourite cryptos
                    val displayIds = Crypto.values()
                        .filter { it.isFavourite }  // Filter cryptos where isFavourite is true
                        .map { it.symbol }           // Map to their symbols

                    _uiState.update { tickerState ->
                        tickerState.copy(
                            displayIdList = displayIds
                        )
                    }

                } catch(e: Exception) {
                    Timber.e("Exception during fetch favourite")
                }
            }
        }
    }


}


data class TickerUiState(
    val data: List<Ticker>,
    val displayIdList: List<String>,
    val isLoading: Boolean = true,
    val isOnline: Boolean = false,
    val error: String = ""
)
