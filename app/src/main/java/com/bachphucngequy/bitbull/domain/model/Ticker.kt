package com.bachphucngequy.bitbull.domain.model

data class Ticker(
    val symbol: String,
    val priceChange: Double,
    val priceChangePercent: Double,
    val productCode: String,
    val productName: String,
    val lastPrice: Double,
    val openPrice: Double,
    val baseAssetVolume: Double,
    val lowPrice: Double,
    val highPrice: Double,
    val quoteAssetVolume: Double
)