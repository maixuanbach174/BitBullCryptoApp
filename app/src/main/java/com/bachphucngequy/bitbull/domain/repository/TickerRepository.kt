package com.bachphucngequy.bitbull.domain.repository

import com.bachphucngequy.bitbull.domain.model.ConnectionState
import kotlinx.coroutines.flow.Flow

interface TickerRepository {

    fun observeEvent(): Flow<ConnectionState>

    fun observeTicker(): Flow<ConnectionState>

    fun subscribeTicker()

}