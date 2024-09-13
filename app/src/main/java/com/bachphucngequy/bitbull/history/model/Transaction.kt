package com.bachphucngequy.bitbull.history.model

data class Transaction(
    val userIDto: String = "",
    val amount: Double = 0.00,
    val currency: String = "",
    val date: String = "",
    val note: String = "",
    val userIDfrom: String = "",
)