package com.example.travelapp

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.travelapp.dao.AttractionDao
import com.example.travelapp.dao.CollectionDao
import com.example.travelapp.dao.DayDao
import com.example.travelapp.dao.SavedImageDao
import com.example.travelapp.dao.TripDao
import com.example.travelapp.dao.UserDao
import com.example.travelapp.models.Attraction
import com.example.travelapp.models.Collection
import com.example.travelapp.models.Day
import com.example.travelapp.models.SavedImage
import com.example.travelapp.models.Trip
import com.example.travelapp.models.User
import com.example.travelapp.utils.DateConverter

@Database(
    entities = [Trip::class, Day::class, Attraction::class, Collection::class, SavedImage::class, User::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tripDao(): TripDao
    abstract fun dayDao(): DayDao
    abstract fun attractionDao(): AttractionDao
    abstract fun collectionDao(): CollectionDao
    abstract fun savedImageDao(): SavedImageDao
    abstract fun userDao(): UserDao
}
