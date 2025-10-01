package com.example.travelapp.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
@Entity(tableName = "attractions")
data class Attraction(
    @PrimaryKey(autoGenerate = false)
    val id: String = UUID.randomUUID().toString(),
    val dayId: String, // Foreign key to Day
    val name: String = "",
    val location: String = "",
    val image: String? = null,
    val time: String = "",
    val description: String = "",
    val rating: Float = 0f
) : Parcelable
