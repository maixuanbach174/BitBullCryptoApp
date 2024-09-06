package com.bachphucngequy.bitbull.SignInSignUp

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.bachphucngequy.bitbull.R

@Composable
fun ChooseLanguageScreen(
    languages: List<Language>,
    selectedLanguage: Language,
    onLanguageSelect: (Language) -> Unit,
    onContinueClick: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Back Icon (if needed)
        Image(
            painter = painterResource(id = R.drawable.ic_back), // Replace with actual back icon resource
            contentDescription = "Back Icon",
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Top Icon or Image
        Image(
            painter = painterResource(id = R.drawable.ic_star), // Replace with actual icon resource
            contentDescription = "Language Icon",
            modifier = Modifier
                .height(60.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Title Text
        Text(
            text = "Choose the language",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Subtitle Text
        Text(
            text = "Don’t worry! It happens. Please enter the email associated with your account.",
            fontSize = 16.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Search Bar
        BasicTextField(
            value = searchText,
            onValueChange = { searchText = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(48.dp),
            textStyle = androidx.compose.ui.text.TextStyle(
                color = Color.Black,
                fontSize = 16.sp
            ),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_search), // Replace with actual search icon resource
                        contentDescription = "Search Icon",
                        modifier = Modifier
                            .size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(Modifier.weight(1f)) {
                        innerTextField()
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Language List
        languages.filter {
            it.name.contains(searchText, ignoreCase = true)
        }.forEach { language ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable { onLanguageSelect(language) }
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = language.flagResId),
                    contentDescription = language.name,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = language.name,
                    fontSize = 18.sp,
                    color = if (selectedLanguage == language) Color.Black else Color.Gray,
                    fontWeight = if (selectedLanguage == language) FontWeight.Bold else FontWeight.Normal
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Continue Button
        Button(
            onClick = onContinueClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(text = "Continue", color = Color.White)
        }
    }
}

data class Language(val name: String, val flagResId: Int)

@Preview(showBackground = true)
@Composable
fun ChooseLanguageScreenPreview() {
    val languages = listOf(
        Language("English", R.drawable.ic_uk),
        Language("Italian", R.drawable.ic_italy),
        Language("Chinese", R.drawable.ic_china),
        Language("French", R.drawable.ic_france),
        Language("German", R.drawable.ic_germany),
        Language("Spanish", R.drawable.ic_spain),
        Language("Russian", R.drawable.ic_russia)
    )

    ChooseLanguageScreen(
        languages = languages,
        selectedLanguage = languages[0],
        onLanguageSelect = {},
        onContinueClick = {}
    )
}
