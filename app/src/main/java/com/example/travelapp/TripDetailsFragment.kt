package com.example.travelapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.travelapp.models.Trip
import com.example.travelapp.models.Day
import com.example.travelapp.models.Attraction
import com.example.travelapp.repository.TravelRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class TripDetailsFragment : Fragment(R.layout.fragment_trip_details) {

    private lateinit var trip: Trip
    private lateinit var llDaysContainer: LinearLayout
    private lateinit var btnSaveTrip: Button
    private lateinit var ivAddDayGlobal: ImageView // Reference for the global add day button

    private lateinit var repository: TravelRepository

    companion object {
        private const val ARG_TRIP_ID = "trip_id"

        fun newInstance(tripId: String): TripDetailsFragment {
            return TripDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TRIP_ID, tripId)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = Room.databaseBuilder(
            requireContext(),
            AppDatabase::class.java,
            "travel-database"
        ).build()
        repository = TravelRepository(db.tripDao(), db.dayDao(), db.attractionDao(), db.collectionDao(), db.savedImageDao(), db.userDao())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tripId = arguments?.getString(ARG_TRIP_ID) ?: return

        // Initialize views
        llDaysContainer = view.findViewById(R.id.ll_days_container)
        btnSaveTrip = view.findViewById(R.id.btn_save_trip)
        ivAddDayGlobal = view.findViewById(R.id.iv_add_day) // Assuming this is the global add day button

        // Set up click listeners
        setupClickListeners()

        lifecycleScope.launch {
            repository.getTripById(tripId).collectLatest {
                trip = it
                loadDaysForTrip(trip.id)
            }
        }
    }

    private fun setupClickListeners() {
        btnSaveTrip.setOnClickListener {
            parentFragmentManager.popBackStack() // Just navigate back after changes are saved to DB
        }

        ivAddDayGlobal.setOnClickListener { // Listener for the global add day button
            createNewDay(trip.id)
        }
    }

    private suspend fun loadDaysForTrip(tripId: String) {
        repository.getDaysForTrip(tripId).collectLatest { days ->
            llDaysContainer.removeAllViews() // Clear existing views
            if (days.isEmpty()) {
                createNewDay(tripId)
            } else {
                days.forEach { day ->
                    addDayViewToContainer(day)
                }
            }
        }
    }

    private fun addDayViewToContainer(day: Day) {
        val dayView = LayoutInflater.from(requireContext())
            .inflate(R.layout.item_day_planning, llDaysContainer, false)

        val tvDayTitle = dayView.findViewById<TextView>(R.id.tv_day_title)
        val llAttractionsContainer = dayView.findViewById<LinearLayout>(R.id.ll_attractions_container)

        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        tvDayTitle.text = "Day ${dateFormat.format(day.date)}"

        val addAttractionCard = dayView.findViewById<View>(R.id.cv_add_attraction)
        addAttractionCard.setOnClickListener {
            showAttractionSelectionDialog(day.id)
        }

        lifecycleScope.launch {
            repository.getAttractionsForDay(day.id).collectLatest { attractions ->
                llAttractionsContainer.removeAllViews() // Clear existing attractions for this day
                attractions.forEach { attraction ->
                    addAttractionToDay(llAttractionsContainer, attraction)
                }
            }
        }

        llDaysContainer.addView(dayView)
    }

    private fun createNewDay(tripId: String) {
        lifecycleScope.launch {
            val currentDate = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_MONTH, llDaysContainer.childCount)
            }.time
            val newDay = Day(tripId = tripId, date = currentDate)
            repository.insertDay(newDay)
            // No need to manually increment dayCounter here, as loadDaysForTrip will re-fetch
        }
    }

    private fun showAttractionSelectionDialog(dayId: String) {
        val attractions = listOf(
            Attraction(dayId = dayId, name = "Nine Arch Bridge", location = "Ella", image = "ninearch"),
            Attraction(dayId = dayId, name = "Flying Ravana", location = "Ella", image = "flying_ravana"),
            Attraction(dayId = dayId, name = "Ella Rock", location = "Ella", image = "ella_rock"),
            Attraction(dayId = dayId, name = "Little Adam's Peak", location = "Ella", image = "little_adams_peak")
        )

        val attraction = attractions.random()

        lifecycleScope.launch {
            repository.insertAttraction(attraction)
            // The addAttractionToDay will be called via the flow collection in addDayViewToContainer
        }
    }

    private fun addAttractionToDay(container: LinearLayout, attraction: Attraction) {
        val attractionView = LayoutInflater.from(requireContext())
            .inflate(R.layout.item_attraction, container, false)

        val tvAttractionName = attractionView.findViewById<TextView>(R.id.tv_attraction_name)
        val tvAttractionLocation = attractionView.findViewById<TextView>(R.id.tv_attraction_location)
        val ivAttractionImage = attractionView.findViewById<ImageView>(R.id.iv_attraction_image)
        val ivAttractionInfo = attractionView.findViewById<ImageView>(R.id.iv_attraction_info)

        tvAttractionName.text = attraction.name
        tvAttractionLocation.text = attraction.location

        val imageResource = when (attraction.image) {
            "ninearch" -> R.drawable.ninearch
            "flying_ravana" -> R.drawable.flying_ravana
            "ella_rock" -> R.drawable.ella_rock
            "little_adams_peak" -> R.drawable.little_adams_peak
            else -> R.drawable.ninearch
        }
        ivAttractionImage.setImageResource(imageResource)

        ivAttractionInfo.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container,
                    DestinationDetailFragment.newInstance(
                        attraction.name,
                        intArrayOf(imageResource),
                        attraction.description
                    ))
                .addToBackStack(null)
                .commit()
        }

        container.addView(attractionView)
    }

}
