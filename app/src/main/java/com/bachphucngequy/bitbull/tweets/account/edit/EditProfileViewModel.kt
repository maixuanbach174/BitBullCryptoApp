package com.bachphucngequy.bitbull.tweets.account.edit

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bachphucngequy.bitbull.tweets.common.util.Event
import com.bachphucngequy.bitbull.tweets.common.util.EventBus
import com.bachphucngequy.bitbull.tweets.data.Profile
import com.bachphucngequy.bitbull.tweets.data.posts
import com.bachphucngequy.bitbull.tweets.data.profiles
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class EditProfileViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val usersCollection = firestore.collection("user")
    var uiState: EditProfileUiState by mutableStateOf(EditProfileUiState())
        private set

    fun fetchProfile(userId: String){
        viewModelScope.launch {
            uiState = uiState.copy(
                isLoading = false,
                profile = profiles.find { it.id == userId }
            )
        }
    }

    fun uploadProfile(){

        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)

            // Change profile in Firebase
            changeProfileInFirebase()

        }
    }

    @SuppressLint("LogNotTimber")
    private suspend fun changeProfileInFirebase() {
        val currentUser = auth.currentUser

        // Check if the current user is authenticated
        if (currentUser == null) {
            uiState = uiState.copy(
                isLoading = false,
                uploadSucceed = false,
                errorMessage = "User not authenticated."
            )
            return
        }

        val bio = uiState.profile?.bio ?: ""
        val imageUrl = uiState.profile?.profileUrl ?: ""
        val name = uiState.profile?.name ?: ""
        val userId = currentUser.uid

        // Prepare the data to update
        val updatedProfileData = mapOf(
            "bio" to bio,
            "imageUrl" to imageUrl,
            "name" to name,
            "userId" to userId
        )

        try {
            // Query Firebase to find the document where userId matches current user's ID
            val querySnapshot: QuerySnapshot = usersCollection
                .whereEqualTo("userId", userId)
                .get()
                .await()
            if (querySnapshot.documents.isNotEmpty()) {
                // Assuming there's only one document per user, get the first match
                val document = querySnapshot.documents.first()

                // Update the user's profile in Firestore
                usersCollection.document(document.id)
                    .update(updatedProfileData)
                    .addOnSuccessListener {
                        Log.d("Success", "Edit profile successfully to Firebase")
                    }
                    .addOnFailureListener {
                        Log.d("Error", "Cannot edit profile in Firebase")
                    }
                    .await()

                // Update list of profiles
                profiles = profiles.map {
                    if(it.id == userId) {
                        it.copy(
                            bio = bio,
                            profileUrl = imageUrl,
                            name = name
                        )
                    } else it
                }

                // Update list of posts
                posts = posts.map {
                    if(it.authorId == userId) {
                        it.copy(
                            authorName = name,
                            authorImageUrl = imageUrl
                        )
                    } else it
                }.toMutableList()

                // Update the local UI state
                uiState = uiState.copy(
                    isLoading = false,
                    uploadSucceed = true,
                    errorMessage = null
                )
            } else {
                uiState = uiState.copy(
                    isLoading = false,
                    uploadSucceed = false,
                    errorMessage = "User profile not found."
                )
            }

        } catch (e: Exception) {
            // Handle any errors that occur during the Firestore update
            uiState = uiState.copy(
                isLoading = false,
                uploadSucceed = false,
                errorMessage = e.message
            )
        }
    }

    fun onNameChange(inputName: String){
        uiState = uiState.copy(
            profile = uiState.profile?.copy(name = inputName)
        )
    }

    fun onBioChange(inputBio: String){
        uiState = uiState.copy(
            profile = uiState.profile?.copy(bio = inputBio)
        )
    }

    fun onImageUrlChange(inputImageUrl: String){
        uiState = uiState.copy(
            profile = uiState.profile?.copy(profileUrl = inputImageUrl)
        )
    }
}

data class EditProfileUiState(
    val isLoading: Boolean = true,
    val profile: Profile? = null,
    val uploadSucceed: Boolean = false,
    val errorMessage: String? = null
)