package com.bachphucngequy.bitbull.tweets.post

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bachphucngequy.bitbull.R
import com.bachphucngequy.bitbull.tweets.common.CommentListItem
import com.bachphucngequy.bitbull.tweets.common.Dimension.LargeSpacing
import com.bachphucngequy.bitbull.tweets.common.Dimension.MediumSpacing
import com.bachphucngequy.bitbull.tweets.common.Dimension.SmallSpacing
import com.bachphucngequy.bitbull.tweets.common.PostListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailScreen(
    modifier: Modifier = Modifier,
    postUiState: PostUiState,
    commentsUiState: CommentsUiState,
    postId: Int,
    onProfileNavigation: (userId: Int) -> Unit,
    onUiAction: (PostDetailUiAction) -> Unit,
    navigateUp: () -> Unit
) {
    var commentText by rememberSaveable {
        mutableStateOf("")
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Post Detail") },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                        )
                    }
                }
            )
        },
        content = {paddingValues ->
            if (postUiState.isLoading) {
                Box(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Loading...")
                }
            } else if(postUiState.post != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .imePadding()
                ) {
                    LazyColumn(
                        modifier = modifier
                            .fillMaxSize()
                            .background(Color.White)
                            .weight(1f)
                    ) {
                        item(key = "post_item") {
                            PostListItem(
                                post = postUiState.post,
                                onPostClick = {},
                                onProfileClick = onProfileNavigation,
                                onLikeClick = {onUiAction(PostDetailUiAction.LikeOrDislikePostAction(it))},
                                onCommentClick = {/*TODO*/},
                                isDetailScreen = true
                            )
                        }

                        item(key = "comments_header") {
                            CommentsHeaderSection()
                        }

                        items(
                            items = commentsUiState.comments,
                            key = { comment -> comment.id }
                        ) {
                            Divider()
                            CommentListItem(
                                comment = it,
                                onProfileClick = {},
                                onMoreIconClick = {}
                            )
                        }
                    }

                    CommentInput(
                        commentText = commentText,
                        onCommentChange = {
                            commentText = it
                        },
                        onSendClick = {
                            keyboardController?.hide()
                            onUiAction(PostDetailUiAction.AddCommentAction(it))
                            commentText = ""
                        }
                    )
                }

            } else {
                Box(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ){
                    Column {
                        Text(
                            text = stringResource(id = R.string.loading_error_message),
                            style = MaterialTheme.typography.headlineLarge
                        )

                        OutlinedButton(onClick = {onUiAction(PostDetailUiAction.FetchPostAction(postId))}) {
                            Text(text = stringResource(id = R.string.retry_button_text))
                        }
                    }
                }
            }
        }
    )
    // Call only once when this composable enter composition
    LaunchedEffect(key1 = Unit, block = {
        onUiAction(PostDetailUiAction.FetchPostAction(postId))
    })
}

@Composable
private fun CommentsHeaderSection(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(all = LargeSpacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.comments_label),
            style = MaterialTheme.typography.titleLarge,
            modifier = modifier.weight(1f)
        )
    }
}

@Composable
private fun CommentInput(
    modifier: Modifier = Modifier,
    commentText: String,
    onCommentChange: (String) -> Unit,
    onSendClick: (String) -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .animateContentSize()
    ) {
        Divider()

        Row(
            modifier = modifier.padding(
                horizontal = LargeSpacing,
                vertical = MediumSpacing
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(LargeSpacing)
        ) {
            Box(
                modifier = modifier
                    .heightIn(min = 35.dp, max = 70.dp)
                    .background(
                        color = Color.LightGray.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(percent = 25)
                    )
                    .padding(
                        horizontal = MediumSpacing,
                        vertical = SmallSpacing
                    )
                    .weight(1f)
            ) {
                BasicTextField(
                    value = commentText,
                    onValueChange = onCommentChange,
                    modifier = modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterStart),
                    textStyle = LocalTextStyle.current.copy(color = LocalContentColor.current),
                    cursorBrush = SolidColor(LocalContentColor.current)
                )

                if (commentText.isEmpty()){
                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = SmallSpacing),
                        text = stringResource(id = R.string.comment_hint),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                    )
                }
            }

            SendCommentButton(
                sendCommentEnabled = commentText.isNotBlank(),
                onSendClick = {
                    onSendClick(commentText)
                }
            )
        }

    }
}

@Composable
private fun SendCommentButton(
    modifier: Modifier = Modifier,
    sendCommentEnabled: Boolean,
    onSendClick: () -> Unit
){
    val border = if (!sendCommentEnabled) {
        BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
        )
    } else {
        null
    }

    Button(
        modifier = modifier.height(35.dp),
        enabled = sendCommentEnabled,
        onClick = onSendClick,
        colors = ButtonDefaults.buttonColors(
            disabledContainerColor = Color.Transparent,
            disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
        ),
        border = border,
        shape = RoundedCornerShape(percent = 50),
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            disabledElevation = 0.dp
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            stringResource(id = R.string.send_button_text),
            modifier = modifier.padding(horizontal = 16.dp)
        )
    }

}