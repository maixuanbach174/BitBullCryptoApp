package com.bachphucngequy.bitbull.domain.repository

import com.bachphucngequy.bitbull.data.entity.SymbolPriceTicker
import kotlinx.coroutines.flow.Flow
import com.bachphucngequy.bitbull.data.Result

interface SymbolPriceTickerRepository {
    suspend fun getSymbolPriceTicker(): Flow<Result<SymbolPriceTicker>>
}