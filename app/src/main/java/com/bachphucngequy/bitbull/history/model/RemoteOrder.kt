package com.bachphucngequy.bitbull.history.model

data class RemoteOrder(
    val amount: Double = 0.00,
    val date: String = "",
    val price: Double = 0.00,
    val t1: String = "",
    val t2: String = "",
    val type: String = "",
    val userID: String = "",
)
