package com.bachphucngequy.bitbull.tweets.common

import androidx.annotation.StringRes
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bachphucngequy.bitbull.R

@Composable
fun FollowsButton(
    modifier: Modifier = Modifier,
    @StringRes text: Int,
    onClick: () -> Unit,
    isOutline: Boolean = false
) {
    Button(
        onClick = onClick,
        colors = if (isOutline){
            ButtonDefaults.outlinedButtonColors()
        }else{
            ButtonDefaults.buttonColors()
        },
        border = if (isOutline){
            ButtonDefaults.outlinedButtonBorder
        }else{
            null
        },
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = 0.dp
        )
    ) {
        Text(
            text = stringResource(id = text),
            style = MaterialTheme.typography.displayMedium.copy(
                fontSize = 10.sp
            )
        )
    }
}

@Preview
@Composable
fun FollowsButtonPreview() {
    FollowsButton(text = R.string.unfollow_text_label, onClick = { })
}