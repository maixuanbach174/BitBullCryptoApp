package com.bachphucngequy.bitbull.tweets.account.edit

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bachphucngequy.bitbull.R
import com.bachphucngequy.bitbull.tweets.common.CircleImage
import com.bachphucngequy.bitbull.tweets.common.CustomTextField
import com.bachphucngequy.bitbull.tweets.common.Dimension.ButtonHeight
import com.bachphucngequy.bitbull.tweets.common.Dimension.ExtraLargeSpacing
import com.bachphucngequy.bitbull.tweets.common.Dimension.LargeSpacing
import com.bachphucngequy.bitbull.tweets.common.Dimension.SmallElevation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    modifier: Modifier = Modifier,
    editProfileUiState: EditProfileUiState,
    onNameChange: (String) -> Unit,
    bioTextFieldValue: TextFieldValue,
    onBioChange: (TextFieldValue) -> Unit,
    onUploadButtonClick: () -> Unit,
    onUploadSucceed: () -> Unit,
    fetchProfile: () -> Unit,
    navigateUp: () -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Edit profile") },
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
            Box(
                modifier = modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                when {
                    editProfileUiState.profile != null -> {
                        Column(
                            modifier = modifier
                                .fillMaxSize()
                                .background(
                                    color = Color.White
                                )
                                .padding(ExtraLargeSpacing),
                            verticalArrangement = Arrangement.spacedBy(LargeSpacing),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box {
                                CircleImage(
                                    modifier = modifier.size(120.dp),
                                    url = editProfileUiState.profile.profileUrl,
                                    onClick = {}
                                )

                                IconButton(
                                    onClick = { /*TODO*/ },
                                    modifier = modifier
                                        .align(Alignment.BottomEnd)
                                        .shadow(
                                            elevation = SmallElevation,
                                            shape = RoundedCornerShape(percent = 25)
                                        )
                                        .background(
                                            color = Color.White,
                                            shape = RoundedCornerShape(percent = 25)
                                        )
                                        .size(40.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Edit,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                            Spacer(modifier = modifier.height(LargeSpacing))

                            CustomTextField(
                                value = editProfileUiState.profile.name,
                                onValueChange = onNameChange,
                                hint = R.string.username_hint
                            )

                            BioTextField(value = bioTextFieldValue, onValueChange = onBioChange)

                            Button(
                                onClick = {
                                    onUploadButtonClick()
                                },
                                modifier = modifier
                                    .fillMaxWidth()
                                    .height(ButtonHeight),
                                elevation = ButtonDefaults.elevatedButtonElevation(
                                    defaultElevation = 0.dp
                                ),
                                shape = MaterialTheme.shapes.medium
                            ) {
                                Text(text = stringResource(id = R.string.upload_changes_text))
                            }
                        }
                    }

                    editProfileUiState.errorMessage != null -> {
                        Column {
                            Text(
                                text = stringResource(id = R.string.could_not_load_profile),
                                style = MaterialTheme.typography.headlineMedium.copy(textAlign = TextAlign.Center)
                            )

                            Button(
                                onClick = fetchProfile,
                                modifier = modifier.height(ButtonHeight),
                                elevation = ButtonDefaults.elevatedButtonElevation(
                                    defaultElevation = 0.dp
                                ),
                                shape = MaterialTheme.shapes.medium
                            ) {
                                Text(text = stringResource(id = R.string.retry_button_text))
                            }
                        }
                    }

                }

                if (editProfileUiState.isLoading){
                    Text(text = "Loading...")
                }
            }
        }
    )

    LaunchedEffect(key1 = Unit, block = { fetchProfile() })

    LaunchedEffect(
        key1 = editProfileUiState.uploadSucceed,
        key2 = editProfileUiState.errorMessage,
        block = {
            if (editProfileUiState.uploadSucceed){
                onUploadSucceed()
            }

            if (editProfileUiState.profile != null && editProfileUiState.errorMessage != null){
                Toast.makeText(context, editProfileUiState.errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    )
}

@Composable
fun BioTextField(
    modifier: Modifier = Modifier,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit
) {
    TextField(
        modifier = modifier
            .fillMaxWidth()
            .height(90.dp),
        value = value,
        onValueChange = {
            onValueChange(
                TextFieldValue(
                    text = it.text,
                    selection = TextRange(it.text.length)
                )
            )
        },
        textStyle = MaterialTheme.typography.bodyMedium,
        placeholder = {
            Text(
                text = stringResource(id = R.string.user_bio_hint),
                style = MaterialTheme.typography.bodyMedium
            )
        },
        shape = MaterialTheme.shapes.medium
    )
}