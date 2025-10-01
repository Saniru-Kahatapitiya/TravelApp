package com.example.travelapp

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.travelapp.adapters.TripAdapter
import com.example.travelapp.repository.TravelRepository
import com.example.travelapp.models.Trip
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class TripManagementFragment : Fragment(R.layout.fragment_trip_management) {

    private lateinit var tabOngoing: TextView
    private lateinit var tabPast: TextView
    private lateinit var ivAddTrip: ImageView
    private lateinit var rvTripManagement: RecyclerView // Changed to RecyclerView

    private var isShowingOngoing = true
    private lateinit var repository: TravelRepository
    private var allTrips: List<Trip> = emptyList()
    private lateinit var tripAdapter: TripAdapter

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

        // Initialize views
        tabOngoing = view.findViewById(R.id.tab_ongoing)
        tabPast = view.findViewById(R.id.tab_past)
        ivAddTrip = view.findViewById(R.id.iv_add_trip)
        rvTripManagement = view.findViewById(R.id.rv_trip_management) // Initialize RecyclerView

        // Setup RecyclerView
        rvTripManagement.layoutManager = LinearLayoutManager(requireContext())
        tripAdapter = TripAdapter(onTripClick = { trip ->
            // Handle trip click, e.g., navigate to TripDetailsViewFragment
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, TripDetailsViewFragment.newInstance(trip.id))
                .addToBackStack(null)
                .commit()
        })
        rvTripManagement.adapter = tripAdapter

        // Set up click listeners
        setupClickListeners()

        // Observe trips from the database
        lifecycleScope.launch {
            repository.getAllTrips().collectLatest {
                allTrips = it
                loadTrips()
            }
        }

        // Load trips initially
        updateTabStyles()
    }

    private fun setupClickListeners() {
        // Tab navigation
        tabOngoing.setOnClickListener {
            isShowingOngoing = true
            updateTabStyles()
            loadTrips()
        }

        tabPast.setOnClickListener {
            isShowingOngoing = false
            updateTabStyles()
            loadTrips()
        }

        // Add new trip
        ivAddTrip.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CreateTripFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun updateTabStyles() {
        if (isShowingOngoing) {
            tabOngoing.setTextColor(resources.getColor(R.color.teal_700, null))
            tabOngoing.background = resources.getDrawable(R.drawable.tab_underline, null)
            tabPast.setTextColor(resources.getColor(android.R.color.black, null))
            tabPast.background = null
        } else {
            tabPast.setTextColor(resources.getColor(R.color.teal_700, null))
            tabPast.background = resources.getDrawable(R.drawable.tab_underline, null)
            tabOngoing.setTextColor(resources.getColor(android.R.color.black, null))
            tabOngoing.background = null
        }
    }

    private fun loadTrips() {
        // llTripsContainer.removeAllViews() // No longer needed with RecyclerView

        val tripsToShow = if (isShowingOngoing) {
            allTrips.filter { !it.isCompleted }
        } else {
            allTrips.filter { it.isCompleted }
        }

        tripAdapter.submitList(tripsToShow)
    }

    // private fun createTripView(trip: Trip): View { /* Removed as Adapter handles this */ }

    private fun createSampleTrips(): List<Trip> {
        // This method is no longer needed as trips are loaded from the database
        return emptyList()
    }

    private fun showDeleteConfirmationDialog(tripId: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Trip")
            .setMessage("Are you sure to delete the trip?")
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton("Yes") { _, _ ->
                deleteTrip(tripId)
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun deleteTrip(tripId: String) {
        lifecycleScope.launch {
            repository.deleteTrip(tripId)
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
                // Refresh the trips list
                loadTrips()
            }
            .show()
    }
}
