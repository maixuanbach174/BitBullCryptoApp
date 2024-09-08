package com.bachphucngequy.bitbull.presentation.ui.screens

import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.bachphucngequy.bitbull.firebase.WithdrawBackend
import com.bachphucngequy.bitbull.firebase.user

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
    val fee = 1.0
    val availableAmount = remember { mutableStateOf(0.0) }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        WithdrawBackend.fetchAvailableAmount(fee) { amount ->
            availableAmount.value = amount ?: 0.0
            val maxAmount = maxOf(0.0, availableAmount.value - fee)
            if (withdrawAmount.value > maxAmount) {
                withdrawAmount.value = maxAmount
            }
        }
    }

    Scaffold(
        topBar = {
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

            // Binance ID Input
            OutlinedTextField(
                value = binanceId.value,
                onValueChange = { binanceId.value = it },
                label = { Text("Binance ID") },
                placeholder = { Text("Enter Binance ID") },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = if (withdrawAmount.value == 0.0) "" else withdrawAmount.value.toString(),
                    onValueChange = { amount ->
                        val amountDouble = amount.toDoubleOrNull() ?: 0.0
                        val maxAmount = maxOf(0.0, availableAmount.value - fee)
                        withdrawAmount.value = if (amountDouble <= maxAmount) amountDouble else maxAmount
                        if (amountDouble < 0) { withdrawAmount.value = 0.0 }
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
            }

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
                    if (withdrawAmount.value > 0 && binanceId.value.isNotEmpty() && binanceId.value != user.usid) {
                        val remainingAmount = availableAmount.value - withdrawAmount.value - fee
                        if (remainingAmount >= 0) {
                            WithdrawBackend.performWithdrawal(
                                binanceId.value,
                                withdrawAmount.value,
                                fee,
                                note.value,
                                onSuccess = { transactionSuccess ->
                                    if (transactionSuccess) {
                                        WithdrawBackend.fetchAvailableAmount(fee) { amount ->
                                            availableAmount.value = amount ?: 0.0
                                            val maxAmount = maxOf(0.0, availableAmount.value - fee)
                                            if (withdrawAmount.value > maxAmount) {
                                                withdrawAmount.value = maxAmount
                                            }
                                        }
                                        Toast.makeText(context, "Transaction Successful!", Toast.LENGTH_SHORT).show()
                                        Log.d("Withdraw", "Transaction Successful!")
                                    } else {
                                        Toast.makeText(context, "Transaction Failed", Toast.LENGTH_SHORT).show()
                                        Log.d("Withdraw", "Transaction Failed")
                                    }
                                },
                                onError = { errorMessage ->
                                    Toast.makeText(context, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
                                    Log.e("Withdraw", "Error: $errorMessage")
                                }
                            )
                        } else {
                            Toast.makeText(context, "Insufficient funds", Toast.LENGTH_SHORT).show()
                            Log.d("Withdraw", "Insufficient funds")
                        }
                    } else {
                        if(binanceId.value==user.usid) {
                            Toast.makeText(context, "Please enter other Binance ID not yours!", Toast.LENGTH_SHORT).show()
                            Log.d("Withdraw", "Please enter other Binance ID not yours!")
                        }
                        else {
                            Toast.makeText(context, "Please enter Binance ID and a valid withdrawal amount", Toast.LENGTH_SHORT).show()
                            Log.d("Withdraw", "Please enter Binance ID and a valid withdrawal amount")
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
                text = "${withdrawAmount.value + fee} USDT",
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "Fees $fee USDT",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}