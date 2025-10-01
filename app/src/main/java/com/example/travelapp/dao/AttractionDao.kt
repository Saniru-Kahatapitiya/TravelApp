package com.example.travelapp.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.travelapp.models.Attraction
import kotlinx.coroutines.flow.Flow

@Dao
interface AttractionDao {
    @Query("SELECT * FROM attractions WHERE dayId = :dayId")
    fun getAttractionsForDay(dayId: String): Flow<List<Attraction>>

    @Query("SELECT * FROM attractions WHERE id = :attractionId")
    fun getAttractionById(attractionId: String): Flow<Attraction>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttraction(attraction: Attraction)

    @Update
    suspend fun updateAttraction(attraction: Attraction)

    @Query("DELETE FROM attractions WHERE id = :attractionId")
    suspend fun deleteAttraction(attractionId: String)
}
