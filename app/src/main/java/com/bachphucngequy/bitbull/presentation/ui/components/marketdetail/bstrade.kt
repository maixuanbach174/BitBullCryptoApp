package com.bachphucngequy.bitbull.presentation.ui.components.marketdetail

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.bachphucngequy.bitbull.firebase.FirebaseRepository
import com.bachphucngequy.bitbull.firebase.OrderManager
import com.bachphucngequy.bitbull.firebase.user
import com.bachphucngequy.bitbull.presentation.ui.theme.Green100
import com.bachphucngequy.bitbull.presentation.ui.theme.Red100

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderBottomSheet(
    btccurrency: String,
    usdtcurrency: String,
    price: Double,
    isBuySelected: Boolean,
    onDismiss: () -> Unit
) {
    var amount by remember { mutableStateOf("") }
    var isBuy by remember { mutableStateOf(isBuySelected) }
    var availableBalance by remember { mutableStateOf(0.0) }
    var btcCurrency = btccurrency
    var usdtCurrency = usdtcurrency
    var price= price

    val userId = user.usid

    LaunchedEffect(userId, isBuy) {
        if (userId != null) {
            val currency = if (isBuy) usdtCurrency else btcCurrency
            FirebaseRepository.fetchAvailableBalance(
                userId = userId,
                currency = currency,
                onSuccess = { balance ->
                    availableBalance = balance
                },
                onFailure = { exception ->
                    Log.e("FirebaseRepository", "Error fetching balance", exception)
                }
            )
        }
    }

    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
        },
        sheetState = bottomSheetState
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    FilledTonalButton(
                        onClick = {
                            isBuy = true
                            amount = ""
                            val currency = usdtCurrency
                            FirebaseRepository.fetchAvailableBalance(
                                userId = userId,
                                currency = currency,
                                onSuccess = { balance ->
                                    availableBalance = balance
                                },
                                onFailure = { exception ->
                                    Log.e("FirebaseRepository", "Error fetching balance", exception)
                                }
                            )
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isBuy) Green100 else Color.Gray
                        )
                    ) {
                        Text("Buy", style = MaterialTheme.typography.bodyMedium)
                    }

                    FilledTonalButton(
                        onClick = {
                            isBuy = false
                            amount = ""
                            val currency = btcCurrency
                            FirebaseRepository.fetchAvailableBalance(
                                userId = userId,
                                currency = currency,
                                onSuccess = { balance ->
                                    availableBalance = balance
                                },
                                onFailure = { exception ->
                                    Log.e("FirebaseRepository", "Error fetching balance", exception)
                                }
                            )
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (!isBuy) Red100 else Color.Gray
                        )
                    ) {
                        Text("Sell", style = MaterialTheme.typography.bodyMedium)
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextField(
                        value = amount,
                        onValueChange = { inputAmount ->
                            if (inputAmount.toDoubleOrNull() != null && inputAmount.toDouble() <= availableBalance) {
                                amount = inputAmount
                            }
                        },
                        modifier = Modifier.weight(1f),
                        label = { Text("Amount (Max: $availableBalance)") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        )
                    )

                    Text(text = if (isBuy) usdtCurrency else btcCurrency)

                    FilledTonalButton(
                        onClick = {
                            amount = availableBalance.toString()
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                    ) {
                        Text("Max")
                    }
                }

                val context = LocalContext.current

                FilledTonalButton(
                    onClick = {
                        if (amount.isBlank() || amount.toDoubleOrNull() == null || amount.toDouble() <= 0.0) {
                            Toast.makeText(context, "Please enter a valid amount!", Toast.LENGTH_SHORT).show()
                            return@FilledTonalButton
                        }
                        FirebaseRepository.fetchAvailableBalance(
                            userId = userId,
                            currency = btcCurrency,
                            onSuccess = { currentAmountBuyCurrency ->
                                FirebaseRepository.fetchAvailableBalance(
                                    userId = userId,
                                    currency = usdtCurrency,
                                    onSuccess = { currentAmountSellCurrency ->
                                        val (newAmountBuyCurrency, newAmountSellCurrency) = OrderManager.handleOrder(
                                            currentAmountBuyCurrency = currentAmountBuyCurrency,
                                            currentAmountSellCurrency = currentAmountSellCurrency,
                                            inputAmount = amount.toDouble(),
                                            price = price,
                                            isBuy = isBuy
                                        )
                                        Log.d("OrderDebug1", "1: $currentAmountBuyCurrency")
                                        Log.d("OrderDebug1", "2: $currentAmountSellCurrency")
                                        FirebaseRepository.updateBalances(
                                            userId = userId,
                                            buyCurrency = btcCurrency,
                                            sellCurrency = usdtCurrency,
                                            newAmountBuyCurrency = newAmountBuyCurrency,
                                            newAmountSellCurrency = newAmountSellCurrency,
                                            onSuccess = {
                                                Toast.makeText(context, "Order executed successfully!", Toast.LENGTH_SHORT).show()
                                                onDismiss()
                                            },
                                            onFailure = { exception ->
                                                Toast.makeText(context, "Order failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                                            }
                                        )
                                    },
                                    onFailure = { exception ->
                                        Log.e("FirebaseRepository", "Error fetching $btcCurrency balance", exception)
                                        Toast.makeText(context, "Failed to fetch $btcCurrency balance", Toast.LENGTH_SHORT).show()
                                    }
                                )
                            },
                            onFailure = { exception ->
                                Log.e("FirebaseRepository", "Error fetching $usdtCurrency balance", exception)
                                Toast.makeText(context, "Failed to fetch $usdtCurrency balance", Toast.LENGTH_SHORT).show()
                            }
                        )
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Place Order", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
