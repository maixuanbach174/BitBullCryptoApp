package com.bachphucngequy.bitbull.tweets.account.follows

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.bachphucngequy.bitbull.tweets.common.util.Constants.FOLLOWERS_CODE
import com.bachphucngequy.bitbull.tweets.common.util.Constants.FOLLOWING_CODE

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FollowsScreen(
    modifier: Modifier = Modifier,
    uiState: FollowsUiState,
    fetchFollows: () -> Unit,
    onItemClick: (String) -> Unit,
    navigateUp: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if(uiState.followsType == FOLLOWING_CODE) {
                        Text(text = "Following")
                    } else if(uiState.followsType == FOLLOWERS_CODE) {
                        Text(text = "Followers")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                        )
                    }
                }
            )
        },
        content = {paddingValues ->
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                LazyColumn(
                    modifier = modifier.fillMaxSize()
                ) {
                    items(
                        items = uiState.followsUsers,
                        key = { user -> user.id }
                    ) {followUser ->
                        FollowsListItem(
                            name = followUser.name,
                            bio = followUser.bio,
                            imageUrl = followUser.profileUrl,
                            onItemClick = {onItemClick(followUser.id)}
                        )
                    }
                }
            }
            if (uiState.isLoading && uiState.followsUsers.isEmpty()){
                LinearProgressIndicator(modifier = modifier.fillMaxWidth())
            }
        }
    )
    LaunchedEffect(key1 = Unit, block = { fetchFollows() })
}