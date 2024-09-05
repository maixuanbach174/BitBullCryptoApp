package com.bachphucngequy.bitbull.remote.socket

import com.bachphucngequy.bitbull.remote.model.Subscribe
import com.bachphucngequy.bitbull.remote.model.RemoteTicker
import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import kotlinx.coroutines.flow.Flow

interface CoinbaseService {

    /**
     * Observe WebSocket event
     */
    @Receive
    fun observeWebSocket(): Flow<WebSocket.Event>

    /**
     * Send message(what you want to receive) to get response from WebSocket
     */
    @Send
    fun sendSubscribe(subscribe: Subscribe)

    /**
     * Response from WebSocket
     */
    @Receive
    fun observeTicker(): Flow<RemoteTicker>
}