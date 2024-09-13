package com.bachphucngequy.bitbull.history.model

data class HistoryItem (
    val coinCode: String,
    val date: String,
    val amount: Double,
    val status: String = "Completed"
)

//val sampleHistoryItems = listOf(
//    HistoryItem("USDT", "2021-04-28 15:14:02", 14.71, "Completed"),
//    HistoryItem("USDT", "2021-04-23 00:50:50", 14.71, "Completed"),
//    HistoryItem("USDT", "2021-04-22 19:43:48", 14.71, "Completed"),
//    HistoryItem("USDT", "2021-02-23 15:22:33", 14.71, "Completed"),
//    HistoryItem("USDT", "2021-02-16 16:38:37", 14.71, "Completed")
//)
