package com.example.travelapp

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.travelapp.repository.TravelRepository
import com.example.travelapp.models.Trip
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class TripDetailsViewFragment : Fragment(R.layout.fragment_trip_details_view) {

    private lateinit var trip: Trip
    private lateinit var btnBack: ImageView
    private lateinit var btnEdit: ImageView
    private lateinit var btnDelete: ImageView
    private lateinit var ivTripCover: ImageView
    private lateinit var tvTripName: TextView
    private lateinit var tvTripDates: TextView
    private lateinit var tvTripStatus: TextView
    private lateinit var llDaysContainer: LinearLayout

    private lateinit var repository: TravelRepository

    companion object {
        private const val ARG_TRIP_ID = "trip_id"

        fun newInstance(tripId: String): TripDetailsViewFragment {
            return TripDetailsViewFragment().apply {
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

        lifecycleScope.launch {
            repository.getTripById(tripId).collectLatest {
                trip = it
                // Initialize views
                initializeViews(view)

                // Set up click listeners
                setupClickListeners()

                // Populate trip data and load days/attractions
                populateTripData()
                loadDaysAndAttractions(trip.id)
            }
        }
    }

    private fun initializeViews(view: View) {
        btnBack = view.findViewById(R.id.btn_back)
        btnEdit = view.findViewById(R.id.btn_edit)
        btnDelete = view.findViewById(R.id.btn_delete)
        ivTripCover = view.findViewById(R.id.iv_trip_cover)
        tvTripName = view.findViewById(R.id.tv_trip_name)
        tvTripDates = view.findViewById(R.id.tv_trip_dates)
        tvTripStatus = view.findViewById(R.id.tv_trip_status)
        llDaysContainer = view.findViewById(R.id.ll_days_container)
    }

    private fun setupClickListeners() {
        // Back button
        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // Edit button
        btnEdit.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, EditTripFragment.newInstance(trip.id))
                .addToBackStack(null)
                .commit()
        }

        // Delete button
        btnDelete.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }

    private fun populateTripData() {
        // Set trip basic info
        tvTripName.text = trip.name

        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val startDate = trip.startDate?.let { dateFormat.format(it) } ?: "TBD"
        val endDate = trip.endDate?.let { dateFormat.format(it) } ?: "TBD"
        tvTripDates.text = "$startDate - $endDate"

        tvTripStatus.text = if (trip.isCompleted) "Completed" else "Ongoing"

        // Set cover image (you can customize this based on trip data)
        ivTripCover.setImageResource(R.drawable.ninearch)
    }

    private suspend fun loadDaysAndAttractions(tripId: String) {
        repository.getDaysForTrip(tripId).collectLatest { days ->
            llDaysContainer.removeAllViews()
            days.forEach { day ->
                val dayView = createDayView(day)
                llDaysContainer.addView(dayView)
            }
        }
    }

    private fun createDayView(day: com.example.travelapp.models.Day): View {
        val dayView = LayoutInflater.from(requireContext())
            .inflate(R.layout.item_day_view, llDaysContainer, false)

        val tvDayTitle = dayView.findViewById<TextView>(R.id.tv_day_title)
        val llAttractionsContainer = dayView.findViewById<LinearLayout>(R.id.ll_attractions_container)

        // Set day title
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        tvDayTitle.text = "Day ${dateFormat.format(day.date)}"

        // Add attractions
        lifecycleScope.launch {
            repository.getAttractionsForDay(day.id).collectLatest { attractions ->
                attractions.forEach { attraction ->
                    val attractionView = createAttractionView(attraction)
                    llAttractionsContainer.addView(attractionView)
                }
            }
        }

        return dayView
    }

    private fun createAttractionView(attraction: com.example.travelapp.models.Attraction): View {
        val attractionView = LayoutInflater.from(requireContext())
            .inflate(R.layout.item_attraction_view, llDaysContainer, false)

        val ivAttractionImage = attractionView.findViewById<ImageView>(R.id.iv_attraction_image)
        val tvAttractionName = attractionView.findViewById<TextView>(R.id.tv_attraction_name)
        val tvAttractionDetails = attractionView.findViewById<TextView>(R.id.tv_attraction_details)
        val ivAttractionInfo = attractionView.findViewById<ImageView>(R.id.iv_attraction_info)

        // Set attraction data
        tvAttractionName.text = attraction.name
        tvAttractionDetails.text = "${attraction.location} • ${attraction.time}"

        // Set image
        val imageResource = when (attraction.image) {
            "ninearch" -> R.drawable.ninearch
            "flying_ravana" -> R.drawable.flying_ravana
            "ella_rock" -> R.drawable.ella_rock
            "little_adams_peak" -> R.drawable.little_adams_peak
            else -> R.drawable.ninearch
        }
        ivAttractionImage.setImageResource(imageResource)

        // Info button click
        ivAttractionInfo.setOnClickListener {
            // Navigate to attraction detail
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

        return attractionView
    }

    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Trip")
            .setMessage("Are you sure to delete the trip?")
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton("Yes") { _, _ ->
                deleteTrip()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun deleteTrip() {
        lifecycleScope.launch {
            repository.deleteTrip(trip.id)
            Toast.makeText(requireContext(), "Trip deleted successfully!", Toast.LENGTH_SHORT).show()
            showDeleteSuccessDialog()
        }
    }

    private fun showDeleteSuccessDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Deleted successfully")
            .setMessage("Your trip is deleted successfully.")
            .setIcon(android.R.drawable.ic_dialog_info)
            .setPositiveButton("Ok") { _, _ ->
                parentFragmentManager.popBackStack()
            }
            .show()
    }
}
