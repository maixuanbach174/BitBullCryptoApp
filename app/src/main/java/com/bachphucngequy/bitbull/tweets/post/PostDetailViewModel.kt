package com.bachphucngequy.bitbull.tweets.post

import android.icu.util.Calendar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bachphucngequy.bitbull.tweets.common.util.Event
import com.bachphucngequy.bitbull.tweets.common.util.EventBus
import com.bachphucngequy.bitbull.tweets.data.Comment
import com.bachphucngequy.bitbull.tweets.data.Post
import com.bachphucngequy.bitbull.tweets.data.sampleComments
import com.bachphucngequy.bitbull.tweets.data.samplePosts
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PostDetailViewModel : ViewModel() {
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

    private fun fetchData(postId: Int) {
        viewModelScope.launch {
            postUiState = postUiState.copy(
                isLoading = false,
                post = samplePosts.find { it.id == postId }
            )
            commentsUiState = commentsUiState.copy(
                isLoading = false,
                comments = sampleComments.filter { comment -> comment.postId == postId }
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

            // TODO: Need to replace with update database
            updatedPostsInDB(updatedPost.id, post, count)

            EventBus.send(Event.PostUpdated(updatedPost))
        }
    }

    private fun updatedPostsInDB(id: Int, post: Post, count: Int) {
        samplePosts = samplePosts.map {
            if (it.id == id) {
                it.copy(
                    isLiked = !post.isLiked, // toggle
                    likesCount = post.likesCount.plus(count)
                )
            } else it
        }.toMutableList()
    }

    private fun addNewComment(commentStr: String) {
        viewModelScope.launch {
            val post = postUiState.post ?: return@launch

            commentsUiState = commentsUiState.copy(isAddingNewComment = true)
            delay(500)

            // TODO: Need to replace with update database
            val comment = updateCommentsInDB(commentStr, post)
            //
            val updatedComments = listOf(comment) + commentsUiState.comments

            commentsUiState = commentsUiState.copy(
                comments = updatedComments,
                isAddingNewComment = false
            )

            val updatedPost = post.copy(
                commentsCount = post.commentsCount.plus(1)
            )

            EventBus.send(Event.PostUpdated(updatedPost))
        }
    }

    private fun updateCommentsInDB(commentStr: String, post : Post): Comment {
        val lastId = sampleComments.last().id
        val newComment = Comment(
            id = lastId + 1,
            comment = commentStr,
            date = sdf.format(Calendar.getInstance().time),
            // TODO: Need to replace with current user
            authorName = post.authorName,
            authorId = post.authorId,
            authorImageUrl = post.authorImageUrl,
            //
            postId = post.id
        )
        sampleComments.add(newComment)
        return newComment
    }


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
    data class FetchPostAction(val postId: Int): PostDetailUiAction
    data class LikeOrDislikePostAction(val post: Post): PostDetailUiAction
    data class AddCommentAction(val comment: String) : PostDetailUiAction
}