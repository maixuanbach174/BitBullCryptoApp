package com.bachphucngequy.bitbull.data.mapper

import com.bachphucngequy.bitbull.data.entity.Crypto
import com.bachphucngequy.bitbull.data.entity.TickerEntity
import com.bachphucngequy.bitbull.domain.model.Ticker
import javax.inject.Inject

class TickerMapper @Inject constructor() : Mapper<TickerEntity, Ticker> {

    override fun mapFromEntity(entity: TickerEntity): Ticker {
        val enum = Crypto.values().find { crypto -> crypto.symbol == entity.symbol }
        return Ticker(
            symbol = entity.symbol ?: "",
            priceChange = entity.priceChange?.toDouble() ?: 0.0,
            priceChangePercent = entity.priceChangePercent?.toDouble() ?: 0.0,
            productCode = enum?.code
                ?: "",
            productName = enum?.fullName
                ?: "",
            quoteCode = enum?.quoteCode ?: "",
            lastPrice = entity.lastPrice?.toDouble() ?: 0.0,
            openPrice = entity.openPrice?.toDouble() ?: 0.0,
            baseAssetVolume = entity.baseAssetVolume?.toDouble() ?: 0.0,
            lowPrice = entity.lowPrice?.toDouble() ?: 0.0,
            highPrice = entity.highPrice?.toDouble() ?: 0.0,
            quoteAssetVolume = entity.quoteAssetVolume?.toDouble() ?: 0.0,
        )
    }

    override fun mapToEntity(domainModel: Ticker): TickerEntity {
        return TickerEntity()
    }
}