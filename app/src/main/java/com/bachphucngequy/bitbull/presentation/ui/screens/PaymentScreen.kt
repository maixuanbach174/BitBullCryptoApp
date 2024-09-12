package com.bachphucngequy.bitbull.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

data class PaymentMethod(
    val icon: ImageVector,
    val name: String,
    val balance: String? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(onNavigateToUserAccount: () -> Unit) {
    val paymentMethods = listOf(
        PaymentMethod(Icons.Default.CreditCard, "Green Wallet", "0 USD"),
        PaymentMethod(Icons.Default.AccountBalance, "MoMo"),
        PaymentMethod(Icons.Default.Payment, "ZaloPay"),
        PaymentMethod(Icons.Default.Phone, "Viettel Money"),
        PaymentMethod(Icons.Default.ShoppingCart, "Google Pay")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Payment Management") },
                navigationIcon = {
                    IconButton(onClick = onNavigateToUserAccount) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Handle history click */ }) {
                        Icon(Icons.Default.History, contentDescription = "History")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            items(paymentMethods) { method ->
                PaymentMethodItem(method)
            }

            item {
                if (paymentMethods.first().balance != null) {
                    BalanceSection(paymentMethods.first().balance!!)
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    "Adding payment gateway",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                PaymentMethodItem(PaymentMethod(Icons.Default.CreditCard, "International Card"))
            }

            item {
                PaymentMethodItem(PaymentMethod(Icons.Default.Store, "ShopeePay"))
            }
        }
    }
}

@Composable
fun PaymentMethodItem(method: PaymentMethod) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = method.icon,
                contentDescription = method.name,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = method.name,
                modifier = Modifier.weight(1f)
            )
            Icon(
                Icons.Default.KeyboardArrowRight,
                contentDescription = "Details"
            )
        }
    }
}

@Composable
fun BalanceSection(balance: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            "Remaining: $balance",
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { /* Handle top-up */ },
                modifier = Modifier.weight(1f)
            ) {
                Text("Deposit")
            }
            Button(
                onClick = { /* Handle gift card exchange */ },
                modifier = Modifier.weight(1f)
            ) {
                Text("Prize Exchange")
            }
        }
    }
}