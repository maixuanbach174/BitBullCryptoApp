package com.bachphucngequy.bitbull.data.entity

enum class Crypto(val symbol: String, val code: String, val fullName: String, val quoteCode: String) {
    BITCOIN(symbol = "BTCUSDT", code = "BTC", fullName = "Bitcoin", quoteCode = "USDT"),
    ETHEREUM(symbol = "ETHUSDT", code = "ETH", fullName = "Ethereum", quoteCode = "USDT"),
    CARDANO(symbol = "ADAUSDT", code = "ADA", fullName = "Cardano", quoteCode = "USDT"),
    CHAINLINK(symbol = "LINKUSDT", code = "LINK", fullName = "Chainlink", quoteCode = "USDT"),
    LITECOIN(symbol = "LTCUSDT", code = "LTC", fullName = "Litecoin", quoteCode = "USDT");

    companion object {
        fun getCompanyName(symbol: String): String {
            return when(symbol) {
                BITCOIN.symbol -> BITCOIN.fullName
                ETHEREUM.symbol -> ETHEREUM.fullName
                CARDANO.symbol -> CARDANO.fullName
                CHAINLINK.symbol -> CHAINLINK.fullName
                LITECOIN.symbol -> LITECOIN.fullName
                else -> ""
            }
        }
    }
}