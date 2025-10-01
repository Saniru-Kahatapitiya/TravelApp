package com.example.travelapp

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.travelapp.repository.TravelRepository
import com.example.travelapp.models.Collection
import com.example.travelapp.models.SavedImage
import com.example.travelapp.adapters.SavedImageAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CollectionDetailsFragment : Fragment(R.layout.fragment_collection_details) {

    private lateinit var collection: Collection
    private lateinit var savedImages: List<SavedImage>
    private lateinit var btnBack: ImageView
    private lateinit var tvCollectionName: TextView
    private lateinit var ivAddImages: ImageView
    private lateinit var btnAddMoreImages: Button
    private lateinit var rvSavedImages: RecyclerView // Changed to RecyclerView

    private lateinit var repository: TravelRepository
    private lateinit var savedImageAdapter: SavedImageAdapter

    companion object {
        private const val ARG_COLLECTION_ID = "collection_id"

        fun newInstance(collectionId: String): CollectionDetailsFragment {
            return CollectionDetailsFragment().apply {
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

        // Initialize views
        btnBack = view.findViewById(R.id.btn_back)
        tvCollectionName = view.findViewById(R.id.tv_collection_name)
        ivAddImages = view.findViewById(R.id.iv_add_images)
        btnAddMoreImages = view.findViewById(R.id.btn_add_more_images)
        rvSavedImages = view.findViewById(R.id.rv_saved_images) // Initialize RecyclerView

        // Setup RecyclerView
        rvSavedImages.layoutManager = GridLayoutManager(requireContext(), 2) // 2 columns
        savedImageAdapter = SavedImageAdapter { image ->
            // Handle image click, e.g., view image in full screen or download
            Toast.makeText(requireContext(), "Clicked on image: ${image.name}", Toast.LENGTH_SHORT).show()
            downloadImage(image) // Reuse existing download logic
        }
        rvSavedImages.adapter = savedImageAdapter

        // Set up click listeners
        setupClickListeners()

        lifecycleScope.launch {
            repository.getCollectionById(collectionId).collectLatest {
                collection = it
                tvCollectionName.text = collection.name // Populate collection name here

                // Fetch and populate images
                repository.getImagesForCollection(collection.id).collectLatest {
                    savedImages = it
                    savedImageAdapter.submitList(savedImages)
                }
            }
        }
    }

    private fun initializeViews(view: View) {
        btnBack = view.findViewById(R.id.btn_back)
        tvCollectionName = view.findViewById(R.id.tv_collection_name)
        ivAddImages = view.findViewById(R.id.iv_add_images)
        btnAddMoreImages = view.findViewById(R.id.btn_add_more_images)
    }

    private fun setupClickListeners() {
        // Back button
        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // Add images button
        ivAddImages.setOnClickListener {
            navigateToAddImages()
        }

        // Add more images button
        btnAddMoreImages.setOnClickListener {
            navigateToAddImages()
        }

        // Image click listeners
        setupImageClickListeners()
    }

    private fun setupImageClickListeners() {
        // No longer needed as clicks are handled by the adapter
    }

    private fun setupStarClickListeners() {
        // No longer needed as clicks are handled by the adapter (TODO: integrate star toggle in adapter)
    }

    private fun downloadImage(image: SavedImage?) {
        if (image != null) {
            // TODO: Implement actual download functionality
            showDownloadSuccessDialog()
        }
    }

    private fun toggleStar(imageIndex: Int) {
        // This logic will be moved to the adapter or handled by a separate event in the future
        if (imageIndex < savedImages.size) {
            Toast.makeText(requireContext(), "Star toggled for image ${imageIndex + 1}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToAddImages() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, AddImagesFragment.newInstance(collection.id))
            .addToBackStack(null)
            .commit()
    }

    private fun showDownloadSuccessDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Downloaded")
            .setMessage("Your image is downloaded.")
            .setIcon(android.R.drawable.ic_dialog_info)
            .setPositiveButton("Ok") { _, _ ->
                // Dialog dismissed
            }
            .show()
    }
}
