package com.bachphucngequy.bitbull.tweets.account.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bachphucngequy.bitbull.tweets.common.util.Event
import com.bachphucngequy.bitbull.tweets.common.util.EventBus
import com.bachphucngequy.bitbull.tweets.data.FollowRelationship
import com.bachphucngequy.bitbull.tweets.data.Post
import com.bachphucngequy.bitbull.tweets.data.Profile
import com.bachphucngequy.bitbull.tweets.data.sampleFollowRelationship
import com.bachphucngequy.bitbull.tweets.data.samplePosts
import com.bachphucngequy.bitbull.tweets.data.sampleProfiles
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    var userInfoUiState by mutableStateOf(UserInfoUiState())
        private set

    var profilePostsUiState by mutableStateOf(ProfilePostsUiState())
        private set

    init {
        syncProfilesWithFollows()
    }

    private fun fetchProfile(userId: String) {
        viewModelScope.launch {
            userInfoUiState = userInfoUiState.copy(
                isLoading = false,
                profile = sampleProfiles.find{it.id == userId}
            )
            profilePostsUiState = profilePostsUiState.copy(
                isLoading = false,
                posts = samplePosts.filter { post -> post.authorId == userId }
            )
        }
    }

    private fun syncProfilesWithFollows() {
        for(follows in sampleFollowRelationship) {
            sampleProfiles.find { it.id == follows.fromUserId }?.let {profile ->
                profile.followingCount += 1
            }
            sampleProfiles.find { it.id == follows.toUserId }?.let {profile ->
                profile.followersCount += 1
            }
        }
    }

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
            updateFollowRelationships(
                userInfoUiState.profile?.id!!,
                profile.id,
                !profile.isFollowing // toggle
            )
        }
    }

    private fun updateFollowRelationships(fromUserId: String, toUserId: String, isFollowing: Boolean) {
        if(isFollowing) {
            sampleFollowRelationship.add(
                FollowRelationship(
                    fromUserId = fromUserId,
                    toUserId = toUserId
                )
            )
        }
        else {
            sampleFollowRelationship.remove(
                FollowRelationship(
                    fromUserId = fromUserId,
                    toUserId = toUserId
                )
            )
        }
    }

    private fun likeOrDislikePost(post: Post) {
        viewModelScope.launch {
            val count = if (post.isLiked) -1 else +1
            val updatedPost = post.copy(
                isLiked = !post.isLiked,
                likesCount = post.likesCount.plus(count)
            )

            profilePostsUiState = profilePostsUiState.copy(
                posts = profilePostsUiState.posts.map {
                    if (it.id == updatedPost.id) updatedPost else it
                }
            )

            // TODO: Need to replace with update database
            updatedPostsInDB(post, count)

            EventBus.send(Event.PostUpdated(updatedPost))
        }
    }

    private fun updatedPostsInDB(post: Post, count: Int) {
        samplePosts = samplePosts.map {
            if (it.id == post.id) {
                it.copy(
                    isLiked = !post.isLiked, // toggle
                    likesCount = post.likesCount.plus(count)
                )
            } else it
        }.toMutableList()
    }

    fun onUiAction(uiAction: ProfileUiAction) {
        when (uiAction) {
            is ProfileUiAction.FetchProfileAction -> fetchProfile(uiAction.profileId)
            is ProfileUiAction.FollowUserAction -> followUser(uiAction.profile)
            is ProfileUiAction.PostLikeAction -> likeOrDislikePost(uiAction.post)
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