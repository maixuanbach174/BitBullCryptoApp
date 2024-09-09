package com.bachphucngequy.bitbull.tweets

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bachphucngequy.bitbull.tweets.common.CircleImage
import com.bachphucngequy.bitbull.tweets.common.PostListItem
import com.bachphucngequy.bitbull.tweets.data.Post

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TweetsScreen(
    postsUIState: PostsUIState,
    onUiAction: (HomeUiAction) -> Unit,
    onProfileNavigation: (userId: String) -> Unit,
    onPostDetailNavigation: (Post) -> Unit,
    onNewPostNavigation: () -> Unit,
    navigateUp: () -> Unit
){
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Feed") },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back to Home")
                    }
                }
            )
        },
        floatingActionButton = {
           FloatingActionButton(
               onClick = onNewPostNavigation,
               containerColor = Color.Blue,
               contentColor = Color.White,
               content = {
                   Icon(
                       imageVector = Icons.Default.Edit,
                       contentDescription = "Add new post"
                   )
               }
           )
        },
        content = {paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(postsUIState.posts) {post ->
                    PostListItem(
                        post = post,
                        onPostClick = {onPostDetailNavigation(it)},
                        onProfileClick = {onProfileNavigation(it)},
                        onLikeClick = {onUiAction(HomeUiAction.PostLikeAction(it))},
                        onCommentClick = {onPostDetailNavigation(it)}
                    )
                    Divider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 1.dp
                    )
                }
            }
        }
    )

    LaunchedEffect(key1 = Unit) {
        onUiAction(HomeUiAction.CreateNewUser)
    }
}