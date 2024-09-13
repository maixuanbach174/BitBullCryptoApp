package com.bachphucngequy.bitbull.history.model

data class BuySellHistoryItem(
    val t1: String = "",
    val t2: String = "",
    val date: String = "",
    val type: String = "",
    val amount: Double = 0.00,
    val price: Double = 0.00,
    val status: String = "Completed"
)
