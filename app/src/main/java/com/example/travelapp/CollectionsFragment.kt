package com.example.travelapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.travelapp.adapters.CollectionAdapter
import com.example.travelapp.models.Collection
import com.example.travelapp.repository.TravelRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CollectionsFragment : Fragment(R.layout.fragment_collections) {

    private lateinit var btnBack: ImageView
    private lateinit var ivAddCollection: ImageView
    private lateinit var etSearch: EditText
    private lateinit var rvCollections: RecyclerView // Changed to RecyclerView

    private lateinit var repository: TravelRepository
    private var collections: List<Collection> = emptyList()
    private lateinit var collectionAdapter: CollectionAdapter

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
        btnBack = view.findViewById(R.id.btn_back)
        ivAddCollection = view.findViewById(R.id.iv_add_collection)
        etSearch = view.findViewById(R.id.et_search)
        rvCollections = view.findViewById(R.id.rv_collections) // Initialize RecyclerView

        // Setup RecyclerView
        rvCollections.layoutManager = GridLayoutManager(requireContext(), 2) // 2 columns
        collectionAdapter = CollectionAdapter { collection ->
            // Handle collection click, e.g., navigate to CollectionDetailsFragment
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CollectionDetailsFragment.newInstance(collection.id))
                .addToBackStack(null)
                .commit()
        }
        rvCollections.adapter = collectionAdapter

        // Set up click listeners
        setupClickListeners()

        // Load collections from DB
        lifecycleScope.launch {
            repository.getAllCollections().collectLatest {
                collections = it
                collectionAdapter.submitList(collections)
            }
        }
    }

    private fun initializeViews(view: View) {
        btnBack = view.findViewById(R.id.btn_back)
        ivAddCollection = view.findViewById(R.id.iv_add_collection)
        etSearch = view.findViewById(R.id.et_search)
        // glCollections = view.findViewById(R.id.gl_collections) // Removed
    }

    private fun setupClickListeners() {
        // Back button
        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // Add collection button
        ivAddCollection.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CreateCollectionFragment())
                .addToBackStack(null)
                .commit()
        }

        // Search functionality
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                filterCollections(s.toString())
            }
        })

        // Collection click listeners (will be dynamic later)
        setupCollectionClickListeners()
    }

    private fun setupCollectionClickListeners() {
        // No longer needed as clicks are handled by the adapter
        // The individual hardcoded cards will be replaced by RecyclerView items
    }

    private fun displayCollections(collectionsToDisplay: List<Collection>) {
        collectionAdapter.submitList(collectionsToDisplay)
    }

    private fun filterCollections(query: String) {
        val filteredList = collections.filter { it.name.contains(query, ignoreCase = true) }
        displayCollections(filteredList)
    }

    private fun createSampleCollections(): List<Collection> {
        // This method is no longer needed as collections are loaded from the database
        return emptyList()
    }
}
