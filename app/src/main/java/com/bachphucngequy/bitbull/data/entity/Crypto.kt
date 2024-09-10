package com.bachphucngequy.bitbull.data.entity

enum class Crypto(val symbol: String, val code: String, val fullName: String) {
    BITCOIN(symbol = "BTCUSDT", code = "BTC", fullName = "Bitcoin"),
    ETHEREUM(symbol = "ETHUSDT", code = "ETH", fullName = "Ethereum"),
    CARDANO(symbol = "ADAUSDT", code = "ADA", fullName = "Cardano"),
    CHAINLINK(symbol = "LINKUSDT", code = "LINK", fullName = "Chainlink"),
    LITECOIN(symbol = "LTCUSDT", code = "LTC", fullName = "Litecoin")
}