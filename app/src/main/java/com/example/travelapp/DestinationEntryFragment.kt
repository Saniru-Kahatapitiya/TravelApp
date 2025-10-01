package com.example.travelapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment

class DestinationEntryFragment : Fragment() {

    private lateinit var tvSelectedDateTime: TextView
    private lateinit var etDestinationName: EditText
    private lateinit var etCategory: EditText
    private lateinit var etCountry: EditText
    private lateinit var etDescription: EditText
    private lateinit var etThingsToDo: EditText
    private lateinit var etAccommodation: EditText
    private lateinit var etWhereToEat: EditText
    private lateinit var btnAddImages: Button
    private lateinit var btnCancel: Button
    private lateinit var btnSubmit: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_destination_entry, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        tvSelectedDateTime = view.findViewById(R.id.tv_selected_datetime)
        etDestinationName = view.findViewById(R.id.et_destination_name)
        etCategory = view.findViewById(R.id.et_category)
        etCountry = view.findViewById(R.id.et_country)
        etDescription = view.findViewById(R.id.et_description)
        etThingsToDo = view.findViewById(R.id.et_things_to_do)
        etAccommodation = view.findViewById(R.id.et_accommodation)
        etWhereToEat = view.findViewById(R.id.et_where_to_eat)
        btnAddImages = view.findViewById(R.id.btn_add_images)
        btnCancel = view.findViewById(R.id.btn_cancel)
        btnSubmit = view.findViewById(R.id.btn_submit)

        // Get calendar details from arguments
        val selectedDate = arguments?.getString("selected_date")
        val selectedTime = arguments?.getString("selected_time")
        val amPm = arguments?.getString("am_pm")

        // Display the selected date and time
        if (selectedDate != null && selectedTime != null && amPm != null) {
            val formattedDate = formatDateForDisplay(selectedDate)
            val fullTime = "$selectedTime $amPm"
            tvSelectedDateTime.text = "Selected: $formattedDate at $fullTime"
        }

        // Pre-fill form if editing existing destination
        preFillForm()

        // Set up click listeners
        btnCancel.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        btnAddImages.setOnClickListener {
            // TODO: Implement image selection functionality
            // For now, just show a message
            showMessage("Image selection will be implemented")
        }

        btnSubmit.setOnClickListener {
            if (validateForm()) {
                showSuccessDialog()
            }
        }
    }

    private fun validateForm(): Boolean {
        val name = etDestinationName.text.toString().trim()
        val category = etCategory.text.toString().trim()
        val country = etCountry.text.toString().trim()
        val description = etDescription.text.toString().trim()

        if (name.isEmpty()) {
            etDestinationName.error = "Destination name is required"
            return false
        }

        if (category.isEmpty()) {
            etCategory.error = "Category is required"
            return false
        }

        if (country.isEmpty()) {
            etCountry.error = "Country is required"
            return false
        }

        if (description.isEmpty()) {
            etDescription.error = "Description is required"
            return false
        }

        return true
    }

    private fun showSuccessDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Success!")
            .setMessage("Destination added successfully!")
            .setPositiveButton("OK") { _, _ ->
                navigateToDestinationDetails()
            }
            .setCancelable(false)
            .show()
    }

    private fun navigateToDestinationDetails() {
        // Get all form data
        val destinationData = DestinationData(
            name = etDestinationName.text.toString().trim(),
            category = etCategory.text.toString().trim(),
            country = etCountry.text.toString().trim(),
            description = etDescription.text.toString().trim(),
            thingsToDo = etThingsToDo.text.toString().trim(),
            accommodation = etAccommodation.text.toString().trim(),
            whereToEat = etWhereToEat.text.toString().trim(),
            selectedDate = arguments?.getString("selected_date") ?: "",
            selectedTime = arguments?.getString("selected_time") ?: "",
            amPm = arguments?.getString("am_pm") ?: ""
        )

        // Create bundle with all destination and calendar data
        val bundle = Bundle().apply {
            putString("destination_name", destinationData.name)
            putString("destination_category", destinationData.category)
            putString("destination_country", destinationData.country)
            putString("destination_description", destinationData.description)
            putString("destination_things_to_do", destinationData.thingsToDo)
            putString("destination_accommodation", destinationData.accommodation)
            putString("destination_where_to_eat", destinationData.whereToEat)
            putString("selected_date", destinationData.selectedDate)
            putString("selected_time", destinationData.selectedTime)
            putString("am_pm", destinationData.amPm)
        }

        // Navigate to destinations list with the new destination
        val destinationsFragment = DestinationsListFragment()
        destinationsFragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, destinationsFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun formatDateForDisplay(dateString: String): String {
        // Convert from DD/MM/YYYY to "DD Month YYYY" format
        val parts = dateString.split("/")
        if (parts.size == 3) {
            val day = parts[0]
            val month = parts[1].toInt()
            val year = parts[2]

            val monthNames = arrayOf(
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
            )

            return "$day ${monthNames[month - 1]} $year"
        }
        return dateString
    }

    private fun preFillForm() {
        // Pre-fill form fields if editing existing destination
        etDestinationName.setText(arguments?.getString("destination_name") ?: "")
        etCategory.setText(arguments?.getString("destination_category") ?: "")
        etCountry.setText(arguments?.getString("destination_country") ?: "")
        etDescription.setText(arguments?.getString("destination_description") ?: "")
        etThingsToDo.setText(arguments?.getString("destination_things_to_do") ?: "")
        etAccommodation.setText(arguments?.getString("destination_accommodation") ?: "")
        etWhereToEat.setText(arguments?.getString("destination_where_to_eat") ?: "")
    }

    private fun showMessage(message: String) {
        // Simple message display - you can replace with Toast or Snackbar
        android.widget.Toast.makeText(requireContext(), message, android.widget.Toast.LENGTH_SHORT).show()
    }

    data class DestinationData(
        val name: String,
        val category: String,
        val country: String,
        val description: String,
        val thingsToDo: String,
        val accommodation: String,
        val whereToEat: String,
        val selectedDate: String,
        val selectedTime: String,
        val amPm: String
    )
}
