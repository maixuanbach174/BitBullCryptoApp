package com.bachphucngequy.bitbull.remote.mapper

import com.bachphucngequy.bitbull.data.entity.TickerEntity
import com.bachphucngequy.bitbull.remote.model.RemoteTicker
import javax.inject.Inject

class TickerEntityMapper @Inject constructor() : EntityMapper<RemoteTicker, TickerEntity> {

    override fun mapFromRemote(type: RemoteTicker): TickerEntity {
        return TickerEntity(
            symbol = type.symbol,
            priceChange = type.priceChange,
            priceChangePercent = type.priceChangePercent,
            lastPrice = type.lastPrice,
            openPrice = type.openPrice,
            highPrice = type.highPrice,
            lowPrice = type.lowPrice,
            baseAssetVolume = type.baseAssetVolume,
            quoteAssetVolume = type.quoteAssetVolume
        )
    }
}