package com.example.travelapp.repository

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
import kotlinx.coroutines.flow.Flow

class TravelRepository(
    private val tripDao: TripDao,
    private val dayDao: DayDao,
    private val attractionDao: AttractionDao,
    private val collectionDao: CollectionDao,
    private val savedImageDao: SavedImageDao,
    private val userDao: UserDao
) {

    // User operations
    suspend fun insertUser(user: User) = userDao.insertUser(user)
    suspend fun getUserByEmail(email: String) = userDao.getUserByEmail(email)
    suspend fun getUserByCredentials(email: String, password: String) = userDao.getUserByCredentials(email, password)


    // Trip operations
    fun getAllTrips(): Flow<List<Trip>> = tripDao.getAllTrips()
    fun getTripById(tripId: String): Flow<Trip> = tripDao.getTripById(tripId)
    suspend fun insertTrip(trip: Trip) = tripDao.insertTrip(trip)
    suspend fun updateTrip(trip: Trip) = tripDao.updateTrip(trip)
    suspend fun deleteTrip(tripId: String) = tripDao.deleteTrip(tripId)

    // Day operations
    fun getDaysForTrip(tripId: String): Flow<List<Day>> = dayDao.getDaysForTrip(tripId)
    fun getDayById(dayId: String): Flow<Day> = dayDao.getDayById(dayId)
    suspend fun insertDay(day: Day) = dayDao.insertDay(day)
    suspend fun updateDay(day: Day) = dayDao.updateDay(day)
    suspend fun deleteDay(dayId: String) = dayDao.deleteDay(dayId)

    // Attraction operations
    fun getAttractionsForDay(dayId: String): Flow<List<Attraction>> = attractionDao.getAttractionsForDay(dayId)
    fun getAttractionById(attractionId: String): Flow<Attraction> = attractionDao.getAttractionById(attractionId)
    suspend fun insertAttraction(attraction: Attraction) = attractionDao.insertAttraction(attraction)
    suspend fun updateAttraction(attraction: Attraction) = attractionDao.updateAttraction(attraction)
    suspend fun deleteAttraction(attractionId: String) = attractionDao.deleteAttraction(attractionId)

    // Collection operations
    fun getAllCollections(): Flow<List<Collection>> = collectionDao.getAllCollections()
    fun getCollectionById(collectionId: String): Flow<Collection> = collectionDao.getCollectionById(collectionId)
    suspend fun insertCollection(collection: Collection) = collectionDao.insertCollection(collection)
    suspend fun updateCollection(collection: Collection) = collectionDao.updateCollection(collection)
    suspend fun deleteCollection(collectionId: String) = collectionDao.deleteCollection(collectionId)

    // SavedImage operations
    fun getImagesForCollection(collectionId: String): Flow<List<SavedImage>> = savedImageDao.getImagesForCollection(collectionId)
    fun getImageById(imageId: String): Flow<SavedImage> = savedImageDao.getImageById(imageId)
    suspend fun insertImage(image: SavedImage) = savedImageDao.insertImage(image)
    suspend fun updateImage(image: SavedImage) = savedImageDao.updateImage(image)
    suspend fun deleteImage(imageId: String) = savedImageDao.deleteImage(imageId)
}
