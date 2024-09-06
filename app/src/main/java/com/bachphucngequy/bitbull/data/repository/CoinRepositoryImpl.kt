package com.bachphucngequy.bitbull.data.repository

import com.bachphucngequy.bitbull.data.CoinPaprikaApi
import com.bachphucngequy.bitbull.data.entity.toCoin
import com.bachphucngequy.bitbull.data.entity.toCoinDetail
import com.bachphucngequy.bitbull.domain.model.Coin
import com.bachphucngequy.bitbull.domain.model.CoinDetail
import com.bachphucngequy.bitbull.domain.repository.CoinRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import com.bachphucngequy.bitbull.data.Result

class CoinRepositoryImpl(
    private val api: CoinPaprikaApi
): CoinRepository {
    override suspend fun getCoins(): Flow<com.bachphucngequy.bitbull.data.Result<List<Coin>>> {
        return flow {
            val coinsFromApi = try {
                api.getCoins().map { it.toCoin() }
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Result.Error(null, "Error loading coins from API"))
                return@flow
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Result.Error(null, "Error loading coins from API"))
                return@flow
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Result.Error(null, "Error loading coins from API"))
                return@flow
            }

            emit(Result.Success(coinsFromApi))
        }
    }

    override suspend fun getCoinById(coinId: String): Flow<Result<CoinDetail>> {
        return flow {
            val coinDetailFromApi = try {
                api.getCoinById(coinId).toCoinDetail()
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Result.Error(null, "Error loading coin detail from API"))
                return@flow
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Result.Error(null, "Error loading coin detail from API"))
                return@flow
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Result.Error(null, "Error loading coin detail from API"))
                return@flow
            }

            emit(Result.Success(coinDetailFromApi))
        }
    }
}