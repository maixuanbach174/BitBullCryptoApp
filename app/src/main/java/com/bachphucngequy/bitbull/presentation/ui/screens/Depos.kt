package com.bachphucngequy.bitbull.presentation.ui.screens

import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bachphucngequy.bitbull.firebase.BlankBox
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Deposit(number: String, navController: NavController) {
    var qrCodeBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var walletId by remember { mutableStateOf<String?>(null) }

    val db = FirebaseFirestore.getInstance()
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    val textToCopy by remember { derivedStateOf { walletId ?: "" } }

    LaunchedEffect(number) {
        db.collection("wallet")
            .whereEqualTo("UserID", number)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    walletId = document.getString("walletID")

                    Log.d("DepositScreen", "Document data: ${document.data}")
                    qrCodeBitmap = generateQRCode(walletId ?: "")
                    break
                }
            }
            .addOnFailureListener { exception ->
                Log.e("DepositScreen", "Error getting documents: ", exception)
            }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        BlankBox()
        TopAppBar(
            title = {
                Text(
                        text = "Deposit USDT",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = Color.Black
                )
            },
            navigationIcon = {
                IconButton(onClick = {
                    navController.popBackStack() // Navigate back in the navigation stack
                }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }
            },
            actions = {
                IconButton(onClick = { /* Handle history action */ }) {
                    Icon(Icons.Default.History, contentDescription = "History", tint = Color.Black)
                }
                IconButton(onClick = { /* Handle help action */ }) {
                    Icon(Icons.Default.HelpOutline, contentDescription = "Help", tint = Color.Black)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.Gray, shape = RoundedCornerShape(8.dp))
                .align(Alignment.CenterHorizontally)
        ) {
            qrCodeBitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "QR Code",
                    modifier = Modifier
                        .size(200.dp)
                        .align(Alignment.Center)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .border(
                    width = 1.dp,
                    color = Color.Blue,
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Network",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Binance ID",
                        color = Color.Black,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Text(
                        text = "* Contract Information ***97955",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Deposit Address",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text(
                            text = "$walletId",
                            color = Color.Black,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(onClick = {
                            clipboardManager.setText(AnnotatedString(textToCopy))
                            Toast.makeText(context, "Address copied", Toast.LENGTH_SHORT).show()
                        }) {
                            Icon(Icons.Default.ContentCopy, contentDescription = "Copy Address", tint = Color.Black)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp)
        ) {
            Button(
                onClick = {
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, textToCopy)
                        type = "text/plain"
                    }

                    val chooserIntent = Intent.createChooser(intent, "Share Address")

                    if (intent.resolveActivity(context.packageManager) != null) {
                        context.startActivity(chooserIntent)
                    } else {
                        Toast.makeText(context, "No messaging apps found", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107))
            ) {
                Text(
                    text = "Save and Share Address",
                    color = Color.Black,
                    style = MaterialTheme.typography.labelLarge
                )
            }



        }

    }
}

fun generateQRCode(text: String): Bitmap? {
    return try {
        val bitMatrix: BitMatrix = MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, 200, 200)
        val barcodeEncoder = BarcodeEncoder()
        barcodeEncoder.createBitmap(bitMatrix)
    } catch (e: Exception) {
        null
    }
}
