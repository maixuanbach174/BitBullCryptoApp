package com.bachphucngequy.bitbull.ui.components.marketdetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bachphucngequy.bitbull.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SymbolAppBar(
    marketName: String,
    companyName: String,
    onBackClick: () -> Unit,
    onFavouriteClick: () -> Unit,
    isCoinFavourite: Boolean
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White,
            titleContentColor = Color.Black
        ),
        title = {
            Column {
                Text(text = marketName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(text = companyName,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Back to previous screen",
                    modifier = Modifier.size(60.dp)
                )
            }
        },
        actions = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = "Follower",

                        )
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = "Follower",

                        )
                }
                IconButton(onClick = onFavouriteClick) {
                    Icon(
                        painter = if (isCoinFavourite) {
                            painterResource(id = R.drawable.like_icon_filled)
                        } else {
                            painterResource(id = R.drawable.like_icon_outlined)
                        },
                        contentDescription = null,
                        tint = if (isCoinFavourite) Color.Red else Color.DarkGray
                    )
                }
            }
        }
    )
}