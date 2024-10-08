package com.bachphucngequy.bitbull.tweets

import android.annotation.SuppressLint
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
import com.bachphucngequy.bitbull.tweets.data.Profile
import com.bachphucngequy.bitbull.tweets.data.comments
import com.bachphucngequy.bitbull.tweets.data.posts
import com.bachphucngequy.bitbull.tweets.data.profiles
import com.bachphucngequy.bitbull.tweets.data.remote.RemoteComment
import com.bachphucngequy.bitbull.tweets.data.remote.RemoteFollows
import com.bachphucngequy.bitbull.tweets.data.remote.RemotePost
import com.bachphucngequy.bitbull.tweets.data.remote.RemotePostLike
import com.bachphucngequy.bitbull.tweets.data.remote.RemoteUser
import com.bachphucngequy.bitbull.tweets.data.remote.remoteComments
import com.bachphucngequy.bitbull.tweets.data.remote.remoteFollows
import com.bachphucngequy.bitbull.tweets.data.remote.remoteLikes
import com.bachphucngequy.bitbull.tweets.data.remote.remotePosts
import com.bachphucngequy.bitbull.tweets.data.remote.remoteUsers
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
    private val followsCollection = firestore.collection("follows")
    private val commentsCollection = firestore.collection("postComments")
    var postsUiState by mutableStateOf(PostsUIState())
        private set

    init {
        EventBus.events
            .onEach {
                when (it) {
                    is Event.PostUpdated -> updatePost(it.post)
                }
            }.launchIn(viewModelScope)
    }

    private fun fetchPostsFromFirebase() {
        postsUiState = postsUiState.copy(
            isLoading = true,
            posts = posts
        )
        viewModelScope.launch {
            try {
                // Get current user ID from Firebase Authentication
                val currentUserId = auth.currentUser?.uid

                val postSnapshot = postsCollection.get().await()
                remotePosts = postSnapshot.documents.mapNotNull { it.toObject(RemotePost::class.java) }

                for(remotePost in remotePosts) {
                    Log.d("Post id", remotePost.postId)
                }
                val userSnapShot = usersCollection.get().await()
                remoteUsers = userSnapShot.documents.mapNotNull { it.toObject(RemoteUser::class.java) }
                for(remoteUser in remoteUsers) {
                    Log.d("User id", remoteUser.userId)
                }
                // Create current user in firebase if not exists
                createUserIfNotExists()

                // Fetch likes from Firebase
                val likesSnapshot = likesCollection.get().await()
                remoteLikes = likesSnapshot.documents.mapNotNull { it.toObject(RemotePostLike::class.java) }
                for(like in remoteLikes) {
                    Log.d("Like for post", like.postId)
                }
                // Fetch comments from Firebase
                val commentsSnapshot = commentsCollection.get().await()
                remoteComments = commentsSnapshot.documents.mapNotNull { it.toObject(RemoteComment::class.java) }
                for(comment in remoteComments) {
                    Log.d("Comment for post", comment.postId)
                }
                // Fetch remote follows
                val followsSnapshot = followsCollection.get().await()
                remoteFollows = followsSnapshot.documents.mapNotNull { it.toObject(RemoteFollows::class.java) }

                // Create likesMap
                val likesMap: Map<String, List<String>> =
                    remoteLikes
                    .groupBy { it.postId }
                    .mapValues { entry -> entry.value.map { it.userId } }

                // Create commentsMap that map postId to list of userId that comments
                // on that post
                val commentsMap: Map<String, List<String>> = remoteComments
                    .groupBy { it.postId }
                    .mapValues { entry -> entry.value.map { it.userId } }

                // Create a map of userId to RemoteUser for quick lookup
                val usersMap = remoteUsers.associateBy { it.userId }

                // Combine RemotePost and RemoteUser into Post objects
                // then add all to "posts"
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

                // Convert remoteComments to list of Comments
                comments = remoteComments.mapNotNull { remoteComment ->
                        // Find the corresponding user for the comment
                        val user = remoteUsers.find { it.userId == remoteComment.userId }
                        // If user is found, create a Comment object
                        user?.let {remoteUser ->
                            Comment(
                                comment = remoteComment.content,
                                date = remoteComment.createdAt,
                                authorName = remoteUser.name,
                                authorImageUrl = remoteUser.imageUrl,
                                authorId = remoteUser.userId,
                                postId = remoteComment.postId
                            )
                        }
                    }.toMutableList()

                // Map remote users to Profiles
                profiles = remoteUsers.map { user ->
                    // Calculate followers count (where `toId` is the user's ID)
                    val followersCount = remoteFollows.count { it.toId == user.userId }

                    // Calculate following count (where `fromId` is the user's ID)
                    val followingCount = remoteFollows.count { it.fromId == user.userId }

                    // Check if the profile belongs to the current user
                    val isOwnProfile = user.userId == auth.currentUser?.uid

                    // Check if the current user is following this user
                    val isFollowing = remoteFollows.any { it.fromId == auth.currentUser?.uid && it.toId == user.userId }

                    // Create Profile object
                    Profile(
                        id = user.userId,
                        name = user.name,
                        bio = user.bio,
                        profileUrl = user.imageUrl,
                        followersCount = followersCount,
                        followingCount = followingCount,
                        isOwnProfile = isOwnProfile,
                        isFollowing = isFollowing
                    )
                }
            } catch(e: Exception) {
                Log.e("Error", "Error in TweetsViewModel")
                postsUiState = postsUiState.copy(
                    isLoading = false,
                    errorMessage = e.message
                )
            }
        }
    }

    @SuppressLint("LogNotTimber")
    private suspend fun createUserIfNotExists() {
        val isUserExist = remoteUsers.any {remoteUser ->
            remoteUser.userId == auth.currentUser?.uid!!
        }
        Log.e("currentUser", auth.currentUser?.uid!!)
        Log.e("isUserExist", isUserExist.toString())
        if(!isUserExist) {
            try {
                auth.currentUser?.uid?.let { userUid ->
                    // Insert new user with default values
                    val userData = mapOf(
                        "bio" to "Hey, what's up? Welcome to my profile!",
                        "imageUrl" to "https://upload.wikimedia.org/wikipedia/commons/thumb/4/46/Bitcoin.svg/1200px-Bitcoin.svg.png",
                        "name" to "User",
                        "userId" to userUid
                    )
                    // Insert to Firebase collection
                    usersCollection
                        .add(userData)
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
                println("Failed to insert user in Firestore: ${e.message}")
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
            HomeUiAction.FetchPostsAction -> fetchPostsFromFirebase()
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

    object FetchPostsAction: HomeUiAction
}

//data class TabItem (
//    val title: String,
//    val unselectedIcon: ImageVector,
//    val selectedIcon: ImageVector
//)
//
//val tabItems = listOf(
//    TabItem(
//        title = "For you",
//        unselectedIcon = Icons.Outlined.AccountCircle,
//        selectedIcon = Icons.Filled.AccountCircle
//    ),
//    TabItem(
//        title = "Following",
//        unselectedIcon = Icons.Outlined.Favorite,
//        selectedIcon = Icons.Filled.Favorite
//    )
//)