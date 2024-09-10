package com.bachphucngequy.bitbull.data.entity

data class TickerEntity(
    val symbol: String? = null,
    val priceChange: String? = null,
    val priceChangePercent: String? = null,
    val lastPrice: String? = null,
    val openPrice: String? = null,
    val highPrice: String? = null,
    val lowPrice: String? = null,
    val baseAssetVolume: String? = null,
    val quoteAssetVolume: String? = null
)