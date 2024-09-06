package com.bachphucngequy.bitbull.tweets.common.util

import com.bachphucngequy.bitbull.tweets.common.util.Constants.EVENT_BUS_BUFFER_CAPACITY
import com.bachphucngequy.bitbull.tweets.data.Post
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object EventBus {
    private val _events = MutableSharedFlow<Event>(extraBufferCapacity = EVENT_BUS_BUFFER_CAPACITY)
    val events = _events.asSharedFlow()

    suspend fun send(event: Event) {
        _events.emit(event)
    }
}
sealed interface Event{
    data class PostUpdated(val post: Post): Event
}