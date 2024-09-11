package com.bachphucngequy.bitbull.history

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bachphucngequy.bitbull.history.Constants.TRANSACTION_COLLECTION
import com.bachphucngequy.bitbull.history.model.HistoryItem
import com.bachphucngequy.bitbull.history.model.Transaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class HistoryViewModel : ViewModel() {
    var uiState by mutableStateOf(HistoryUiState())
        private set

    private val firestore = FirebaseFirestore.getInstance()
    init {
        fetchHistory()
    }
    fun onUiAction(uiAction: HistoryUiAction) {
        when(uiAction) {
            is HistoryUiAction.TopTabSelect -> updateTabSelected(uiAction.newTabIndex)
        }
    }

    private fun getCurrentUserId(): String? {
        // Get the current user
        val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser

        // Return the user's ID or null if no user is signed in
        return user?.uid
    }

    private fun updateTabSelected(newTabIndex: Int) {
        viewModelScope.launch {
            uiState = uiState.copy(
                topTabIndex = newTabIndex
            )
        }
    }

    private fun fetchHistory() {
        viewModelScope.launch {
            val userId = getCurrentUserId()
            if (userId != null) {
                try {
                    val depositSnapshot = firestore.collection(TRANSACTION_COLLECTION)
                        .whereEqualTo("UserIDdep", userId)
                        .get()
                        .await()

                    val withdrawalSnapshot = firestore.collection(TRANSACTION_COLLECTION)
                        .whereEqualTo("userIDwith", userId)
                        .get()
                        .await()

                    val userDepositList = depositSnapshot.documents.mapNotNull { doc ->
                        doc.toObject(Transaction::class.java)
                    }

                    for(deposit in userDepositList) {
                        Log.d("Deposit", deposit.amount.toString())
                    }

                    val userWithdrawalList = withdrawalSnapshot.documents.mapNotNull { doc ->
                        doc.toObject(Transaction::class.java)
                    }

                    for(withdraw in userWithdrawalList) {
                        Log.d("Withdraw", withdraw.amount.toString())
                    }

                    val depositList = userDepositList.map {transaction ->
                        HistoryItem(
                            coinCode = transaction.currency,
                            date = "2024-09-06", // Replace with actual date conversion from Transaction if needed
                            amount = transaction.amount,
                            status = "Completed" // Or derive status based on your business logic
                        )
                    }

                    val withdrawalList = userWithdrawalList.map {transaction ->
                        HistoryItem(
                            coinCode = transaction.currency,
                            date = "2024-09-06", // Replace with actual date conversion from Transaction if needed
                            amount = transaction.amount.toDouble(),
                            status = "Completed" // Or derive status based on your business logic
                        )
                    }

                    uiState = uiState.copy(
                        isLoading = false,
                        depositList = depositList,
                        withdrawalList = withdrawalList
                    )

                } catch(e: Exception) {
                    uiState = uiState.copy(
                        isLoading = false,
                        errorMessage = e.localizedMessage
                    )

                    e.localizedMessage?.let { Log.d("Exception: ", it) }
                }
            } else {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = "User not logged in"
                )

                Log.d("Error: ", "User not logged in")
            }
        }
    }
}

data class HistoryUiState (
    val isLoading: Boolean = true,
    val topTabIndex: Int = 0,
    val depositList: List<HistoryItem> = listOf(),
    val withdrawalList: List<HistoryItem> = listOf(),
    val errorMessage: String? = null
)

sealed interface HistoryUiAction {
    data class TopTabSelect(val newTabIndex: Int): HistoryUiAction
}

val topTabs = listOf("Deposit", "Withdrawal", "Trade")