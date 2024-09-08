package com.bachphucngequy.bitbull.firebase

import com.google.firebase.firestore.FirebaseFirestore

object WithdrawBackend {
    fun fetchAvailableAmount(fee: Double, onResult: (Double?) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val currency = "USDT"

        db.collection("hold")
            .whereEqualTo("userID", user.usid)
            .whereEqualTo("currency", currency)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val amount = document.getDouble("amount")
                    if (amount != null) {
                        val finalAmount = maxOf(0.0, amount - fee)
                        onResult(finalAmount)
                    } else {
                        onResult(0.0)
                    }
                    return@addOnSuccessListener
                }
                onResult(null)
            }
            .addOnFailureListener {
                onResult(null)
            }
    }

    fun performWithdrawal(
        binanceId: String,
        withdrawAmount: Double,
        fee: Double,
        note: String,
        onSuccess: (Boolean) -> Unit,
        onError: (String) -> Unit
    ) {
        val db = FirebaseFirestore.getInstance()

        db.collection("user2")
            .whereEqualTo("userID", binanceId)
            .get()
            .addOnSuccessListener { userDocs ->
                if (userDocs.isEmpty) {
                    onError("Binance ID does not exist")
                    return@addOnSuccessListener
                }

                db.collection("hold")
                    .whereEqualTo("userID", user.usid)
                    .whereEqualTo("currency", "USDT")
                    .get()
                    .addOnSuccessListener { holdDocs ->
                        if (holdDocs.isEmpty) {
                            onError("No holdings found for the current user")
                            return@addOnSuccessListener
                        }
                        val userHoldDoc = holdDocs.documents.firstOrNull()
                        val userAvailableAmount = userHoldDoc?.getDouble("amount") ?: 0.0

                        if (withdrawAmount + fee > userAvailableAmount) {
                            onError("Insufficient funds")
                            return@addOnSuccessListener
                        }

                        db.collection("hold")
                            .whereEqualTo("userID", binanceId)
                            .whereEqualTo("currency", "USDT")
                            .get()
                            .addOnSuccessListener { recipientHoldDocs ->
                                if (recipientHoldDocs.isEmpty) {
                                    val recipientNewHold = hashMapOf(
                                        "userID" to binanceId,
                                        "currency" to "USDT",
                                        "amount" to withdrawAmount
                                    )

                                    db.collection("hold")
                                        .add(recipientNewHold)
                                        .addOnSuccessListener {

                                            val newUserAmount = userAvailableAmount - withdrawAmount - fee
                                            db.collection("hold")
                                                .document(userHoldDoc?.id ?: "")
                                                .update("amount", newUserAmount)
                                                .addOnSuccessListener {
                                                    onSuccess(true)
                                                }
                                                .addOnFailureListener {
                                                    onError("Failed to update user's available amount")
                                                }
                                        }
                                        .addOnFailureListener {
                                            onError("Failed to create recipient's holdings")
                                        }

                                } else {

                                    val recipientHoldDoc = recipientHoldDocs.documents.firstOrNull()
                                    val recipientAvailableAmount = recipientHoldDoc?.getDouble("amount") ?: 0.0

                                    val transaction = hashMapOf(
                                        "UserIDdep" to binanceId,
                                        "userIDwith" to user.usid,
                                        "currency" to "USDT",
                                        "amount" to withdrawAmount,
                                        "note" to note
                                    )

                                    db.collection("transaction")
                                        .add(transaction)
                                        .addOnSuccessListener {

                                            val newUserAmount = userAvailableAmount - withdrawAmount - fee
                                            db.collection("hold")
                                                .document(userHoldDoc?.id ?: "")
                                                .update("amount", newUserAmount)
                                                .addOnSuccessListener {


                                                    val newRecipientAmount = recipientAvailableAmount + withdrawAmount
                                                    db.collection("hold")
                                                        .document(recipientHoldDoc?.id ?: "")
                                                        .update("amount", newRecipientAmount)
                                                        .addOnSuccessListener {
                                                            onSuccess(true)
                                                        }
                                                        .addOnFailureListener {
                                                            onError("Failed to update recipient's available amount")
                                                        }
                                                }
                                                .addOnFailureListener {
                                                    onError("Failed to update user's available amount")
                                                }
                                        }
                                        .addOnFailureListener {
                                            onError("Failed to add transaction")
                                        }
                                }
                            }
                            .addOnFailureListener {
                                onError("Failed to fetch recipient's available amount")
                            }
                    }
                    .addOnFailureListener {
                        onError("Failed to fetch user's available amount")
                    }
            }
            .addOnFailureListener {
                onError("Failed to check Binance ID")
            }
    }



}
