package com.bachphucngequy.bitbull.tweets.account.profile

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bachphucngequy.bitbull.tweets.common.util.Event
import com.bachphucngequy.bitbull.tweets.common.util.EventBus
import com.bachphucngequy.bitbull.tweets.data.Post
import com.bachphucngequy.bitbull.tweets.data.Profile
import com.bachphucngequy.bitbull.tweets.data.posts
import com.bachphucngequy.bitbull.tweets.data.profiles
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProfileViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val followsCollection = firestore.collection("follows")
    private val likesCollection = firestore.collection("postLikes")
    var userInfoUiState by mutableStateOf(UserInfoUiState())
        private set

    var profilePostsUiState by mutableStateOf(ProfilePostsUiState())
        private set

    private fun fetchProfile(userId: String) {
        viewModelScope.launch {
            userInfoUiState = userInfoUiState.copy(
                isLoading = false,
                profile = profiles.find{it.id == userId}
            )
            profilePostsUiState = profilePostsUiState.copy(
                isLoading = false,
                posts = posts.filter { post -> post.authorId == userId }
            )
        }
    }

//    private fun syncProfilesWithFollows() {
//        for(follows in sampleFollowRelationship) {
//            sampleProfiles.find { it.id == follows.fromUserId }?.let {profile ->
//                profile.followingCount += 1
//            }
//            sampleProfiles.find { it.id == follows.toUserId }?.let {profile ->
//                profile.followersCount += 1
//            }
//        }
//    }

    private fun followUser(profile: Profile){
        viewModelScope.launch {
            val count = if (profile.isFollowing) -1 else +1
            userInfoUiState = userInfoUiState.copy(
                profile = userInfoUiState.profile?.copy(
                    isFollowing = !profile.isFollowing, // toggle
                    followersCount = profile.followersCount.plus(count)
                )
            )
            // TODO: Need to replace with update database
            if(!profile.isFollowing) {
                // Current user is following this user
                insertFollowToFirebase(profile.id)
            } else {
                // Current user is unfollowing this user
                deleteFollowFromFirebase(profile.id)
            }
        }
    }

    private suspend fun deleteFollowFromFirebase(profileId: String) {
        try {
            auth.currentUser?.uid?.let { userUid ->
                // Find the like document by fromId and toId
                val querySnapshot = followsCollection
                    .whereEqualTo("fromId", userUid)
                    .whereEqualTo("toId", profileId)
                    .get()
                    .await()

                // Delete each matching document (there should be only one)
                for (document in querySnapshot.documents) {
                    followsCollection.document(document.id).delete().await()
                }
            }
        } catch (e: Exception) {
            // Handle errors
            println("Failed to delete follow in Firestore: ${e.message}")
        }
    }

    @SuppressLint("LogNotTimber")
    private suspend fun insertFollowToFirebase(profileId: String) {
        try {
            auth.currentUser?.uid?.let { userUid ->
                val followData = mapOf(
                    "fromId" to userUid,
                    "toId" to profileId
                )
                followsCollection
                    .add(followData)
                    .addOnSuccessListener { documentReference ->
                        Log.d("Success", "DocumentSnapshot written with ID: ${documentReference.id}")
                    }
                    .addOnFailureListener { e ->
                        Log.w("Error", "Error adding document", e)
                    }
                    .await()
            }
        } catch (e: Exception) {
            // Handle errors (e.g., log them or show an error message in the UI state)
            println("Failed to insert like in Firestore: ${e.message}")
        }
    }

