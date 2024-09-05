package com.bachphucngequy.bitbull.cache.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bachphucngequy.bitbull.cache.model.CachedTicker

@Dao
interface TickerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateVehicle(entity: CachedTicker): Long

    @Query("SELECT * FROM ticker")
    fun getAllVehicles(): List<CachedTicker>


}