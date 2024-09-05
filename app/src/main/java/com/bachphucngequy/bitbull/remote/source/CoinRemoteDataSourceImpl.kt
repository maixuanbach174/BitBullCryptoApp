package com.bachphucngequy.bitbull.remote.source

import com.bachphucngequy.bitbull.data.datasource.remote.TickerRemoteDataSource
import com.bachphucngequy.bitbull.data.entity.TickerEntity
import com.bachphucngequy.bitbull.remote.mapper.TickerEntityMapper
import com.bachphucngequy.bitbull.remote.model.Subscribe
import com.bachphucngequy.bitbull.remote.socket.CoinbaseService
import com.tinder.scarlet.WebSocket
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class CoinRemoteDataSourceImpl @Inject constructor(
    private val coinbaseService: CoinbaseService,
    private val tickerEntityMapper: TickerEntityMapper
) : TickerRemoteDataSource {

    override fun observeTicker(): Flow<TickerEntity> {
        return coinbaseService.observeTicker()
            .map { remoteTicker -> tickerEntityMapper.mapFromRemote(remoteTicker) }
    }

    override fun sendSubscribe(subscribe: Subscribe) {
        coinbaseService.sendSubscribe(subscribe)
    }

    override fun observeEvent(): Flow<WebSocket.Event> {
        return coinbaseService.observeWebSocket()
    }
}