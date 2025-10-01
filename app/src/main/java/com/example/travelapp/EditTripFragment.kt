package com.example.travelapp

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
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

class EditTripFragment : Fragment(R.layout.fragment_edit_trip) {

    private lateinit var trip: Trip
    private lateinit var btnBack: ImageView
    private lateinit var btnSave: Button
    private lateinit var ivTripCover: ImageView
    private lateinit var etTripName: EditText
    private lateinit var etStartDate: EditText
    private lateinit var etEndDate: EditText

    private val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    private var selectedStartDate: Date? = null
    private var selectedEndDate: Date? = null

    private lateinit var repository: TravelRepository

    companion object {
        private const val ARG_TRIP_ID = "trip_id"

        fun newInstance(tripId: String): EditTripFragment {
            return EditTripFragment().apply {
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

                // Populate existing data
                populateExistingData()
            }
        }
    }

    private fun initializeViews(view: View) {
        btnBack = view.findViewById(R.id.btn_back)
        btnSave = view.findViewById(R.id.btn_save)
        ivTripCover = view.findViewById(R.id.iv_trip_cover)
        etTripName = view.findViewById(R.id.et_trip_name)
        etStartDate = view.findViewById(R.id.et_start_date)
        etEndDate = view.findViewById(R.id.et_end_date)
    }

    private fun setupClickListeners() {
        // Back button
        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // Save button
        btnSave.setOnClickListener {
            if (validateInputs()) {
                saveTripChanges()
            }
        }

        // Cover image click
        ivTripCover.setOnClickListener {
            // TODO: Implement image picker
            Toast.makeText(requireContext(), "Image picker will be implemented", Toast.LENGTH_SHORT).show()
        }

        // Start date picker
        etStartDate.setOnClickListener {
            showDatePicker { date ->
                selectedStartDate = date
                etStartDate.text = android.text.Editable.Factory.getInstance().newEditable(dateFormat.format(date))
            }
        }

        // End date picker
        etEndDate.setOnClickListener {
            showDatePicker { date ->
                selectedEndDate = date
                etEndDate.text = android.text.Editable.Factory.getInstance().newEditable(dateFormat.format(date))
            }
        }
    }

    private fun populateExistingData() {
        // Set existing trip data
        etTripName.setText(trip.name)
        
        trip.startDate?.let { 
            selectedStartDate = it
            etStartDate.text = android.text.Editable.Factory.getInstance().newEditable(dateFormat.format(it))
        }
        
        trip.endDate?.let { 
            selectedEndDate = it
            etEndDate.text = android.text.Editable.Factory.getInstance().newEditable(dateFormat.format(it))
        }

        // Set cover image
        ivTripCover.setImageResource(R.drawable.ninearch)
    }

    private fun showDatePicker(onDateSelected: (Date) -> Unit) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth)
                }.time
                onDateSelected(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun validateInputs(): Boolean {
        val tripName = etTripName.text.toString().trim()
        
        if (tripName.isEmpty()) {
            etTripName.error = "Trip name is required"
            return false
        }
        
        if (selectedStartDate == null) {
            etStartDate.error = "Start date is required"
            return false
        }
        
        if (selectedEndDate == null) {
            etEndDate.error = "End date is required"
            return false
        }
        
        if (selectedStartDate!!.after(selectedEndDate)) {
            etEndDate.error = "End date must be after start date"
            return false
        }
        
        return true
    }

    private fun saveTripChanges() {
        // Update trip with new data
        val updatedTrip = trip.copy(
            name = etTripName.text.toString().trim(),
            startDate = selectedStartDate,
            endDate = selectedEndDate,
            isCompleted = selectedEndDate?.before(Date()) ?: false
        )

        lifecycleScope.launch {
            repository.updateTrip(updatedTrip)
            Toast.makeText(requireContext(), "Trip updated successfully!", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
        }
    }
}
