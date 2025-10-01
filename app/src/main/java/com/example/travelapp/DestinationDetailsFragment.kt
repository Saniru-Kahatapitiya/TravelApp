package com.example.travelapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment

class DestinationDetailsFragment : Fragment() {

    private lateinit var tvDestinationName: TextView
    private lateinit var tvTravelDateTime: TextView
    private lateinit var tvCategory: TextView
    private lateinit var tvCountry: TextView
    private lateinit var tvDescription: TextView
    private lateinit var tvThingsToDo: TextView
    private lateinit var tvAccommodation: TextView
    private lateinit var tvWhereToEat: TextView
    private lateinit var btnBack: Button
    private lateinit var btnEdit: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_destination_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        tvDestinationName = view.findViewById(R.id.tv_destination_name)
        tvTravelDateTime = view.findViewById(R.id.tv_travel_datetime)
        tvCategory = view.findViewById(R.id.tv_category)
        tvCountry = view.findViewById(R.id.tv_country)
        tvDescription = view.findViewById(R.id.tv_description)
        tvThingsToDo = view.findViewById(R.id.tv_things_to_do)
        tvAccommodation = view.findViewById(R.id.tv_accommodation)
        tvWhereToEat = view.findViewById(R.id.tv_where_to_eat)
        btnBack = view.findViewById(R.id.btn_back)
        btnEdit = view.findViewById(R.id.btn_edit)

        // Get all data from arguments
        val destinationName = arguments?.getString("destination_name") ?: "Unknown Destination"
        val category = arguments?.getString("destination_category") ?: "Not specified"
        val country = arguments?.getString("destination_country") ?: "Not specified"
        val description = arguments?.getString("destination_description") ?: "No description provided"
        val thingsToDo = arguments?.getString("destination_things_to_do") ?: "No activities specified"
        val accommodation = arguments?.getString("destination_accommodation") ?: "No accommodation details"
        val whereToEat = arguments?.getString("destination_where_to_eat") ?: "No dining recommendations"
        val selectedDate = arguments?.getString("selected_date") ?: ""
        val selectedTime = arguments?.getString("selected_time") ?: ""
        val amPm = arguments?.getString("am_pm") ?: ""

        // Display all the information
        tvDestinationName.text = destinationName
        tvCategory.text = category
        tvCountry.text = country
        tvDescription.text = description
        tvThingsToDo.text = if (thingsToDo.isEmpty()) "No activities specified" else thingsToDo
        tvAccommodation.text = if (accommodation.isEmpty()) "No accommodation details" else accommodation
        tvWhereToEat.text = if (whereToEat.isEmpty()) "No dining recommendations" else whereToEat

        // Format and display travel date and time
        if (selectedDate.isNotEmpty() && selectedTime.isNotEmpty() && amPm.isNotEmpty()) {
            val formattedDate = formatDateForDisplay(selectedDate)
            val fullTime = "$selectedTime $amPm"
            tvTravelDateTime.text = "$formattedDate at $fullTime"
        } else {
            tvTravelDateTime.text = "Date and time not specified"
        }

        // Set up click listeners
        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        btnEdit.setOnClickListener {
            showEditOptions()
        }
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

    private fun showEditOptions() {
        val options = arrayOf("Edit Destination", "Delete Destination")
        
        AlertDialog.Builder(requireContext())
            .setTitle("Options")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> editDestination()
                    1 -> showDeleteConfirmation()
                }
            }
            .show()
    }

    private fun editDestination() {
        // Navigate back to destination entry form for editing
        // Pass all current data to pre-fill the form
        val bundle = Bundle().apply {
            putString("destination_name", arguments?.getString("destination_name"))
            putString("destination_category", arguments?.getString("destination_category"))
            putString("destination_country", arguments?.getString("destination_country"))
            putString("destination_description", arguments?.getString("destination_description"))
            putString("destination_things_to_do", arguments?.getString("destination_things_to_do"))
            putString("destination_accommodation", arguments?.getString("destination_accommodation"))
            putString("destination_where_to_eat", arguments?.getString("destination_where_to_eat"))
            putString("selected_date", arguments?.getString("selected_date"))
            putString("selected_time", arguments?.getString("selected_time"))
            putString("am_pm", arguments?.getString("am_pm"))
        }

        val destinationEntryFragment = DestinationEntryFragment()
        destinationEntryFragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, destinationEntryFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun showDeleteConfirmation() {
        val destinationName = arguments?.getString("destination_name") ?: "this destination"
        
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Destination")
            .setMessage("Are you sure you want to delete '$destinationName'?")
            .setPositiveButton("Delete") { _, _ ->
                // Navigate back to destinations list
                parentFragmentManager.popBackStack()
                showMessage("Destination deleted successfully")
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showMessage(message: String) {
        android.widget.Toast.makeText(requireContext(), message, android.widget.Toast.LENGTH_SHORT).show()
    }
}
