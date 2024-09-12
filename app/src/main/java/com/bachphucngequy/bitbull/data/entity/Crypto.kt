package com.bachphucngequy.bitbull.data.entity

enum class Crypto(
    val symbol: String,
    val code: String,
    val fullName: String,
    val quoteCode: String,
    var isFavourite: Boolean = false
) {
    BITCOIN(symbol = "BTCUSDT", code = "BTC", fullName = "Bitcoin", quoteCode = "USDT"),
    ETHEREUM(symbol = "ETHUSDT", code = "ETH", fullName = "Ethereum", quoteCode = "USDT"),
    CARDANO(symbol = "ADAUSDT", code = "ADA", fullName = "Cardano", quoteCode = "USDT"),
    CHAINLINK(symbol = "LINKUSDT", code = "LINK", fullName = "Chainlink", quoteCode = "USDT"),
    LITECOIN(symbol = "LTCUSDT", code = "LTC", fullName = "Litecoin", quoteCode = "USDT"),
    AVALANCHE(symbol = "AVAXUSDT", code = "AVAX", fullName = "Avalanche", quoteCode = "USDT"),
    POLKADOT(symbol = "DOTUSDT", code = "DOT", fullName = "Polkadot", quoteCode = "USDT"),
    SOLANA(symbol = "SOLUSDT", code = "SOL", fullName = "Solana", quoteCode = "USDT"),
    UNISWAP(symbol = "UNIUSDT", code = "UNI", fullName = "Uniswap", quoteCode = "USDT"),
    BINANCE_COIN(symbol = "BNBUSDT", code = "BNB", fullName = "Binance Coin", quoteCode = "USDT"),
    DOGECOIN(symbol = "DOGEUSDT", code = "DOGE", fullName = "Dogecoin", quoteCode = "USDT"),
    STELLAR(symbol = "XLMUSDT", code = "XLM", fullName = "Stellar", quoteCode = "USDT"),
    EOS(symbol = "EOSUSDT", code = "EOS", fullName = "EOS", quoteCode = "USDT"),
    TRON(symbol = "TRXUSDT", code = "TRX", fullName = "Tron", quoteCode = "USDT"),
    LEO(symbol = "LEOUSDT", code = "LEO", fullName = "LEO", quoteCode = "USDT"),
    COSMOS(symbol = "ATOMUSDT", code = "ATOM", fullName = "Cosmos", quoteCode = "USDT"),
    TEZOS(symbol = "XTZUSDT", code = "XTZ", fullName = "Tezos", quoteCode = "USDT"),
    MONERO(symbol = "XMRUSDT", code = "XMR", fullName = "Monero", quoteCode = "USDT"),
    DASH(symbol = "DASHUSDT", code = "DASH", fullName = "Dash", quoteCode = "USDT"),
    ZCASH(symbol = "ZECUSDT", code = "ZEC", fullName = "Zcash", quoteCode = "USDT"),
    WAVES(symbol = "WAVESUSDT", code = "WAVES", fullName = "Waves", quoteCode = "USDT"),
    NEM(symbol = "XEMUSDT", code = "XEM", fullName = "NEM", quoteCode = "USDT"),
    IOTA(symbol = "IOTAUSDT", code = "IOTA", fullName = "IOTA", quoteCode = "USDT"),
    NEO(symbol = "NEOUSDT", code = "NEO", fullName = "NEO", quoteCode = "USDT"),
    TONCOIN(symbol = "TONUSDT", code = "TON", fullName = "TONCOIN", quoteCode = "USDT"),
    WETH(symbol = "WETHUSDT", code = "WETH", fullName = "WETH", quoteCode = "USDT"),
    POLYGON(symbol = "MATICUSDT", code = "MATIC", fullName = "Polygon", quoteCode = "USDT"),
    POLKADOT_NEW(symbol = "DOTUSDT", code = "DOT", fullName = "Polkadot", quoteCode = "USDT"),
    KASPA(symbol = "KASUSDT", code = "KASPA", fullName = "Kaspa", quoteCode = "USDT"),
    ;

    companion object {
        fun getCompanyName(symbol: String): String {
            return when(symbol) {
                BITCOIN.symbol -> BITCOIN.fullName
                ETHEREUM.symbol -> ETHEREUM.fullName
                CARDANO.symbol -> CARDANO.fullName
                CHAINLINK.symbol -> CHAINLINK.fullName
                LITECOIN.symbol -> LITECOIN.fullName
                AVALANCHE.symbol -> AVALANCHE.fullName
                POLKADOT.symbol -> POLKADOT.fullName
                SOLANA.symbol -> SOLANA.fullName
                UNISWAP.symbol -> UNISWAP.fullName
                BINANCE_COIN.symbol -> BINANCE_COIN.fullName
                DOGECOIN.symbol -> DOGECOIN.fullName
                STELLAR.symbol -> STELLAR.fullName
                EOS.symbol -> EOS.fullName
                TRON.symbol -> TRON.fullName
                LEO.symbol -> LEO.fullName
                COSMOS.symbol -> COSMOS.fullName
                TEZOS.symbol -> TEZOS.fullName
                MONERO.symbol -> MONERO.fullName
                DASH.symbol -> DASH.fullName
                ZCASH.symbol -> ZCASH.fullName
                WAVES.symbol -> WAVES.fullName
                NEM.symbol -> NEM.fullName
                IOTA.symbol -> IOTA.fullName
                NEO.symbol -> NEO.fullName
                TONCOIN.symbol -> TONCOIN.fullName
                WETH.symbol -> WETH.fullName
                POLYGON.symbol -> POLYGON.fullName
                POLKADOT_NEW.symbol -> POLKADOT_NEW.fullName
                KASPA.symbol -> KASPA.fullName
                else -> ""
            }
        }
    }
}