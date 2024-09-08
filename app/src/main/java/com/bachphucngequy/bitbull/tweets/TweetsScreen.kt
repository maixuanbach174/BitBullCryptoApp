package com.bachphucngequy.bitbull.tweets

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bachphucngequy.bitbull.tweets.common.PostListItem
import com.bachphucngequy.bitbull.tweets.data.Post

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TweetsScreen(
    postsUIState: PostsUIState,
    onUiAction: (HomeUiAction) -> Unit,
    onProfileNavigation: (userId: String) -> Unit,
    onPostDetailNavigation: (Post) -> Unit,
    onNewPostNavigation: () -> Unit,
    navigateUp: () -> Unit
){
    var selectedTabIndex by remember {
        mutableIntStateOf(0)
    }
    val pagerState = rememberPagerState {
        tabItems.size
    }
    LaunchedEffect(selectedTabIndex) {
        pagerState.animateScrollToPage(selectedTabIndex)
    }
    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
        if(!pagerState.isScrollInProgress) {
            selectedTabIndex = pagerState.currentPage
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Feeds") },
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                TabRow(selectedTabIndex = selectedTabIndex) {
                    tabItems.forEachIndexed { index, item ->
                        Tab(
                            selected = index == selectedTabIndex,
                            onClick = {
                                selectedTabIndex = index
                            },
                            text = {
                                Text(text = item.title)
                            },
                            icon = {
                                Icon(
                                    imageVector = if(index == selectedTabIndex) {
                                        item.selectedIcon
                                    } else item.unselectedIcon,
                                    contentDescription = item.title
                                )
                            }
                        )
                    }
                }

                HorizontalPager(
                    state = pagerState
                ) {index ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        when(index) {
                            //TODO: Need to change it later to display random posts
                            0 -> LazyColumn {
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
                            //TODO: Need to change it later to display posts
                            // from following users only, else display nothing message
                            1 -> LazyColumn {
                                items(postsUIState.posts) {post ->
                                    PostListItem(
                                        post = post,
                                        onPostClick = {onPostDetailNavigation(it)},
                                        onProfileClick = {onProfileNavigation(it)},
                                        onLikeClick = { onUiAction(HomeUiAction.PostLikeAction(it))},
                                        onCommentClick = {onPostDetailNavigation(it)}
                                    )
                                    Divider(
                                        modifier = Modifier.fillMaxWidth(),
                                        thickness = 1.dp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}