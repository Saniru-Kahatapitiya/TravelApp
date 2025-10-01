package com.example.travelapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Destination(
    val name: String,
    val images: IntArray,
    val description: String
) : Parcelable
