package com.bachphucngequy.bitbull.data

import com.bachphucngequy.bitbull.data.entity.SymbolPriceTicker
import retrofit2.http.GET

interface BinanceApi {
    @GET("api/v3/ticker/price")
    suspend fun getSymbolPriceTicker(): SymbolPriceTicker
    companion object{
        const val BASE_URL = "https://api.binance.com/"
    }
}