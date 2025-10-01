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
import com.example.travelapp.models.Trip
import com.example.travelapp.repository.TravelRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CreateTripFragment : Fragment(R.layout.fragment_create_trip) {

    private lateinit var etTripName: EditText
    private lateinit var etStartDate: EditText
    private lateinit var etEndDate: EditText
    private lateinit var ivUploadIcon: ImageView
    private lateinit var btnCancel: Button
    private lateinit var btnNext: Button

    private val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    private var selectedStartDate: Date? = null
    private var selectedEndDate: Date? = null

    private lateinit var repository: TravelRepository

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
        etTripName = view.findViewById(R.id.et_trip_name)
        etStartDate = view.findViewById(R.id.et_start_date)
        etEndDate = view.findViewById(R.id.et_end_date)
        ivUploadIcon = view.findViewById(R.id.iv_upload_icon)
        btnCancel = view.findViewById(R.id.btn_cancel)
        btnNext = view.findViewById(R.id.btn_next)

        // Set up click listeners
        setupClickListeners()
    }

    private fun setupClickListeners() {
        // Upload cover image
        ivUploadIcon.setOnClickListener {
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

        // Cancel button
        btnCancel.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // Next button
        btnNext.setOnClickListener {
            if (validateInputs()) {
                val tripName = etTripName.text.toString().trim()
                val newTrip = Trip(
                    name = tripName,
                    startDate = selectedStartDate,
                    endDate = selectedEndDate,
                    isCompleted = selectedEndDate?.before(Date()) ?: false
                )

                lifecycleScope.launch {
                    repository.insertTrip(newTrip)
                    Toast.makeText(requireContext(), "Trip created successfully!", Toast.LENGTH_SHORT).show()
                    parentFragmentManager.popBackStack()
                }
            }
        }
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
}
