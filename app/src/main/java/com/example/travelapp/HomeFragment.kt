package com.example.travelapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.travelapp.adapters.TripAdapter
import com.example.travelapp.repository.TravelRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var repository: TravelRepository
    private lateinit var tripAdapter: TripAdapter
    private lateinit var rvTrips: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = Room.databaseBuilder(
            requireContext(),
            AppDatabase::class.java,
            "travel-database"
        ).build()
        repository = TravelRepository(db.tripDao(), db.dayDao(), db.attractionDao(), db.collectionDao(), db.savedImageDao(), db.userDao())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Find the button
        val addDestinationBtn: Button = view.findViewById(R.id.btn_add_destination)

        // Navigate to CalendarFragment when clicked
        addDestinationBtn.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CalendarFragment()) // 👈 Make sure this matches your container ID in MainActivity
                .addToBackStack(null)
                .commit()
        }

        val btnAddJournal: Button = view.findViewById(R.id.btn_add_journal)
        btnAddJournal.setOnClickListener {
            // Navigate to SearchFragment when plus button is clicked
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SearchFragment())
                .addToBackStack(null)
                .commit()
        }

        // Make the circular preview images clickable and navigate to SearchFragment
        val imgNinearch = view.findViewById<android.widget.ImageView>(R.id.img_ninearch)
        imgNinearch.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SearchFragment())
                .addToBackStack(null)
                .commit()
        }

        val imgHikkaduwa = view.findViewById<android.widget.ImageView>(R.id.img_hikkaduwa)
        imgHikkaduwa.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SearchFragment())
                .addToBackStack(null)
                .commit()
        }

        val imgColombo = view.findViewById<android.widget.ImageView>(R.id.img_colombo)
        imgColombo.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SearchFragment())
                .addToBackStack(null)
                .commit()
        }

        return view
    }


}
