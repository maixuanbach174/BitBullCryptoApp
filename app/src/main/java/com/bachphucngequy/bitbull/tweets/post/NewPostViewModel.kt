package com.bachphucngequy.bitbull.tweets.post

import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bachphucngequy.bitbull.tweets.data.Post
import com.bachphucngequy.bitbull.tweets.data.samplePosts
import com.bachphucngequy.bitbull.tweets.data.sampleUsers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class NewPostViewModel : ViewModel() {
    private val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH)

    private fun createNewPost(postText: String) {
        viewModelScope.launch {
            addNewPostToDB(postText)
        }
    }

    private fun addNewPostToDB(postText: String) {
        val newPost = Post(
            id = samplePosts.last().id + 1,
            text = postText,
            imageUrl = "",
            createdAt = sdf.format(Calendar.getInstance().time),
            likesCount = 0,
            commentsCount = 0,
            authorId = sampleUsers[0].id,
            authorName = sampleUsers[0].name,
            authorImageUrl = sampleUsers[0].profileUrl,
            isLiked = false,
            isOwnPost = true
        )
        samplePosts.add(index = 0, newPost)
    }
    fun onUiAction(action: NewPostUiAction){
        when(action){
            is NewPostUiAction.AddPost -> createNewPost(action.postText)
        }
    }
}

sealed interface NewPostUiAction{
    data class AddPost(val postText: String) : NewPostUiAction
}