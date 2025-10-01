package com.example.travelapp.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.travelapp.models.SavedImage
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedImageDao {
    @Query("SELECT * FROM saved_images WHERE collectionId = :collectionId")
    fun getImagesForCollection(collectionId: String): Flow<List<SavedImage>>

    @Query("SELECT * FROM saved_images WHERE id = :imageId")
    fun getImageById(imageId: String): Flow<SavedImage>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(image: SavedImage)

    @Update
    suspend fun updateImage(image: SavedImage)

    @Query("DELETE FROM saved_images WHERE id = :imageId")
    suspend fun deleteImage(imageId: String)
}
