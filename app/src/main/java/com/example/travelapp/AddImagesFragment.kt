package com.example.travelapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.travelapp.models.Collection
import com.example.travelapp.models.SavedImage
import com.example.travelapp.repository.TravelRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AddImagesFragment : Fragment(R.layout.fragment_add_images) {

    private lateinit var collection: Collection
    private lateinit var btnBack: ImageView
    private lateinit var btnCancel: Button
    private lateinit var btnSave: Button
    private lateinit var ivUploadIcon: ImageView

    private val selectedImageUris = mutableListOf<String>()

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) {
        if (it != null) {
            selectedImageUris.clear()
            selectedImageUris.addAll(it.map { uri -> uri.toString() })
            Toast.makeText(requireContext(), "${it.size} images selected", Toast.LENGTH_SHORT).show()
            // TODO: Update UI to show selected images
        } else {
            Toast.makeText(requireContext(), "No images selected", Toast.LENGTH_SHORT).show()
        }
    }

    private lateinit var repository: TravelRepository

    companion object {
        private const val ARG_COLLECTION_ID = "collection_id"

        fun newInstance(collectionId: String): AddImagesFragment {
            return AddImagesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_COLLECTION_ID, collectionId)
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

        val collectionId = arguments?.getString(ARG_COLLECTION_ID) ?: return

        lifecycleScope.launch {
            repository.getCollectionById(collectionId).collectLatest {
                collection = it
                // Initialize views
                initializeViews(view)

                // Set up click listeners
                setupClickListeners()
            }
        }
    }

    private fun initializeViews(view: View) {
        btnBack = view.findViewById(R.id.btn_back)
        btnCancel = view.findViewById(R.id.btn_cancel)
        btnSave = view.findViewById(R.id.btn_save)
        ivUploadIcon = view.findViewById(R.id.iv_upload_icon)
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
            saveImages()
        }

        // Upload images
        ivUploadIcon.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun saveImages() {
        lifecycleScope.launch {
            selectedImageUris.forEach { uri ->
                val savedImage = SavedImage(
                    collectionId = collection.id,
                    imagePath = uri,
                    name = uri.substringAfterLast("/") // Simple name from URI
                )
                repository.insertImage(savedImage)
            }
            Toast.makeText(requireContext(), "${selectedImageUris.size} images added to collection successfully!", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
        }
    }
}
