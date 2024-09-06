package com.bachphucngequy.bitbull.data

import com.bachphucngequy.bitbull.data.entity.CoinDetailDto
import com.bachphucngequy.bitbull.data.entity.CoinDto
import retrofit2.http.GET
import retrofit2.http.Path

interface CoinPaprikaApi {
    @GET("/v1/coins")
    suspend fun getCoins(): List<CoinDto>

    @GET("/v1/coins/{coinId}")
    suspend fun getCoinById(@Path("coinId") coinId: String): CoinDetailDto

    companion object {
        const val BASE_URL = "https://api.coinpaprika.com"
    }
}