package com.bachphucngequy.bitbull.data.di

import com.bachphucngequy.bitbull.data.repository.TickerRepositoryImpl
import com.bachphucngequy.bitbull.domain.repository.TickerRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindsCoinbaseRepository(tickerRepositoryImpl: TickerRepositoryImpl): TickerRepository
}