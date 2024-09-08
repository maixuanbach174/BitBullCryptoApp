package com.bachphucngequy.bitbull.tweets.account.follows

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bachphucngequy.bitbull.tweets.data.FollowsUser
import com.bachphucngequy.bitbull.tweets.data.sampleUsers
import kotlinx.coroutines.launch

class FollowsViewModel : ViewModel(){

    var uiState by mutableStateOf(FollowsUiState())
        private set

    fun fetchFollows(userId: String, followsType: Int){
        viewModelScope.launch {
            uiState = uiState.copy(
                isLoading = false,
                sampleFollowsUsers = sampleUsers,
                followsType = followsType
            )

        }
    }
}

data class FollowsUiState(
    val isLoading: Boolean = true,
    val sampleFollowsUsers: List<FollowsUser> = listOf(),
    val followsType: Int = 0,
    val errorMessage: String? = null
)