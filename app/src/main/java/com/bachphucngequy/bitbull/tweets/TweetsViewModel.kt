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
import com.bachphucngequy.bitbull.tweets.data.sampleComments
import com.bachphucngequy.bitbull.tweets.data.samplePosts
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class TweetsViewModel: ViewModel() {
    var postsUiState by mutableStateOf(PostsUIState())
        private set

    init {
        syncPostsWithCommentsCount()

        fetchData()

        EventBus.events
            .onEach {
                when (it) {
                    is Event.PostUpdated -> updatePost(it.post)
                }
            }.launchIn(viewModelScope)
    }

    private fun syncPostsWithCommentsCount() {
        for(comment in sampleComments) {
            samplePosts.find{it.id == comment.postId}?.let {post ->
                post.commentsCount += 1
            }
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            postsUiState = postsUiState.copy(
                isLoading = false,
                posts = samplePosts
            )
        }
    }

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
            // TODO: Should replace code below with update posts in database
            updatePostsInDB(post, count)
        }
    }

    private fun updatePostsInDB(post: Post, count: Int) {
        samplePosts = samplePosts.map {
            if (it.id == post.id) {
                it.copy(
                    isLiked = !post.isLiked, // toggle
                    likesCount = post.likesCount.plus(count)
                )
            } else it
        }.toMutableList()
    }

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