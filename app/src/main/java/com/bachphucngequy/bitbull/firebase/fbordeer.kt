package com.bachphucngequy.bitbull.firebase

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.suspendCancellableCoroutine
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.coroutines.resumeWithException

object FirebaseRepository {
    private val firestore = FirebaseFirestore.getInstance()


    /**
     * Function to fetch the available balance without UI/Compose dependencies
     * This is a backend function for handling balance retrieval
     */
    fun fetchAvailableBalance(
        userId: String,
        currency: String,
        onSuccess: (Double) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firestore.collection("hold")
            .whereEqualTo("userID", userId)
            .whereEqualTo("currency", currency)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    onSuccess(0.0) // Return 0.0 if no documents found
                } else {
                    // Retrieve the amount field from the first document
                    val amount = querySnapshot.documents[0].getDouble("amount")
                    onSuccess(amount ?: 0.0) // Return the amount if available, or 0.0 if null
                }
            }
            .addOnFailureListener { e ->
                onFailure(e) // Handle any exceptions and pass the error to the callback
            }
    }

    /**
     * Function to update balances for buy and sell currencies
     * This function operates without UI/Compose dependencies
     */
    fun updateBalances(
        userId: String,
        buyCurrency: String,
        sellCurrency: String,
        newAmountBuyCurrency: Double,
        newAmountSellCurrency: Double,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        // Update or create the buy currency balance
        firestore.collection("hold")
            .whereEqualTo("userID", userId)
            .whereEqualTo("currency", buyCurrency)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    // Create a new document for the buy currency
                    val newBuyData = mapOf(
                        "userID" to userId,
                        "currency" to buyCurrency,
                        "amount" to newAmountBuyCurrency
                    )
                    firestore.collection("hold").add(newBuyData)
                        .addOnSuccessListener {
                            // Continue with the sell currency
                            updateOrCreateSellCurrency(
                                userId,
                                sellCurrency,
                                newAmountSellCurrency,
                                onSuccess,
                                onFailure
                            )
                        }
                        .addOnFailureListener { e ->
                            onFailure(e)
                        }
                } else {
                    // Update the existing document for the buy currency
                    val buyDocument = querySnapshot.documents[0]
                    firestore.collection("hold").document(buyDocument.id)
                        .update("amount", newAmountBuyCurrency)
                        .addOnSuccessListener {
                            // Continue with the sell currency
                            updateOrCreateSellCurrency(
                                userId,
                                sellCurrency,
                                newAmountSellCurrency,
                                onSuccess,
                                onFailure
                            )
                        }
                        .addOnFailureListener { e ->
                            onFailure(e)
                        }
                }
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }
    fun addOrder(
        userId: String,
        amount: Double,
        price: Double,
        btcCurrency: String,
        usdtCurrency: String,
        isBuy: Boolean,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val orderType = if (isBuy) "Buy" else "Sell"
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = dateFormat.format(Date())
        val transactionData = mapOf(
            "userID" to userId,
            "amount" to amount,
            "price" to price,
            "t1" to btcCurrency,
            "t2" to usdtCurrency,
            "type" to orderType,
            "date" to currentDate
        )

        firestore.collection("order")
            .add(transactionData)
            .addOnSuccessListener {

                onSuccess()
            }
            .addOnFailureListener { exception ->

                onFailure(exception)
            }
    }

    private fun updateOrCreateSellCurrency(
        userId: String,
        sellCurrency: String,
        newAmountSellCurrency: Double,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firestore.collection("hold")
            .whereEqualTo("userID", userId)
            .whereEqualTo("currency", sellCurrency)
            .get()
            .addOnSuccessListener { secondSnapshot ->
                if (secondSnapshot.isEmpty) {
                    // Create a new document for the sell currency
                    val newSellData = mapOf(
                        "userID" to userId,
                        "currency" to sellCurrency,
                        "amount" to newAmountSellCurrency
                    )
                    firestore.collection("hold").add(newSellData)
                        .addOnSuccessListener {
                            onSuccess()
                        }
                        .addOnFailureListener { e ->
                            onFailure(e)
                        }
                } else {

                    val sellDocument = secondSnapshot.documents[0]
                    firestore.collection("hold").document(sellDocument.id)
                        .update("amount", newAmountSellCurrency)
                        .addOnSuccessListener {
                            onSuccess()
                        }
                        .addOnFailureListener { e ->
                            onFailure(e)
                        }
                }
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }
}
object OrderManager {

    fun handleOrder(
        currentAmountBuyCurrency: Double,
        currentAmountSellCurrency: Double,
        inputAmount: Double,
        price: Double,
        isBuy: Boolean
    ): Pair<Double, Double> {
        return if (isBuy) {
            val newAmountSellCurrency = currentAmountSellCurrency - inputAmount
            Log.d("OrderDebug1", "Current Amount Buy Currency: $currentAmountBuyCurrency")
            Log.d("OrderDebug1", "Current Amount Sell Currency: $currentAmountSellCurrency")
            val newAmountBuyCurrency = currentAmountBuyCurrency + inputAmount / price
            Log.d("OrderDebug1", "3: $newAmountBuyCurrency")
            Log.d("OrderDebug1", "4: $newAmountSellCurrency")
            Pair(newAmountBuyCurrency, newAmountSellCurrency)

        } else {
            val newAmountBuyCurrency = currentAmountBuyCurrency - inputAmount
            val newAmountSellCurrency = currentAmountSellCurrency + inputAmount * price
            Pair(newAmountBuyCurrency, newAmountSellCurrency)
        }
    }
}