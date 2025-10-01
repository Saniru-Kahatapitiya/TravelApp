package com.example.travelapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DestinationsListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DestinationsAdapter
    private lateinit var btnBack: ImageView
    private lateinit var btnAddDestination: Button
    private lateinit var tvSelectedDate: TextView
    private var destinations = mutableListOf<DestinationItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_destinations_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        recyclerView = view.findViewById(R.id.recycler_view_destinations)
        btnBack = view.findViewById(R.id.btn_back)
        btnAddDestination = view.findViewById(R.id.btn_add_destination)
        tvSelectedDate = view.findViewById(R.id.tv_selected_date)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Get calendar details from arguments
        val selectedDate = arguments?.getString("selected_date")
        val selectedTime = arguments?.getString("selected_time")
        val amPm = arguments?.getString("am_pm")
        
        // Get destination details from entry form
        val destinationName = arguments?.getString("destination_name")
        val destinationCategory = arguments?.getString("destination_category")
        val destinationCountry = arguments?.getString("destination_country")
        val destinationDescription = arguments?.getString("destination_description")
        val destinationThingsToDo = arguments?.getString("destination_things_to_do")
        val destinationAccommodation = arguments?.getString("destination_accommodation")
        val destinationWhereToEat = arguments?.getString("destination_where_to_eat")

        // Display the calendar details
        if (selectedDate != null && selectedTime != null && amPm != null) {
            val formattedDate = formatDateForDisplay(selectedDate)
            val fullTime = "$selectedTime $amPm"
            tvSelectedDate.text = "$formattedDate at $fullTime"
            
            // Add the user's destination to the list if provided
            if (!destinationName.isNullOrEmpty()) {
                val destinationInfo = "$destinationName ($destinationCategory, $destinationCountry)"
                val newDestination = DestinationItem(
                    name = destinationInfo,
                    date = selectedDate,
                    isCompleted = false,
                    // Store all the detailed information for later use
                    fullName = destinationName,
                    category = destinationCategory ?: "",
                    country = destinationCountry ?: "",
                    description = destinationDescription ?: "",
                    thingsToDo = destinationThingsToDo ?: "",
                    accommodation = destinationAccommodation ?: "",
                    whereToEat = destinationWhereToEat ?: "",
                    selectedTime = selectedTime,
                    amPm = amPm
                )
                destinations.add(0, newDestination)
            }
        }

        // Set up click listeners
        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        btnAddDestination.setOnClickListener {
            // Navigate to search fragment to add new destination
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SearchFragment())
                .addToBackStack(null)
                .commit()
        }

        // Initialize destinations list (only if no calendar details provided)
        if (destinations.isEmpty()) {
            destinations.addAll(listOf(
                DestinationItem("Sigiriya", "5/27/15", false),
                DestinationItem("Hikkaduwa", "5/28/15", false),
                DestinationItem("Ella", "5/29/15", true),
                DestinationItem("Matara", "5/30/15", false),
                DestinationItem("Kandy", "5/31/15", true),
                DestinationItem("Galle", "6/1/15", false),
                DestinationItem("Jaffna", "6/2/15", false)
            ))
        }

        adapter = DestinationsAdapter(destinations) { destination ->
            // Navigate to individual destination details when clicked
            val bundle = Bundle().apply {
                putString("destination_name", destination.fullName.ifEmpty { destination.name })
                putString("destination_category", destination.category)
                putString("destination_country", destination.country)
                putString("destination_description", destination.description)
                putString("destination_things_to_do", destination.thingsToDo)
                putString("destination_accommodation", destination.accommodation)
                putString("destination_where_to_eat", destination.whereToEat)
                putString("selected_date", destination.date)
                putString("selected_time", destination.selectedTime)
                putString("am_pm", destination.amPm)
            }

            val destinationDetailsFragment = DestinationDetailsFragment()
            destinationDetailsFragment.arguments = bundle

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, destinationDetailsFragment)
                .addToBackStack(null)
                .commit()
        }

        // Set up edit and delete callbacks
        adapter.onEditClick = { destination, position ->
            showEditDialog(destination, position)
        }

        adapter.onDeleteClick = { destination, position ->
            showDeleteConfirmation(destination, position)
        }

        recyclerView.adapter = adapter
    }

    private fun showEditDialog(destination: DestinationItem, position: Int) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_destination, null)
        val etName = dialogView.findViewById<EditText>(R.id.et_destination_name)
        val tvDate = dialogView.findViewById<TextView>(R.id.tv_selected_datetime)
        
        etName.setText(destination.name)
        tvDate.setText(destination.date)

        AlertDialog.Builder(requireContext())
            .setTitle("Edit Destination")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val newName = etName.text.toString().trim()
                val newDate = tvDate.text.toString().trim()
                
                if (newName.isNotEmpty() && newDate.isNotEmpty()) {
                    destinations[position] = destination.copy(name = newName, date = newDate)
                    adapter.notifyItemChanged(position)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showDeleteConfirmation(destination: DestinationItem, position: Int) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Destination")
            .setMessage("Are you sure you want to delete '${destination.name}'?")
            .setPositiveButton("Delete") { _, _ ->
                destinations.removeAt(position)
                adapter.notifyItemRemoved(position)
                adapter.notifyItemRangeChanged(position, destinations.size)
            }
            .setNegativeButton("Cancel", null)
            .show()
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

    data class DestinationItem(
        val name: String,
        val date: String,
        val isCompleted: Boolean,
        val fullName: String = "",
        val category: String = "",
        val country: String = "",
        val description: String = "",
        val thingsToDo: String = "",
        val accommodation: String = "",
        val whereToEat: String = "",
        val selectedTime: String = "",
        val amPm: String = ""
    )
}
