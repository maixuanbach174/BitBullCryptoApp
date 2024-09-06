package com.bachphucngequy.bitbull.presentation.ui.screens
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.bachphucngequy.bitbull.firebase.BlankBox
import com.bachphucngequy.bitbull.firebase.user
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Withdraw(
    onNavigateToWithdraw: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToHelp: () -> Unit,
) {
    val binanceId = remember { mutableStateOf("") }
    val withdrawAmount = remember { mutableStateOf(0.0) }
    val note = remember { mutableStateOf("") }
    val fee = 1.0 // Example fee value
    val availableAmount = remember { mutableStateOf(0.0) }

    // Fetch available amount
    LaunchedEffect(Unit) {
        fetchAvailableAmount(fee) { amount ->
            availableAmount.value = amount ?: 0.0
            val maxAmount = maxOf(0.0, availableAmount.value - fee)
            if (withdrawAmount.value > maxAmount) {
                withdrawAmount.value = maxAmount
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        BlankBox()
        Scaffold(
            topBar = {

                BlankBox()
                TopAppBar(
                    title = { Text("Send USDT") },
                    navigationIcon = {
                        IconButton(onClick = onNavigateToWithdraw) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        IconButton(onClick = onNavigateToHistory) {
                            Icon(Icons.Default.History, contentDescription = "History")
                        }
                        IconButton(onClick = onNavigateToHelp) {
                            Icon(Icons.Default.HelpOutline, contentDescription = "Help")
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = "Binance ID",
                    onValueChange = { },
                    label = { Text("Send Mode") },
                    enabled = false,
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = binanceId.value,
                    onValueChange = { binanceId.value = it },
                    label = { Text("Binance ID") },
                    placeholder = { Text("Enter Binance ID") },
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = if (withdrawAmount.value == 0.0) "" else withdrawAmount.value.toString(),
                    onValueChange = { amount ->
                        val sanitizedAmount = amount.replace(",", ".")
                        val amountDouble = sanitizedAmount.toDoubleOrNull()

                        if (amountDouble != null) {
                            val maxAmount = maxOf(0.0, availableAmount.value - fee)
                            withdrawAmount.value = if (amountDouble <= maxAmount) amountDouble else maxAmount
                        } else {
                            withdrawAmount.value = 0.0
                        }
                    },
                    label = { Text("Withdrawal Amount") },
                    placeholder = { Text("Minimum 0.00000001") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    trailingIcon = {
                        TextButton(onClick = {
                            withdrawAmount.value = maxOf(0.0, availableAmount.value - fee)
                        }) {
                            Text("Max", color = Color.Black)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Available: ${availableAmount.value} USDT",
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = note.value,
                    onValueChange = { note.value = it },
                    label = { Text("Note (Optional)") },
                    placeholder = { Text("Add a note for the recipient") },
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (withdrawAmount.value > 0) {
                            val remainingAmount = availableAmount.value - withdrawAmount.value - fee
                            if (remainingAmount >= 0) {
                                availableAmount.value = remainingAmount
                                // Perform the actual withdrawal logic (e.g., update Firebase)
                                //Toast.makeText(LocalContext.current, "Successful withdrawal", Toast.LENGTH_LONG).show()
                            } else {
                                //Toast.makeText(LocalContext.current, "Insufficient funds", Toast.LENGTH_LONG).show()
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDACB85))
                ) {
                    Text("Send")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Total amount",
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "${withdrawAmount.value} USDT",
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "Fees $fee USDT",
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }


}

private fun fetchAvailableAmount(fee: Double, onResult: (Double?) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    val currency = "USDT"

    db.collection("hold")
        .whereEqualTo("userID", user.usid)
        .whereEqualTo("currency", currency)
        .get()
        .addOnSuccessListener { documents ->
            for (document in documents) {
                val amount = document.getDouble("amount")
                if (amount != null) {
                    val finalAmount = maxOf(0.0, amount - fee)
                    onResult(finalAmount)
                } else {
                    onResult(0.0)
                }
                return@addOnSuccessListener
            }
            onResult(null) // In case no document matches
        }
        .addOnFailureListener {
            onResult(null) // Handle failure
        }
}
