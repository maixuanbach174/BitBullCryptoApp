package com.bachphucngequy.bitbull.cache.source

import com.bachphucngequy.bitbull.cache.db.TickerDao
import com.bachphucngequy.bitbull.cache.mapper.TickerEntityMapper
import com.bachphucngequy.bitbull.data.datasource.cache.TickerCacheDataSource
import com.bachphucngequy.bitbull.data.entity.TickerEntity
import javax.inject.Inject


class TickerCacheDatasourceImpl @Inject constructor(
    private val tickerDao: TickerDao,
    private val tickerEntityMapper: TickerEntityMapper
) : TickerCacheDataSource {

    override suspend fun getAllTicker(): List<TickerEntity> {
        return tickerDao.getAllVehicles()
            .map { cachedVehicle ->
                tickerEntityMapper.mapFromCached(
                    cachedVehicle
                )
            }
    }

    override suspend fun upsertTicker(tickerEntity: TickerEntity) {
        tickerDao.updateVehicle(tickerEntityMapper.mapToCached(tickerEntity))
    }


}