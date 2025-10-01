package com.example.travelapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.travelapp.models.Collection
import com.example.travelapp.repository.TravelRepository
import kotlinx.coroutines.launch

class CreateCollectionFragment : Fragment(R.layout.fragment_create_collection) {

    private lateinit var btnBack: ImageView
    private lateinit var btnCancel: Button
    private lateinit var btnSave: Button
    private lateinit var ivUploadIcon: ImageView
    private lateinit var etCollectionName: EditText

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
        initializeViews(view)

        // Set up click listeners
        setupClickListeners()
    }

    private fun initializeViews(view: View) {
        btnBack = view.findViewById(R.id.btn_back)
        btnCancel = view.findViewById(R.id.btn_cancel)
        btnSave = view.findViewById(R.id.btn_save)
        ivUploadIcon = view.findViewById(R.id.iv_upload_icon)
        etCollectionName = view.findViewById(R.id.et_collection_name)
    }

    private fun setupClickListeners() {
        // Back button
        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // Cancel button
        btnCancel.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // Save button
        btnSave.setOnClickListener {
            if (validateInputs()) {
                createCollection()
            }
        }

        // Upload cover image
        ivUploadIcon.setOnClickListener {
            // TODO: Implement image picker
            Toast.makeText(requireContext(), "Image picker will be implemented", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateInputs(): Boolean {
        val collectionName = etCollectionName.text.toString().trim()
        
        if (collectionName.isEmpty()) {
            etCollectionName.error = "Collection name is required"
            return false
        }
        
        return true
    }

    private fun createCollection() {
        val collectionName = etCollectionName.text.toString().trim()
        
        val newCollection = Collection(
            name = collectionName,
            coverImage = "default_collection" // You can set a default image
        )

        lifecycleScope.launch {
            repository.insertCollection(newCollection)
            Toast.makeText(requireContext(), "Collection '$collectionName' created successfully!", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
        }
    }
}
