package com.papageorgiouk.simpleweatherifx.data

import androidx.room.*
import com.papageorgiouk.simpleweatherifx.core.DatabaseDataSource
import com.papageorgiouk.simpleweatherifx.domain.WeatherDataEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface WeatherDao {
    @Query("SELECT * FROM history")  //  order by last observation timestamp
    fun getHistory(): Flow<List<WeatherDataEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateWithData(entry: WeatherDataEntity)

    @Query("SELECT * FROM history WHERE primaryKey == :key")
    suspend fun getByPrimaryKey(key: Int): WeatherDataEntity

    @Delete
    suspend fun delete(entry: WeatherDataEntity)
}

@Database(entities = [WeatherDataEntity::class], version = 1)
abstract class WeatherDatabase : RoomDatabase(), DatabaseDataSource<WeatherDataEntity> {

    internal abstract fun weatherDao(): WeatherDao

    override fun getHistory(): Flow<List<WeatherDataEntity>> = weatherDao().getHistory()

    override suspend fun getByPrimaryKey(key: Int): WeatherDataEntity = weatherDao().getByPrimaryKey(key)

    override suspend fun add(data: WeatherDataEntity) {
        weatherDao().updateWithData(data)
    }

    override suspend fun remove(data: WeatherDataEntity) {
        weatherDao().delete(data)
    }

}