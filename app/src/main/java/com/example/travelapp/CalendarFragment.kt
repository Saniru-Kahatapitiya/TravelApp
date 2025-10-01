package com.example.travelapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class CalendarFragment : Fragment(R.layout.fragment_calendar) {

    private var selectedDate: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val calendarView = view.findViewById<CalendarView>(R.id.calendarView)
        val tvSelectedDate = view.findViewById<TextView>(R.id.tv_selected_date)
        val etTime = view.findViewById<EditText>(R.id.et_time)
        val spinnerAmPm = view.findViewById<Spinner>(R.id.spinner_am_pm)
        val btnGo = view.findViewById<Button>(R.id.btn_go)

        // Listen to date changes
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedDate = "$dayOfMonth/${month + 1}/$year"
            tvSelectedDate.text = "Selected Date: $selectedDate"
        }

        // Go button click
        btnGo.setOnClickListener {
            val time = etTime.text.toString().trim()
            val amPm = spinnerAmPm.selectedItem?.toString() ?: "AM"

            if (selectedDate.isNullOrEmpty()) {
                showAlert("Error", "Please select a date")
                return@setOnClickListener
            }

            if (time.isEmpty()) {
                showAlert("Error", "Please enter a time")
                return@setOnClickListener
            }

            // Create bundle with calendar details
            val bundle = Bundle().apply {
                putString("selected_date", selectedDate)
                putString("selected_time", time)
                putString("am_pm", amPm)
            }

            // Navigate to destination entry form
            val destinationEntryFragment = DestinationEntryFragment()
            destinationEntryFragment.arguments = bundle
            
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, destinationEntryFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun showAlert(title: String, message: String) {
        val dialog = AlertDialog.Builder(requireContext()).create()

        // Title
        val titleView = TextView(requireContext())
        titleView.text = title
        titleView.setPadding(40, 40, 40, 0)
        titleView.textSize = 20f
        titleView.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_green_dark))

        // Message
        val messageView = TextView(requireContext())
        messageView.text = message
        messageView.setPadding(40, 20, 40, 40)
        messageView.textSize = 16f
        messageView.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_blue_dark))

        dialog.setCustomTitle(titleView)
        dialog.setView(messageView)

        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK") { d, _ ->
            d.dismiss()

            // Navigate to HomeFragment (or any destinations list fragment)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment()) // replace with your fragment ID
                .addToBackStack(null)
                .commit()
        }

        dialog.show()
    }


}
