package com.bachphucngequy.bitbull.tweets.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.bachphucngequy.bitbull.R
import com.bachphucngequy.bitbull.tweets.common.Dimension.ExtraLargeSpacing
import com.bachphucngequy.bitbull.tweets.common.Dimension.LargeSpacing
import com.bachphucngequy.bitbull.tweets.common.Dimension.MediumSpacing
import com.bachphucngequy.bitbull.tweets.data.Post

@Composable
fun PostListItem(
    modifier: Modifier = Modifier,
    post: Post,
    onPostClick: (Post) -> Unit,
    onProfileClick: (userId: String) -> Unit,
    onLikeClick: (Post) -> Unit,
    onCommentClick: (Post) -> Unit,
    isDetailScreen: Boolean = false
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .padding(ExtraLargeSpacing)
    ) {
        PostHeader(
            name = post.authorName,
            profileUrl = post.authorImageUrl,
            date = post.createdAt,
            onProfileClick = {
                onProfileClick(
                    post.authorId
                )
            }
        )

        AsyncImage(
            model = post.imageUrl,
            contentDescription = null,
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(ratio = 1.0f)
                .clickable { onPostClick(post) },
            contentScale = ContentScale.Crop
        )

        PostLikesRow(
            likesCount = post.likesCount,
            commentCount = post.commentsCount,
            onLikeClick = { onLikeClick(post) },
            isPostLiked = post.isLiked,
            onCommentClick = { onCommentClick(post) }
        )

        Text(
            text = post.text,
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier.padding(horizontal = LargeSpacing),
            maxLines = if (isDetailScreen) 10 else 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}


@Composable
fun PostHeader(
    modifier: Modifier = Modifier,
    name: String,
    profileUrl: String?,
    date: String,
    onProfileClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = LargeSpacing,
                vertical = MediumSpacing
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MediumSpacing)
    ) {
        CircleImage(
            modifier = modifier.size(30.dp),
            url = profileUrl,
            onClick = onProfileClick
        )

        Text(
            text = name,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = date,
            style = MaterialTheme.typography.headlineMedium.copy(
                textAlign = TextAlign.Start,
                fontSize = 12.sp
            ),
            modifier = modifier.weight(1f)
        )

        Icon(
            painter = painterResource(id = R.drawable.round_more_horizontal),
            contentDescription = null
        )
    }
}


@Composable
fun PostLikesRow(
    modifier: Modifier = Modifier,
    likesCount: Int,
    commentCount: Int,
    onLikeClick: () -> Unit,
    isPostLiked: Boolean,
    onCommentClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                vertical = 0.dp,
                horizontal = MediumSpacing
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onLikeClick
        ) {
            Icon(
                painter = if (isPostLiked) {
                    painterResource(id = R.drawable.like_icon_filled)
                } else {
                    painterResource(id = R.drawable.like_icon_outlined)
                },
                contentDescription = null,
                tint = if (isPostLiked) Red else DarkGray
            )
        }

        Text(
            text = "$likesCount",
            style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp)
        )

        Spacer(modifier = modifier.width(MediumSpacing))

        IconButton(
            onClick = onCommentClick
        ) {
            Icon(
                painter = painterResource(id = R.drawable.chat_icon_outlined),
                contentDescription = null
            )
        }

        Text(
            text = "$commentCount",
            style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp)
        )
    }
}

@Preview
@Composable
private fun PostHeaderPreview() {
    Surface(color = Color.White) {
        PostHeader(
            name = "Mr Dip",
            profileUrl = "",
            date = "20 min",
            onProfileClick = {}
        )
    }
}


@Preview
@Composable
private fun PostLikesRowPreview() {

    Surface(color = Color.White) {
        PostLikesRow(
            likesCount = 12,
            commentCount = 2,
            onLikeClick = {},
            isPostLiked = true,
            onCommentClick = {}
        )
    }

}