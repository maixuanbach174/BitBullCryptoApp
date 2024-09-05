package com.bachphucngequy.bitbull.data.datasource.remote

import com.bachphucngequy.bitbull.data.entity.TickerEntity
import com.bachphucngequy.bitbull.remote.model.Subscribe
import com.tinder.scarlet.WebSocket
import kotlinx.coroutines.flow.Flow

interface TickerRemoteDataSource {

    fun observeTicker(): Flow<TickerEntity>

    fun sendSubscribe(subscribe: Subscribe)

    fun observeEvent(): Flow<WebSocket.Event>
}