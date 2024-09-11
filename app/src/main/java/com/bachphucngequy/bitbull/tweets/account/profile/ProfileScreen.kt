package com.bachphucngequy.bitbull.tweets.account.profile

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bachphucngequy.bitbull.R
import com.bachphucngequy.bitbull.tweets.common.CircleImage
import com.bachphucngequy.bitbull.tweets.common.Dimension.LargeSpacing
import com.bachphucngequy.bitbull.tweets.common.Dimension.MediumSpacing
import com.bachphucngequy.bitbull.tweets.common.Dimension.SmallSpacing
import com.bachphucngequy.bitbull.tweets.common.FollowsButton
import com.bachphucngequy.bitbull.tweets.common.PostListItem
import com.bachphucngequy.bitbull.tweets.data.Post


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    userInfoUiState: UserInfoUiState,
    profilePostsUiState: ProfilePostsUiState,
    profileId: String,
    onUiAction: (ProfileUiAction) -> Unit,
    onButtonClick: () -> Unit,
    onFollowersScreenNavigation: () -> Unit,
    onFollowingScreenNavigation: () -> Unit,
    onPostDetailNavigation: (Post) -> Unit,
    navigateUp: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Profile") },
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
            if (userInfoUiState.isLoading && profilePostsUiState.posts.isEmpty()) {
                LinearProgressIndicator(modifier = modifier.fillMaxWidth())
            } else {
                LazyColumn(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    item(key = "header_section") {
                        ProfileHeaderSection(
                            imageUrl = userInfoUiState.profile?.profileUrl ?: "",
                            name = userInfoUiState.profile?.name ?: "",
                            bio = userInfoUiState.profile?.bio ?: "",
                            followersCount = userInfoUiState.profile?.followersCount ?: 0,
                            followingCount = userInfoUiState.profile?.followingCount ?: 0,
                            isFollowing = userInfoUiState.profile?.isFollowing ?: false,
                            isCurrentUser = userInfoUiState.profile?.isOwnProfile ?: false,
                            onButtonClick = onButtonClick,
                            onFollowersClick = onFollowersScreenNavigation,
                            onFollowingClick = onFollowingScreenNavigation
                        )
                    }
                    items(
                        items = profilePostsUiState.posts,
                        key = { post -> post.id }
                    ) {
                        PostListItem(
                            post = it,
                            onPostClick = onPostDetailNavigation,
                            onProfileClick = {},
                            onLikeClick = {post ->
                                onUiAction(ProfileUiAction.PostLikeAction(post)) },
                            onCommentClick = onPostDetailNavigation
                        )
                    }
                }
            }
        }
    )

    // Call only once when this composable enter composition
    LaunchedEffect(key1 = Unit, block = {
        onUiAction(ProfileUiAction.FetchProfileAction(profileId = profileId))
    })
}

@Composable
fun ProfileHeaderSection(
    modifier: Modifier = Modifier,
    imageUrl: String,
    name: String,
    bio: String,
    followersCount: Int,
    followingCount: Int,
    isCurrentUser: Boolean = false,
    isFollowing: Boolean = false,
    onButtonClick: () -> Unit,
    onFollowersClick: () -> Unit,
    onFollowingClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = MediumSpacing)
            .background(color = Color.White)
            .padding(all = LargeSpacing)
    ) {

        CircleImage(
            modifier = modifier.size(90.dp),
            url = imageUrl,
            onClick = {}
        )

        Spacer(modifier = modifier.height(SmallSpacing))

        Text(
            text = name,
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(text = bio, style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = modifier.height(MediumSpacing))

        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = modifier.weight(1f)
            ) {

                FollowsText(
                    count = followersCount,
                    text = R.string.followers_text,
                    onClick = onFollowersClick
                )

                Spacer(modifier = modifier.width(MediumSpacing))

                FollowsText(
                    count = followingCount,
                    text = R.string.following_text,
                    onClick = onFollowingClick
                )
            }

            FollowsButton(
                text = when {
                    isCurrentUser -> R.string.edit_profile_label
                    isFollowing -> R.string.unfollow_text_label
                    else -> R.string.follow_text_label
                },
                onClick = onButtonClick,
                isOutline = isCurrentUser || isFollowing
            )
        }
    }
}

@Composable
fun FollowsText(
    modifier: Modifier = Modifier,
    count: Int,
    @StringRes text: Int,
    onClick: () -> Unit
) {

    Text(
        text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            ) {
                append(text = "$count ")
            }

            withStyle(
                style = SpanStyle(
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp
                )
            ) {
                append(text = stringResource(id = text))
            }
        },
        modifier = modifier.clickable { onClick() }
    )
}

@Preview(showBackground = true)
@Composable
fun ProfileHeaderPreview() {
    Surface(color = Color.White) {
        ProfileHeaderSection(
            imageUrl = "",
            name = "Mr Dip",
            bio = "Hey there, welcome to Mr Dip Coding page",
            followersCount = 9,
            followingCount = 2,
            onButtonClick = { /*TODO*/ },
            onFollowersClick = { /*TODO*/ }) {
        }
    }
}
