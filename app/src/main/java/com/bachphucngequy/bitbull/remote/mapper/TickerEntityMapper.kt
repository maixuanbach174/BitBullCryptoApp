package com.bachphucngequy.bitbull.remote.mapper

import com.bachphucngequy.bitbull.data.entity.TickerEntity
import com.bachphucngequy.bitbull.remote.model.RemoteTicker
import javax.inject.Inject

class TickerEntityMapper @Inject constructor() : EntityMapper<RemoteTicker, TickerEntity> {

    override fun mapFromRemote(type: RemoteTicker): TickerEntity {
        return TickerEntity(
            productId = type.productId,
            price = type.price,
            openPrice = type.openPrice,
            volume24 = type.volume24,
            volumeMonth = type.volume30d,
            low24 = type.low24,
            high24 = type.high24
        )
    }
}