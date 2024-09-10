package com.bachphucngequy.bitbull.tweets.post

import android.annotation.SuppressLint
import android.icu.util.Calendar
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bachphucngequy.bitbull.tweets.common.util.Event
import com.bachphucngequy.bitbull.tweets.common.util.EventBus
import com.bachphucngequy.bitbull.tweets.data.Comment
import com.bachphucngequy.bitbull.tweets.data.Post
import com.bachphucngequy.bitbull.tweets.data.comments
import com.bachphucngequy.bitbull.tweets.data.posts
import com.bachphucngequy.bitbull.tweets.data.remote.remoteUsers
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Locale

class PostDetailViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val likesCollection = firestore.collection("postLikes")
    private val commentsCollection = firestore.collection("postComments")
    var postUiState by mutableStateOf(PostUiState())
        private set

    var commentsUiState by mutableStateOf(CommentsUiState())
        private set

    private val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH)
    init {
        EventBus.events
            .onEach {
                when (it) {
                    is Event.PostUpdated -> updatePost(it.post)
                }
            }
            .launchIn(viewModelScope)
    }

//    private suspend fun fetchPostComments(postId: Long) {
//        if (commentsUiState.isLoading || commentsUiState.comments.isNotEmpty()) {
//            return
//        }
//    }

    private fun fetchData(postId: String) {
        viewModelScope.launch {
            postUiState = postUiState.copy(
                isLoading = false,
                post = posts.find { it.id == postId }
            )
            commentsUiState = commentsUiState.copy(
                isLoading = false,
                comments = comments.filter { comment -> comment.postId == postId }
            )
        }
    }

    private fun updatePost(post: Post){
        postUiState = postUiState.copy(
            post = post
        )
    }

    private fun likeOrDislikePost(post: Post) {
        viewModelScope.launch {
            val count = if (post.isLiked) -1 else +1
            val updatedPost = post.copy(
                isLiked = !post.isLiked,
                likesCount = post.likesCount.plus(count)
            )

            updatePost(updatedPost)

            // Insert or delete like in Firestore
            if (!post.isLiked) {
                // User is liking the post
                insertLikeToFirestore(post.id)
            } else {
                // User is unliking the post
                deleteLikeFromFirestore(post.id)
            }

            EventBus.send(Event.PostUpdated(updatedPost))
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

//    private fun updatedPostsInDB(id: String, post: Post, count: Int) {
//        samplePosts = samplePosts.map {
//            if (it.id == id) {
//                it.copy(
//                    isLiked = !post.isLiked, // toggle
//                    likesCount = post.likesCount.plus(count)
//                )
//            } else it
//        }.toMutableList()
//    }

    private fun addNewComment(commentStr: String) {
        viewModelScope.launch {
            val post = postUiState.post ?: return@launch

            commentsUiState = commentsUiState.copy(isAddingNewComment = true)

            // TODO: Need to replace with update database
            val currentUser = remoteUsers.find { it.userId == auth.currentUser?.uid }
            val currentDate = sdf.format(Calendar.getInstance().time)
            val newComment = Comment(
                comment = commentStr,
                date = currentDate,
                // TODO: Need to replace with current user
                authorName = currentUser?.name ?: "User",
                authorId = currentUser?.userId ?: "",
                authorImageUrl = currentUser?.imageUrl ?: "",
                //
                postId = post.id
            )
            // Add to list of comments
            comments.add(newComment)

            // Update UI
            commentsUiState = commentsUiState.copy(
                comments = commentsUiState.comments + listOf(newComment),
                isAddingNewComment = false
            )

            // Insert comments in Firebase
            insertCommentFirebase(commentStr, currentDate, post.id)

            val updatedPost = post.copy(
                commentsCount = post.commentsCount.plus(1)
            )

            EventBus.send(Event.PostUpdated(updatedPost))
        }
    }

    @SuppressLint("LogNotTimber")
    private suspend fun insertCommentFirebase(commentStr: String, currentDate: String, postId: String) {
        try {
            auth.currentUser?.uid?.let { userUid ->
                val commentData = mapOf(
                    "content" to commentStr,
                    "createdAt" to currentDate,
                    "postId" to postId,
                    "userId" to userUid
                )
                commentsCollection
                    .add(commentData)
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

//    private fun updateCommentsInDB(commentStr: String, post : Post): Comment {
//        val newComment = Comment(
//            comment = commentStr,
//            date = sdf.format(Calendar.getInstance().time),
//            // TODO: Need to replace with current user
//            authorName = post.authorName,
//            authorId = post.authorId,
//            authorImageUrl = post.authorImageUrl,
//            //
//            postId = post.id
//        )
//        sampleComments.add(newComment)
//        return newComment
//    }


    fun onUiAction(action: PostDetailUiAction){
        when(action){
            is PostDetailUiAction.FetchPostAction -> {
                fetchData(action.postId)
            }
            is PostDetailUiAction.LikeOrDislikePostAction -> {
                likeOrDislikePost(action.post)
            }
            is PostDetailUiAction.AddCommentAction -> {
                addNewComment(action.comment)
            }
        }
    }
}

data class PostUiState(
    val isLoading: Boolean = true,
    val post: Post? = null,
    val errorMessage: String? = null
)

data class CommentsUiState(
    val isLoading: Boolean = true,
    val comments: List<Comment> = listOf(),
    val errorMessage: String? = null,
    val endReached: Boolean = false,
    val isAddingNewComment: Boolean = false
)

sealed interface PostDetailUiAction{
    data class FetchPostAction(val postId: String): PostDetailUiAction
    data class LikeOrDislikePostAction(val post: Post): PostDetailUiAction
    data class AddCommentAction(val comment: String) : PostDetailUiAction
}