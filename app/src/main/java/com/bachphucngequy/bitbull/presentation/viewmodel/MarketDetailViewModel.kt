package com.bachphucngequy.bitbull.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class MarketDetailViewModel: ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val favouriteCoinCollection = firestore.collection("favourite")
    fun insertFavouriteToFirebase(cryptoSymbol: String) {
        viewModelScope.launch {
            val currentUserId = auth.currentUser?.uid

            if(currentUserId != null) {
                val favouriteData = mapOf(
                    "cryptoSymbol" to cryptoSymbol,
                    "userId" to currentUserId
                )

                try {
                    favouriteCoinCollection
                        .add(favouriteData)
                        .addOnSuccessListener { documentReference ->
                            Timber.e("Insert favourite successfully ${documentReference.id}")
                        }
                        .addOnFailureListener { e ->
                            Timber.e("Failed to insert favourite")
                        }
                        .await()
                } catch(e: Exception) {
                    Timber.e("Exception during insert favourite")
                }
            }
        }
    }

    fun deleteFavouriteToFirebase(cryptoSymbol: String) {
        viewModelScope.launch {
            val currentUserId = auth.currentUser?.uid
            if (currentUserId != null) {
                try {
                    val querySnapshot = favouriteCoinCollection
                        .whereEqualTo("cryptoSymbol", cryptoSymbol)
                        .whereEqualTo("userId", currentUserId)
                        .get()
                        .await()

                    for (document in querySnapshot.documents) {
                        favouriteCoinCollection.document(document.id).delete().await()
                    }
                } catch(e: Exception) {
                    Timber.e("Exception during delete favourite")
                }
            }
        }
    }
}