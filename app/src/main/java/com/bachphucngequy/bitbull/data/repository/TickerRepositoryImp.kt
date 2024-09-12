package com.bachphucngequy.bitbull.data.repository

import com.bachphucngequy.bitbull.data.datasource.remote.TickerRemoteDataSource
import com.bachphucngequy.bitbull.data.entity.Crypto
import com.bachphucngequy.bitbull.data.mapper.TickerMapper
import com.bachphucngequy.bitbull.domain.model.ConnectionState
import com.bachphucngequy.bitbull.domain.repository.TickerRepository
import com.bachphucngequy.bitbull.remote.model.Subscribe
import com.tinder.scarlet.WebSocket
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TickerRepositoryImpl @Inject constructor(
    private val tickerRemoteDataSource: TickerRemoteDataSource,
    private val tickerMapper: TickerMapper,
) : TickerRepository {

    private val messageScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val connectionCount = AtomicInteger(0)
    private val messageChannel = Channel<() -> Unit>(Channel.UNLIMITED)
    private val rateLimiter = RateLimiter(5, 1000) // 5 messages per second

    init {
        messageScope.launch {
            for (message in messageChannel) {
                rateLimiter.acquire()
                message()
            }
        }

        // Reset connection count every 5 minutes
        messageScope.launch {
            while (isActive) {
                delay(5 * 60 * 1000L)
                connectionCount.set(0)
            }
        }
    }

    override fun observeEvent(): Flow<ConnectionState> {
        return tickerRemoteDataSource.observeEvent()
            .map { event ->
                when (event) {
                    is WebSocket.Event.OnConnectionOpened<*> -> {
                        if (connectionCount.incrementAndGet() > 300) {
                            ConnectionState.Disconnected
                        } else {
                            subscribeTicker()
                            ConnectionState.Connected
                        }
                    }
                    is WebSocket.Event.OnMessageReceived -> ConnectionState.Connected
                    is WebSocket.Event.OnConnectionClosing,
                    is WebSocket.Event.OnConnectionClosed -> {
                        ConnectionState.Disconnected
                    }
                    else -> ConnectionState.Disconnected
                }
            }
    }

    override fun observeTicker(): Flow<ConnectionState> = flow {
        emitAll(tickerRemoteDataSource.observeTicker().map {
            ConnectionState.Success(tickerMapper.mapFromEntity(it))
        })
    }

    override fun subscribeTicker() {
        tickerRemoteDataSource.sendSubscribe(
            Subscribe(
                1,
                params = Crypto.values().map { it.symbol.lowercase() + "@ticker" },
            )
        )
    }

    private class RateLimiter(private val rate: Int, private val per: Long) {
        private val availableTokens = AtomicInteger(rate)
        private var lastRefillTime = System.currentTimeMillis()

        suspend fun acquire() {
            while (true) {
                refill()
                if (availableTokens.decrementAndGet() >= 0) {
                    return
                }
                availableTokens.incrementAndGet()
                delay(per / rate)
            }
        }

        private fun refill() {
            val now = System.currentTimeMillis()
            val timeElapsed = now - lastRefillTime
            val refill = (timeElapsed * rate / per).toInt()
            if (refill > 0) {
                availableTokens.addAndGet(refill)
                lastRefillTime = now
            }
            if (availableTokens.get() > rate) {
                availableTokens.set(rate)
            }
        }
    }
}
