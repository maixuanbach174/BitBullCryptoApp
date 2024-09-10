package com.bachphucngequy.bitbull.presentation.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bachphucngequy.bitbull.R
import com.bachphucngequy.bitbull.presentation.viewmodel.AuthState
import com.bachphucngequy.bitbull.presentation.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserAccountScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToCryptoWallet: () -> Unit,
    onNavigateToSignIn: () -> Unit,
    authViewModel: AuthViewModel
) {
    val authState = authViewModel.authState.observeAsState()

    LaunchedEffect(authState.value) {
        when(authState.value) {
            is AuthState.Unauthenticated -> onNavigateToSignIn.invoke()
            else -> Unit
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Account") },
                navigationIcon = {
                    IconButton(onClick = onNavigateToHome) {
                        Icon(
                            painter = painterResource(R.drawable.ic_back),
                            contentDescription = "Return",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            item { GreenFundBanner() }
            item { UserInfoSection(onNavigateToCryptoWallet) }
            item { MenuItems() }
            item { LogoutButton(authViewModel) }
        }
    }
}

@Composable
fun GreenFundBanner() {
    Image(
        painter = painterResource(id = R.drawable.ic_france), // Replace with your actual image resource
        contentDescription = "Green Fund Banner",
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        contentScale = ContentScale.Crop
    )
}

// UserInfoSection, MenuItems, and LogoutButton remain the same as in the previous version

@Composable
fun UserInfoSection(onNavigateToCryptoWallet: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable(onClick = onNavigateToCryptoWallet)  // Add clickable modifier
            .padding(vertical = 8.dp),  // Add some padding for better touch feedback
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.mylogo),
            contentDescription = "User Avatar",
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp)
        ) {
            Text(
                "John Doe",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                "+1234567890",
                fontSize = 14.sp,
                color = LocalContentColor.current.copy(alpha = 0.7f)
            )
        }
        Icon(
            Icons.Default.KeyboardArrowRight,
            contentDescription = "Arrow Right"
        )
    }
}

@Composable
fun MenuItems() {
    val menuItems = listOf(
        "Member Rank" to Icons.Default.Star,
        "Refer a Friend" to Icons.Default.Person,
        "Deposit" to Icons.Default.AccountBalance,
        "Change" to Icons.Default.Autorenew,
        "Payment Management" to Icons.Default.Payment,
        "Invoice Information" to Icons.Default.Receipt,
        "Prize" to Icons.Default.EmojiEvents,
        "Regular Questions" to Icons.Default.QuestionAnswer,
        "Terms and Policies" to Icons.Default.Description,
        "Support Center" to Icons.Default.Help,
        "Driver Feedback" to Icons.Default.Feedback,
        "Company Information" to Icons.Default.Info,
        "Change Password" to Icons.Default.Lock,
        "Language" to Icons.Default.Language
    )

    Column {
        menuItems.forEach { (title, icon) ->
            ListItem(
                headlineContent = { Text(title) },
                leadingContent = {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                trailingContent = {
                    Icon(
                        Icons.Default.KeyboardArrowRight,
                        contentDescription = "Arrow Right"
                    )
                },
                modifier = Modifier.clickable { /* Handle click */ }
            )
            Divider()
        }
    }
}

@Composable
fun LogoutButton(authViewModel: AuthViewModel) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Button(
            onClick = { authViewModel.signout() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                Icons.Default.ExitToApp,
                contentDescription = "Logout",
                modifier = Modifier.padding(end = 8.dp)
            )
            Text("Sign Out")
        }
    }
}