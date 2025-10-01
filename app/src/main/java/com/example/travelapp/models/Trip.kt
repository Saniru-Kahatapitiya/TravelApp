package com.example.travelapp.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.Date
import java.util.UUID

@Parcelize
@Entity(tableName = "trips")
data class Trip(
    @PrimaryKey(autoGenerate = false) // Assuming ID is generated elsewhere or is unique
    val id: String = UUID.randomUUID().toString(), // Generate a unique ID by default
    val name: String = "",
    val coverImage: String? = null,
    val startDate: Date? = null,
    val endDate: Date? = null,
    // Removed 'days' as it will be handled by a separate Day entity and relationships
    val isCompleted: Boolean = false,
    val createdAt: Date = Date()
) : Parcelable