//    private fun updateFollowRelationships(fromUserId: String, toUserId: String, isFollowing: Boolean) {
//        if(isFollowing) {
//            sampleFollowRelationship.add(
//                FollowRelationship(
//                    fromUserId = fromUserId,
//                    toUserId = toUserId
//                )
//            )
//        }
//        else {
//            sampleFollowRelationship.remove(
//                FollowRelationship(
//                    fromUserId = fromUserId,
//                    toUserId = toUserId
//                )
//            )
//        }
//    }

    private fun likeOrUnlikePost(post: Post) {
        viewModelScope.launch {
            val count = if (post.isLiked) -1 else +1

            profilePostsUiState = profilePostsUiState.copy(
                posts = profilePostsUiState.posts.map {
                    if (it.id == post.id) {
                        it.copy(
                            isLiked = !post.isLiked,
                            likesCount = post.likesCount.plus(count)
                        )
                    } else it
                }
            )
            // Insert or delete like in Firestore
            if (!post.isLiked) {
                // User is liking the post
                insertLikeToFirestore(post.id)
            } else {
                // User is unliking the post
                deleteLikeFromFirestore(post.id)
            }
            val updatedPost = post.copy(
                isLiked = !post.isLiked,
                likesCount = post.likesCount.plus(count)
            )
            EventBus.send(Event.PostUpdated(updatedPost))
        }
    }

    private suspend fun deleteLikeFromFirestore(postId: String) {
        try {
            auth.currentUser?.uid?.let { userUid ->
                // Find the like document by postId and userId
                val querySnapshot = likesCollection
                    .whereEqualTo("postId", postId)
                    .whereEqualTo("userId", userUid)
                    .get()
                    .await()

                // Delete each matching document (there should be only one)
                for (document in querySnapshot.documents) {
                    likesCollection.document(document.id).delete().await()
                }
            }
        } catch (e: Exception) {
            // Handle errors
            println("Failed to delete like in Firestore: ${e.message}")
        }
    }

    @SuppressLint("LogNotTimber")
    private suspend fun insertLikeToFirestore(postId: String) {
        try {
            auth.currentUser?.uid?.let { userUid ->
                val likeData = mapOf(
                    "postId" to postId,
                    "userId" to userUid
                )
                likesCollection
                    .add(likeData)
                    .addOnSuccessListener { documentReference ->
                        Log.d("Success", "DocumentSnapshot written with ID: ${documentReference.id}")
                    }
                    .addOnFailureListener { e ->
                        Log.w("Error", "Error adding document", e)
                    }
                    .await()
            }
        } catch (e: Exception) {
            // Handle errors (e.g., log them or show an error message in the UI state)
            println("Failed to insert like in Firestore: ${e.message}")
        }
    }

//    private fun likeOrDislikePost(post: Post) {
//        viewModelScope.launch {
//            val count = if (post.isLiked) -1 else +1
//            val updatedPost = post.copy(
//                isLiked = !post.isLiked,
//                likesCount = post.likesCount.plus(count)
//            )
//
//            profilePostsUiState = profilePostsUiState.copy(
//                posts = profilePostsUiState.posts.map {
//                    if (it.id == updatedPost.id) updatedPost else it
//                }
//            )
//
//
//            updatedPostsInDB(post, count)
//
//            EventBus.send(Event.PostUpdated(updatedPost))
//        }
//    }
//
//    private fun updatedPostsInDB(post: Post, count: Int) {
//        samplePosts = samplePosts.map {
//            if (it.id == post.id) {
//                it.copy(
//                    isLiked = !post.isLiked, // toggle
//                    likesCount = post.likesCount.plus(count)
//                )
//            } else it
//        }.toMutableList()
//    }

    fun onUiAction(uiAction: ProfileUiAction) {
        when (uiAction) {
            is ProfileUiAction.FetchProfileAction -> fetchProfile(uiAction.profileId)
            is ProfileUiAction.FollowUserAction -> followUser(uiAction.profile)
            is ProfileUiAction.PostLikeAction -> likeOrUnlikePost(uiAction.post)
        }
    }
}

data class UserInfoUiState(
    val isLoading: Boolean = true,
    val profile: Profile? = null,
    val errorMessage: String? = null
)

data class ProfilePostsUiState(
    val isLoading: Boolean = true,
    val posts: List<Post> = listOf(),
    val errorMessage: String? = null,
    val endReached: Boolean = false
)

sealed interface ProfileUiAction {
    data class FetchProfileAction(val profileId: String) : ProfileUiAction
    data class FollowUserAction(val profile: Profile) : ProfileUiAction
    data class PostLikeAction(val post: Post) : ProfileUiAction
}