package com.bachphucngequy.bitbull.retrofit

import com.bachphucngequy.bitbull.data.BinanceApi
import com.bachphucngequy.bitbull.data.CoinPaprikaApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client: OkHttpClient = OkHttpClient
        .Builder().addInterceptor(interceptor)
        .build()

    val binanceApi: BinanceApi = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BinanceApi.BASE_URL)
        .client(client)
        .build()
        .create(BinanceApi::class.java)

    val coinPaprikaApi: CoinPaprikaApi = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(CoinPaprikaApi.BASE_URL)
        .client(client)
        .build()
        .create(CoinPaprikaApi::class.java) 

}