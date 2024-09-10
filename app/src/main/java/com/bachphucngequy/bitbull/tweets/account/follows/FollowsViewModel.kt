package com.bachphucngequy.bitbull.tweets.account.follows

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bachphucngequy.bitbull.tweets.data.FollowsUser
import com.bachphucngequy.bitbull.tweets.data.remote.remoteFollows
import com.bachphucngequy.bitbull.tweets.data.remote.remoteUsers
import com.bachphucngequy.bitbull.tweets.data.sampleUsers
import kotlinx.coroutines.launch

class FollowsViewModel : ViewModel(){

    var uiState by mutableStateOf(FollowsUiState())
        private set

    fun fetchFollows(currentUserId: String, followsType: Int){
        // Filter remoteFollows based on followsType
        val filteredFollows = if (followsType == 1) {
            // Users that the current user is following (fromId == currentUserId)
            remoteFollows.filter { it.fromId == currentUserId }
        } else {
            // Users that are following the current user (toId == currentUserId)
            remoteFollows.filter { it.toId == currentUserId }
        }
        // Get the list of user IDs based on the filtered follows
        val userIds = filteredFollows.map { follow ->
            if (followsType == 1) follow.toId else follow.fromId
        }
        // Map to FollowsUser objects using remoteUsers
        val followsUsers = userIds.mapNotNull { userId ->
            remoteUsers.find { it.userId == userId }?.let { user ->
                FollowsUser(
                    id = user.userId,
                    name = user.name,
                    bio = user.bio,
                    profileUrl = user.imageUrl,
                    isFollowing = followsType == 1  // For type 1, current user is following them
                )
            }
        }
        viewModelScope.launch {
            uiState = uiState.copy(
                isLoading = false,
                followsUsers = followsUsers,
                followsType = followsType
            )
        }
    }
}

data class FollowsUiState(
    val isLoading: Boolean = true,
    val followsUsers: List<FollowsUser> = listOf(),
    val followsType: Int = 0,
    val errorMessage: String? = null
)