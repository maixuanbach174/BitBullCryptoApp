package com.bachphucngequy.bitbull.presentation.ui.screens

import android.widget.Toast
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bachphucngequy.bitbull.R
import com.bachphucngequy.bitbull.presentation.viewmodel.AuthState
import com.bachphucngequy.bitbull.presentation.viewmodel.AuthViewModel
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserAccountScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToCryptoWallet: () -> Unit,
    onNavigateToChangeNickname: () -> Unit,
    onNavigateToSignIn: () -> Unit,
    authViewModel: AuthViewModel
) {
    val authState = authViewModel.authState.observeAsState()
    val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    val currentUserId = authViewModel.getCurrentUserId()
    var userName by remember { mutableStateOf("") }
    var userEmail by remember { mutableStateOf("") }

    LaunchedEffect(currentUserId) {
        if (currentUserId != null) {
            firestore.collection("AppUsers").document(currentUserId)
                .get().addOnSuccessListener {
                if(it!=null){
                    userName = it.data?.get("name")?.toString().toString()
                    userEmail = it.data?.get("email")?.toString().toString()
                }
            }
                .addOnFailureListener{
                    println("Error fetching user data")}
        }
    }

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
                colors = TopAppBarDefaults.smallTopAppBarColors(
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
            item { UserInfoSection(onNavigateToCryptoWallet, userName, userEmail) }
            item { MenuItems(onNavigateToChangeNickname) }
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
fun UserInfoSection(onNavigateToCryptoWallet: () -> Unit, userName: String, userEmail: String) {
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
                userName.ifEmpty { "Loading..." },
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                userEmail.ifEmpty { "Loading..." },
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
fun MenuItems(
    onNavigateToChangeNickname: () -> Unit
) {
    val menuItems = listOf(
        MenuItem("Member Rank", Icons.Default.Star),
        MenuItem("Refer a Friend", Icons.Default.Person),
        MenuItem("Deposit", Icons.Default.AccountBalance),
        MenuItem("Change", Icons.Default.Autorenew),
        MenuItem("Payment Management", Icons.Default.Payment),
        MenuItem("Invoice Information", Icons.Default.Receipt),
        MenuItem("Prize", Icons.Default.EmojiEvents),
        MenuItem("Regular Questions", Icons.Default.QuestionAnswer),
        MenuItem("Terms and Policies", Icons.Default.Description),
        MenuItem("Support Center", Icons.Default.Help),
        MenuItem("User Feedback", Icons.Default.Feedback),
        MenuItem("Company Information", Icons.Default.Info),
        MenuItem("Change Password", Icons.Default.Lock),
        MenuItem("Change Nickname", Icons.Default.Edit),
        MenuItem("Language", Icons.Default.Language)
    )

    Column {
        menuItems.forEach { item ->
            ListItem(
                headlineContent = { Text(item.title) },
                leadingContent = {
                    Icon(
                        imageVector = item.icon,
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
                modifier = Modifier.clickable {
                    when (item.title) {
                        "Change Nickname" -> onNavigateToChangeNickname()
                        else -> {
                            // Handle other menu item clicks
                            // You can add more navigation or action handlers here
                        }
                    }
                }
            )
            Divider()
        }
    }
}

data class MenuItem(val title: String, val icon: ImageVector)

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