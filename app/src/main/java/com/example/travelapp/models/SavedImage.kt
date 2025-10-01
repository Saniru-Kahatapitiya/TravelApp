package com.example.travelapp.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.Date
import java.util.UUID

@Parcelize
@Entity(tableName = "saved_images")
data class SavedImage(
    @PrimaryKey(autoGenerate = false)
    val id: String = UUID.randomUUID().toString(),
    val collectionId: String, // Foreign key to Collection
    val name: String = "",
    val imagePath: String = "",
    val fileSize: String = "",
    val isDownloaded: Boolean = false,
    val isStarred: Boolean = false,
    val downloadedAt: Date? = null
) : Parcelable
