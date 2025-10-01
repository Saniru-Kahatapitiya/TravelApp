package com.example.travelapp.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.Date
import java.util.UUID

@Parcelize
@Entity(tableName = "days")
data class Day(
    @PrimaryKey(autoGenerate = false)
    val id: String = UUID.randomUUID().toString(),
    val tripId: String, // Foreign key to Trip
    val date: Date,
    // Removed attractions as it will be handled by a separate Attraction entity and relationships
) : Parcelable
