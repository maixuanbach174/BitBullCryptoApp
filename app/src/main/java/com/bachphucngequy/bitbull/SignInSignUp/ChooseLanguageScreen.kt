package com.bachphucngequy.bitbull.SignInSignUp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bachphucngequy.bitbull.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseLanguageScreen(
    onNavigateToUserAccount: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    var selectedLanguage by remember { mutableStateOf<Language?>(null) }

    val languages = remember {
        listOf(
            Language("English", R.drawable.ic_uk),
            Language("Italian", R.drawable.ic_italy),
            Language("Chinese", R.drawable.ic_china),
            Language("French", R.drawable.ic_france),
            Language("German", R.drawable.ic_germany),
            Language("Spanish", R.drawable.ic_spain),
            Language("Russian", R.drawable.ic_russia),
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Choose Language") },
                navigationIcon = {
                    IconButton(onClick = { onNavigateToUserAccount() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Icon(
                imageVector = Icons.Default.Language,
                contentDescription = "Language Icon",
                modifier = Modifier
                    .size(80.dp)
                    .padding(bottom = 16.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Select Your Language",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Choose the one you're most comfortable",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                placeholder = { Text("Search languages") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Search Icon")
                },
                shape = RoundedCornerShape(8.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(languages.filter {
                    it.name.contains(searchText, ignoreCase = true)
                }) { language ->
                    LanguageItem(
                        language = language,
                        isSelected = language == selectedLanguage,
                        onSelect = { selectedLanguage = it }
                    )
                }
            }

            Button(
                onClick = onNavigateToUserAccount,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = selectedLanguage != null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Continue", fontSize = 18.sp)
            }
        }
    }
}

@Composable
fun LanguageItem(
    language: Language,
    isSelected: Boolean,
    onSelect: (Language) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect(language) }
            .padding(vertical = 12.dp, horizontal = 16.dp)
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = language.flagResourceId),
            contentDescription = "${language.name} flag",
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(4.dp))
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = language.name,
            fontSize = 18.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
            else MaterialTheme.colorScheme.onSurface
        )
    }
}

data class Language(val name: String, val flagResourceId: Int)