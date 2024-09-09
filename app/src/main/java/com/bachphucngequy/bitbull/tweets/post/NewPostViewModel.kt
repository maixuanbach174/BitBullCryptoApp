package com.bachphucngequy.bitbull.tweets.post

import android.annotation.SuppressLint
import android.icu.util.Calendar
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bachphucngequy.bitbull.tweets.data.Post
import com.bachphucngequy.bitbull.tweets.data.posts
import com.bachphucngequy.bitbull.tweets.data.remote.remotePosts
import com.bachphucngequy.bitbull.tweets.data.remote.remoteUsers
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Locale

class NewPostViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH)
    private val postsCollection = firestore.collection("posts")

    private fun createNewPost(postText: String, imageUrl: String) {
        viewModelScope.launch {
            val currentUser = remoteUsers.find { it.userId == auth.currentUser?.uid }
            val currentDate = sdf.format(Calendar.getInstance().time)
            val postId = (remotePosts.last().postId.toInt() + 1).toString()
            val newPost = Post(
                id = postId,
                text = postText,
                imageUrl = imageUrl,
                createdAt = currentDate,
                likesCount = 0,
                commentsCount = 0,
                authorId = currentUser?.userId ?: "",
                authorName = currentUser?.name ?: "User",
                authorImageUrl = currentUser?.imageUrl ?: "",
                isLiked = false,
                isOwnPost = true
            )
            posts.add(index = 0, newPost)

            // Insert new post to Firebase
            insertPostFirebase(
                caption = postText,
                createdAt = currentDate,
                imageUrl = imageUrl,
                postId = postId
            )
        }
    }
    @SuppressLint("LogNotTimber")
    private suspend fun insertPostFirebase(
        caption: String,
        createdAt: String,
        imageUrl: String,
        postId: String
    ) {
        try {
            auth.currentUser?.uid?.let { userUid ->
                val postData = mapOf(
                    "caption" to caption,
                    "createdAt" to createdAt,
                    "imageUrl" to imageUrl,
                    "postId" to postId,
                    "userId" to userUid
                )
                postsCollection
                    .add(postData)
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
    fun onUiAction(action: NewPostUiAction){
        when(action){
            is NewPostUiAction.AddPost -> createNewPost(action.postText, action.imageUrl)
        }
    }
}

sealed interface NewPostUiAction{
    data class AddPost(val postText: String, val imageUrl: String) : NewPostUiAction
}