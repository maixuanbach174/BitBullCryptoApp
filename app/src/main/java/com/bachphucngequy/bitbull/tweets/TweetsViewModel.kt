package com.bachphucngequy.bitbull.tweets

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bachphucngequy.bitbull.tweets.common.util.Event
import com.bachphucngequy.bitbull.tweets.common.util.EventBus
import com.bachphucngequy.bitbull.tweets.data.Post
import com.bachphucngequy.bitbull.tweets.data.posts
import com.bachphucngequy.bitbull.tweets.data.remote.RemoteComment
import com.bachphucngequy.bitbull.tweets.data.remote.RemotePost
import com.bachphucngequy.bitbull.tweets.data.remote.RemotePostLike
import com.bachphucngequy.bitbull.tweets.data.remote.RemoteUser
import com.bachphucngequy.bitbull.tweets.data.remote.remoteComments
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class TweetsViewModel: ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val postsCollection = firestore.collection("posts")
    private val usersCollection = firestore.collection("user")
    private val likesCollection = firestore.collection("postLikes")
    private val commentsCollection = firestore.collection("postComments")
    var postsUiState by mutableStateOf(PostsUIState())
        private set

    init {
        fetchPostsFromFirebase()

        EventBus.events
            .onEach {
                when (it) {
                    is Event.PostUpdated -> updatePost(it.post)
                }
            }.launchIn(viewModelScope)
    }

    private fun fetchPostsFromFirebase() {
        viewModelScope.launch {
            try {
                val postSnapshot = postsCollection.get().await()
                val remotePosts = postSnapshot.documents.mapNotNull { it.toObject(RemotePost::class.java) }

                val userSnapShot = usersCollection.get().await()
                val remoteUsers = userSnapShot.documents.mapNotNull { it.toObject(RemoteUser::class.java) }

                // Fetch likes from Firebase
                val likesSnapshot = likesCollection.get().await()
                val remoteLikes = likesSnapshot.documents.mapNotNull { it.toObject(RemotePostLike::class.java) }

                // Fetch comments from Firebase
                val commentsSnapshot = commentsCollection.get().await()
                remoteComments = commentsSnapshot.documents.mapNotNull { it.toObject(RemoteComment::class.java) }

                // Create likesMap
                val likesMap: Map<String, List<String>> = remoteLikes
                    .groupBy { it.postId }
                    .mapValues { entry -> entry.value.map { it.userId } }

                // Create commentsMap
                val commentsMap: Map<String, List<String>> = remoteComments
                    .groupBy { it.postId }
                    .mapValues { entry -> entry.value.map { it.userId } }

                // Create a map of userId to RemoteUser for quick lookup
                val usersMap = remoteUsers.associateBy { it.userId }

                // Get current user ID from Firebase Authentication
                val currentUserId = auth.currentUser?.uid

                // Combine RemotePost and RemoteUser into Post objects
                posts = remotePosts.mapNotNull { remotePost ->
                    usersMap[remotePost.userId]?.let { remoteAuthor ->
                        val isLikedByCurrentUser = likesMap[remotePost.postId]?.contains(currentUserId)
                        val likesCount = likesMap[remotePost.postId]?.size
                        val commentsCount = commentsMap[remotePost.postId]?.size
                        mapToPost(remotePost, remoteAuthor, currentUserId, isLikedByCurrentUser, likesCount, commentsCount)
                    }
                }.toMutableList()

                postsUiState = postsUiState.copy(
                    isLoading = false,
                    posts = posts
                )

            } catch(e: Exception) {
                postsUiState = postsUiState.copy(
                    isLoading = false,
                    errorMessage = e.message
                )
            }
        }
    }

    private fun mapToPost(
        remotePost: RemotePost,
        remoteUser: RemoteUser,
        currentUserId: String?,
        isLikedByCurrentUser: Boolean?,
        likesCount: Int?,
        commentsCount: Int?
    ): Post {
        // Map RemotePost and RemoteUser to Post
        return Post(
            id = remotePost.postId,
            text = remotePost.caption,
            imageUrl = remotePost.imageUrl,
            createdAt = remotePost.createdAt,
            authorId = remoteUser.userId,
            authorName = remoteUser.name,
            authorImageUrl = remoteUser.imageUrl,
            likesCount = likesCount ?: 0,
            commentsCount = commentsCount ?: 0,
            isLiked = isLikedByCurrentUser ?: false,
            isOwnPost = currentUserId == remotePost.userId
        )
    }

//    private fun syncPostsWithCommentsCount() {
//        for(comment in sampleComments) {
//            samplePosts.find{it.id == comment.postId}?.let {post ->
//                post.commentsCount += 1
//            }
//        }
//    }

//    private fun fetchData() {
//        viewModelScope.launch {
//            postsUiState = postsUiState.copy(
//                isLoading = false,
//                posts = samplePosts
//            )
//        }
//    }

    private fun likeOrUnlikePost(post: Post) {
        viewModelScope.launch {
            val count = if (post.isLiked) -1 else +1

            postsUiState = postsUiState.copy(
                posts = postsUiState.posts.map {
                    if (it.id == post.id) {
                        it.copy(
                            isLiked = !post.isLiked,
                            likesCount = post.likesCount.plus(count)
                        )
                    } else it
                }
            )
            // TODO: Update posts in database
            // Insert or delete like in Firestore
            if (!post.isLiked) {
                // User is liking the post
                insertLikeToFirestore(post.id)
            } else {
                // User is unliking the post
                deleteLikeFromFirestore(post.id)
            }
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

    private suspend fun insertLikeToFirestore(postId: String) {
        try {
            auth.currentUser?.uid?.let { userUid ->
                val likeData = mapOf(
                    "postId" to postId,
                    "userId" to userUid
                )
                likesCollection.add(likeData).await()
            }
        } catch (e: Exception) {
            // Handle errors (e.g., log them or show an error message in the UI state)
            println("Failed to insert like in Firestore: ${e.message}")
        }
    }


//    private fun updatePostsInDB(post: Post, count: Int) {
//        samplePosts = samplePosts.map {
//            if (it.id == post.id) {
//                it.copy(
//                    isLiked = !post.isLiked, // toggle
//                    likesCount = post.likesCount.plus(count)
//                )
//            } else it
//        }.toMutableList()
//    }



    private fun updatePost(post: Post) {
        postsUiState = postsUiState.copy(
            posts = postsUiState.posts.map {
                if (it.id == post.id) post else it
            }
        )
    }

    fun onUiAction(uiAction: HomeUiAction) {
        when (uiAction) {
            is HomeUiAction.PostLikeAction -> likeOrUnlikePost(uiAction.post)
        }
    }
}

data class PostsUIState(
    val isLoading: Boolean = true,
    val posts: List<Post> = listOf(),
    val errorMessage: String? = null
)

sealed interface HomeUiAction {
    data class PostLikeAction(val post: Post) : HomeUiAction
}

data class TabItem (
    val title: String,
    val unselectedIcon: ImageVector,
    val selectedIcon: ImageVector
)

val tabItems = listOf(
    TabItem(
        title = "For you",
        unselectedIcon = Icons.Outlined.AccountCircle,
        selectedIcon = Icons.Filled.AccountCircle
    ),
    TabItem(
        title = "Following",
        unselectedIcon = Icons.Outlined.Favorite,
        selectedIcon = Icons.Filled.Favorite
    )
)