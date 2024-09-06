package com.bachphucngequy.bitbull.data.repository

import com.bachphucngequy.bitbull.data.BinanceApi
import com.bachphucngequy.bitbull.data.entity.SymbolPriceTicker
import com.bachphucngequy.bitbull.domain.repository.SymbolPriceTickerRepository
import kotlinx.coroutines.flow.Flow
import com.bachphucngequy.bitbull.data.Result
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException

class SymbolPriceTickerRepositoryImpl(
    private val api: BinanceApi
): SymbolPriceTickerRepository {
    override suspend fun getSymbolPriceTicker(): Flow<Result<SymbolPriceTicker>> {
        return flow {
            val tickerFromApi = try {
                api.getSymbolPriceTicker()
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Result.Error(null, "Error loading ticker from API"))
                return@flow
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Result.Error(null, "Error loading ticker from API"))
                return@flow
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Result.Error(null, "Error loading ticker from API"))
                return@flow
            }

            emit(Result.Success(tickerFromApi))
        }
    }
}