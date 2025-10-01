package com.example.travelapp.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.Date
import java.util.UUID

@Parcelize
@Entity(tableName = "collections")
data class Collection(
    @PrimaryKey(autoGenerate = false)
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val coverImage: String? = null,
    // Removed 'images' as it will be handled by a separate SavedImage entity and relationships
    val createdAt: Date = Date()
) : Parcelable
