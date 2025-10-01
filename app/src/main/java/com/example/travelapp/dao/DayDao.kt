package com.example.travelapp.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.travelapp.models.Day
import kotlinx.coroutines.flow.Flow

@Dao
interface DayDao {
    @Query("SELECT * FROM days WHERE tripId = :tripId")
    fun getDaysForTrip(tripId: String): Flow<List<Day>>

    @Query("SELECT * FROM days WHERE id = :dayId")
    fun getDayById(dayId: String): Flow<Day>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDay(day: Day)

    @Update
    suspend fun updateDay(day: Day)

    @Query("DELETE FROM days WHERE id = :dayId")
    suspend fun deleteDay(dayId: String)
}
