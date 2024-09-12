package com.bachphucngequy.bitbull.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RemoteTicker(
    @Json(name = "e") val eventType: String,          // Event type
    @Json(name = "E") val eventTime: Long,            // Event time
    @Json(name = "s") val symbol: String,             // Symbol
    @Json(name = "p") val priceChange: String,        // Price change
    @Json(name = "P") val priceChangePercent: String, // Price change percent
    @Json(name = "w") val weightedAvgPrice: String,   // Weighted average price
    @Json(name = "x") val firstTradePrice: String,    // First trade (F)-1 price
    @Json(name = "c") val lastPrice: String,          // Last price
    @Json(name = "Q") val lastQuantity: String,       // Last quantity
    @Json(name = "b") val bestBidPrice: String,       // Best bid price
    @Json(name = "B") val bestBidQuantity: String,    // Best bid quantity
    @Json(name = "a") val bestAskPrice: String,       // Best ask price
    @Json(name = "A") val bestAskQuantity: String,    // Best ask quantity
    @Json(name = "o") val openPrice: String,          // Open price
    @Json(name = "h") val highPrice: String,          // High price
    @Json(name = "l") val lowPrice: String,           // Low price
    @Json(name = "v") val baseAssetVolume: String,    // Total traded base asset volume
    @Json(name = "q") val quoteAssetVolume: String,   // Total traded quote asset volume
    @Json(name = "O") val statisticsOpenTime: Long,   // Statistics open time
    @Json(name = "C") val statisticsCloseTime: Long,  // Statistics close time
    @Json(name = "F") val firstTradeId: Long,         // First trade ID
    @Json(name = "L") val lastTradeId: Long,          // Last trade ID
    @Json(name = "n") val totalTrades: Long           // Total number of trades
)