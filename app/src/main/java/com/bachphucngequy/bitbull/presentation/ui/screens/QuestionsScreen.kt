package com.bachphucngequy.bitbull.presentation.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionsScreen(onNavigateToUserAccount: () -> Unit) {
    val topics = remember {
        listOf(
            Topic(
                title = "Account Management",
                questions = listOf(
                    Question(
                        "How do I open a trading account?",
                        "To open a trading account, go to our website or app, click on 'Sign Up', fill in your personal details, verify your identity, and fund your account. Our customer support team is available to assist you through the process."
                    ),
                    Question(
                        "What documents are required for account verification?",
                        "For account verification, you typically need to provide a government-issued ID (passport or driver's license), proof of address (utility bill or bank statement not older than 3 months), and in some cases, proof of funds."
                    )
                )
            ),
            Topic(
                title = "Trading Basics",
                questions = listOf(
                    Question(
                        "What is the minimum deposit to start trading?",
                        "The minimum deposit to start trading on our platform is $100. However, we recommend starting with an amount you're comfortable with and can afford to risk."
                    ),
                    Question(
                        "How do I place my first trade?",
                        "To place your first trade, log into your account, select the asset you want to trade, choose 'Buy' or 'Sell', enter the amount, set any stop loss or take profit levels, and click 'Place Order'. We recommend starting with a demo account to practice."
                    )
                )
            ),
            Topic(
                title = "Platform Features",
                questions = listOf(
                    Question(
                        "What types of orders can I place?",
                        "Our platform supports various order types including Market Orders, Limit Orders, Stop Orders, and Take Profit Orders. Each type serves different trading strategies and risk management approaches."
                    ),
                    Question(
                        "Do you offer mobile trading?",
                        "Yes, we offer mobile trading apps for both iOS and Android devices. You can download our app from the App Store or Google Play Store to trade on-the-go."
                    )
                )
            ),
            Topic(
                title = "Security and Support",
                questions = listOf(
                    Question(
                        "How is my personal information protected?",
                        "We use industry-standard encryption and security measures to protect your personal and financial information. This includes SSL encryption, two-factor authentication, and regular security audits."
                    ),
                    Question(
                        "How can I contact customer support?",
                        "Our customer support team is available 24/7. You can reach us via live chat on our website or app, email at support@yourtrading.com, or phone at +1 (800) 123-4567."
                    )
                )
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Frequently Asked Questions") },
                navigationIcon = {
                    IconButton(onClick = onNavigateToUserAccount) {
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            items(topics) { topic ->
                ExpandableTopicItem(topic)
            }
        }
    }
}

@Composable
fun ExpandableTopicItem(topic: Topic) {
    var expanded by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = topic.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    modifier = Modifier.rotate(rotationState)
                )
            }
            AnimatedVisibility(visible = expanded) {
                Column {
                    topic.questions.forEach { question ->
                        ExpandableQuestionItem(question)
                    }
                }
            }
        }
    }
}

@Composable
fun ExpandableQuestionItem(question: Question) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        Text(
            text = question.question,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .clickable { expanded = !expanded }
                .padding(vertical = 4.dp)
        )
        AnimatedVisibility(visible = expanded) {
            Text(
                text = question.answer,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp, bottom = 8.dp)
            )
        }
    }
}

data class Topic(val title: String, val questions: List<Question>)
data class Question(val question: String, val answer: String)