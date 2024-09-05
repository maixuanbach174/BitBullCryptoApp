package com.bachphucngequy.bitbull.data.datasource.cache

import com.bachphucngequy.bitbull.data.entity.TickerEntity

interface TickerCacheDataSource {

    suspend fun getAllTicker():List<TickerEntity>

    suspend fun upsertTicker(tickerEntity: TickerEntity)
}