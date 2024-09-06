package com.bachphucngequy.bitbull.domain.repository

import com.bachphucngequy.bitbull.domain.model.Coin
import com.bachphucngequy.bitbull.domain.model.CoinDetail
import kotlinx.coroutines.flow.Flow
import com.bachphucngequy.bitbull.data.Result

interface CoinRepository {
    suspend fun getCoins(): Flow<Result<List<Coin>>>
    suspend fun getCoinById(coinId: String): Flow<Result<CoinDetail>>
}