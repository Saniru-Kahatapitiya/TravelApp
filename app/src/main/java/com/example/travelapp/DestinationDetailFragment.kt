package com.example.travelapp

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.LinearLayout
import android.widget.Button
import androidx.fragment.app.Fragment

class DestinationDetailFragment : Fragment(R.layout.fragment_destination_detail_new) {

    private lateinit var tabAbout: TextView
    private lateinit var tabReview: TextView
    private lateinit var tabPhoto: TextView
    private lateinit var aboutContent: LinearLayout
    private lateinit var reviewContent: LinearLayout
    private lateinit var photoContent: LinearLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get arguments
        val args = requireArguments()
        val name = args.getString(ARG_NAME) ?: ""
        val images = args.getIntArray(ARG_IMAGES) ?: intArrayOf()
        val description = args.getString(ARG_DESC) ?: ""

        // Initialize views
        val tvName = view.findViewById<TextView>(R.id.tv_destination_name)
        val tvLocation = view.findViewById<TextView>(R.id.tv_location)
        val tvRating = view.findViewById<TextView>(R.id.tv_rating_number)
        val tvDescription = view.findViewById<TextView>(R.id.tv_description)
        val headerImage = view.findViewById<ImageView>(R.id.header_image)
        val btnBack = view.findViewById<ImageView>(R.id.btn_back)
        val btnAddToTrip = view.findViewById<Button>(R.id.btn_add_to_trip)

        // Tab views
        tabAbout = view.findViewById(R.id.tab_about)
        tabReview = view.findViewById(R.id.tab_review)
        tabPhoto = view.findViewById(R.id.tab_photo)
        aboutContent = view.findViewById(R.id.about_content)
        reviewContent = view.findViewById(R.id.review_content)
        photoContent = view.findViewById(R.id.photo_content)

        // Set content
        tvName.text = name
        tvLocation.text = "Sri Lanka"
        tvRating.text = "4.8"
        tvDescription.text = description
        
        // Set header image (use first image from the array)
        if (images.isNotEmpty()) {
            headerImage.setImageResource(images[0])
        }

        // Setup tab functionality
        setupTabs()

        // Back button
        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // Add to trip button
        btnAddToTrip.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CreateTripFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun setupTabs() {
        // Set initial state
        showTab(aboutContent)
        updateTabStyles(tabAbout, true)

        // Tab click listeners
        tabAbout.setOnClickListener {
            showTab(aboutContent)
            updateTabStyles(tabAbout, true)
            updateTabStyles(tabReview, false)
            updateTabStyles(tabPhoto, false)
        }

        tabReview.setOnClickListener {
            showTab(reviewContent)
            updateTabStyles(tabReview, true)
            updateTabStyles(tabAbout, false)
            updateTabStyles(tabPhoto, false)
        }

        tabPhoto.setOnClickListener {
            showTab(photoContent)
            updateTabStyles(tabPhoto, true)
            updateTabStyles(tabAbout, false)
            updateTabStyles(tabReview, false)
        }
    }

    private fun showTab(content: LinearLayout) {
        aboutContent.visibility = View.GONE
        reviewContent.visibility = View.GONE
        photoContent.visibility = View.GONE
        content.visibility = View.VISIBLE
    }

    private fun updateTabStyles(tab: TextView, isSelected: Boolean) {
        if (isSelected) {
            tab.setTextColor(resources.getColor(R.color.teal_700, null))
            tab.background = resources.getDrawable(R.drawable.tab_underline, null)
        } else {
            tab.setTextColor(resources.getColor(android.R.color.black, null))
            tab.background = null
        }
    }

    companion object {
        private const val ARG_NAME = "name"
        private const val ARG_IMAGES = "images"
        private const val ARG_DESC = "description"

        fun newInstance(name: String, images: IntArray, description: String): DestinationDetailFragment {
            return DestinationDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_NAME, name)
                    putIntArray(ARG_IMAGES, images)
                    putString(ARG_DESC, description)
                }
            }
        }
    }
}
