package com.bachphucngequy.bitbull.firebase

import com.google.firebase.firestore.FirebaseFirestore

object FirebaseRepository {
    private val firestore = FirebaseFirestore.getInstance()

    // Function to fetch the available balance without using await
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
}
