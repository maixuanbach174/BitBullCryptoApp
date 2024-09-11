package com.bachphucngequy.bitbull.tweets.post

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bachphucngequy.bitbull.R
import com.bachphucngequy.bitbull.tweets.common.Dimension

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewPostScreen(
    modifier: Modifier = Modifier,
    onUiAction: (NewPostUiAction) -> Unit,
    navigateUp: () -> Unit
) {
    var postText by rememberSaveable {
        mutableStateOf("")
    }
    var postImageUrl by rememberSaveable {
        mutableStateOf("")
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "New post") },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close new post"
                        )
                    }
                },
                actions = {
                    PostButton(
                        sendCommentEnabled = postText.isNotBlank(),
                        onPostClick = {
                            keyboardController?.hide()
                            onUiAction(NewPostUiAction.AddPost(postText, postImageUrl))
                            postText = ""
                            navigateUp()
                        }
                    )
                }
            )
        },
        content = {paddingValues ->
            PostInput(
                contentPaddingValues = paddingValues,
                postText = postText,
                postImageUrl = postImageUrl,
                onTextChange = {postText = it},
                onImageUrlChange = {postImageUrl = it}
            )
        }
    )
}

@Composable
fun PostInput(
    contentPaddingValues: PaddingValues,
    postText: String,
    postImageUrl: String,
    onTextChange: (String) -> Unit,
    onImageUrlChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .background(
                color = Color.White
            )
            .padding(contentPaddingValues)
    ) {
        TextField(
            value = postText,
            onValueChange = onTextChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = Dimension.MediumSpacing,
                    vertical = Dimension.SmallSpacing
                )
                .weight(1f),
            textStyle = LocalTextStyle.current.copy(color = LocalContentColor.current),
            placeholder = {
                Text(
                    modifier = Modifier
                        .padding(start = Dimension.SmallSpacing),
                    text = stringResource(id = R.string.post_hint),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                )
            }
        )
        Spacer(
            modifier = Modifier.height(20.dp)
        )
        TextField(
            value = postImageUrl,
            onValueChange = onImageUrlChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                horizontal = Dimension.MediumSpacing,
                vertical = Dimension.SmallSpacing
            ),
            textStyle = LocalTextStyle.current.copy(color = LocalContentColor.current),
            placeholder = {
                Text(
                    modifier = Modifier
                        .padding(start = Dimension.SmallSpacing),
                    text = stringResource(id = R.string.image_url_hint),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                )
            }
        )
    }
}

@Composable
private fun PostButton(
    modifier: Modifier = Modifier,
    sendCommentEnabled: Boolean,
    onPostClick: () -> Unit
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
        onClick = onPostClick,
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
            stringResource(id = R.string.post_button_text),
            modifier = modifier.padding(horizontal = 16.dp)
        )
    }

}

@Preview(showBackground = true)
@Composable
fun NewPostScreenPreview() {
    NewPostScreen(
        onUiAction = {},
        navigateUp = {}
    )
}