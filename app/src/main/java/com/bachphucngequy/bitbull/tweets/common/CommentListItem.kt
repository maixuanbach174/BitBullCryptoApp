package com.bachphucngequy.bitbull.tweets.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bachphucngequy.bitbull.R
import com.bachphucngequy.bitbull.tweets.common.Dimension.LargeSpacing
import com.bachphucngequy.bitbull.tweets.common.Dimension.MediumSpacing
import com.bachphucngequy.bitbull.tweets.data.Comment
import com.bachphucngequy.bitbull.tweets.data.sampleComments

@Composable
fun CommentListItem(
    modifier: Modifier = Modifier,
    comment: Comment,
    onProfileClick: (String) -> Unit,
    onMoreIconClick: (Comment) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(all = LargeSpacing),
        horizontalArrangement = Arrangement.spacedBy(MediumSpacing)
    ) {
        CircleImage(
            modifier = modifier.size(30.dp),
            url = comment.authorImageUrl,
            onClick = { onProfileClick(comment.authorId) }
        )

        Column(
            modifier = modifier
                .weight(1f)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(
                    MediumSpacing
                )
            ) {
                Text(
                    text = comment.authorName,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = modifier.alignByBaseline()
                )

                Text(
                    text = comment.date,
                    style = MaterialTheme.typography.headlineMedium.copy(fontSize = 11.sp),
                    color = Color.DarkGray,
                    modifier = modifier.alignByBaseline().weight(1f)
                )
                Icon(
                    painter = painterResource(id = R.drawable.round_more_horizontal),
                    contentDescription = null,
                    tint = Color.DarkGray,
                    modifier = modifier.clickable { onMoreIconClick(comment) }
                )
            }

            Text(
                text = comment.comment,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CommentListItemPreview() {
    Surface(color = Color.White) {
        CommentListItem(
            comment = sampleComments.first(),
            onProfileClick = {},
            onMoreIconClick = {}
        )
    }
}