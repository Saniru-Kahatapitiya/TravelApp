package com.example.travelapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.travelapp.adapters.DestinationAdapter
import com.example.travelapp.models.Destination

class SearchFragment : Fragment(R.layout.activity_search_fragment) {

    private lateinit var rvDestinations: RecyclerView
    private lateinit var destinationAdapter: DestinationAdapter

    // Reusable navigation function for details
    private fun navigateToDetail(
        name: String,
        images: Array<Int>, // still pass Array<Int>
        description: String
    ) {
        parentFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_container,
                DestinationDetailFragment.newInstance(name, images.toIntArray(), description)
            )
            .addToBackStack(null)
            .commit()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvDestinations = view.findViewById(R.id.rv_destinations)
        rvDestinations.layoutManager = GridLayoutManager(requireContext(), 2)
        destinationAdapter = DestinationAdapter { destination ->
            navigateToDetail(destination.name, destination.images.toTypedArray(), destination.description)
        }
        rvDestinations.adapter = destinationAdapter

        // Prepare destination data
        val destinations = listOf(
            Destination(
                "Nine Arch Bridge",
                intArrayOf(R.drawable.ninearch_1, R.drawable.ninearch_2, R.drawable.ninearch_3),
                "The bridge was designed to accommodate a challenging nine-degree curve and steep gradient. Built entirely by local labor under British supervision, the construction faced significant logistical challenges, including difficult terrain and material transport. Completed in 1919, the bridge has since stood resilient, showcasing innovative engineering solutions such as concrete cornice blocks for arch support and locally produced sand-cement blocks for facing."
            ),
            Destination(
                "Hikkaduwa",
                intArrayOf(R.drawable.hikkaduwa_1, R.drawable.hikkaduwa_2, R.drawable.hikkaduwa_3, R.drawable.hikkaduwa_4),
                "Hikkaduwa, a vibrant coastal town on the southwestern shores of Sri Lanka, offers a mix of sun-soaked beaches, water sports, and nightlife. Attractions include the Coral Sanctuary, shipwrecks like SS Conch, vibrant markets, cafes, and beachfront parties."
            ),
            Destination(
                "Kandy",
                intArrayOf(R.drawable.kandy_1, R.drawable.kandy_2, R.drawable.kandy_3, R.drawable.kandy_4),
                "Kandy served as the last capital of the ancient Sinhalese kingdom. Its major attraction is the Temple of the Sacred Tooth Relic (Sri Dalada Maligawa), an architectural masterpiece and pilgrimage site."
            ),
            Destination(
                "Colombo",
                intArrayOf(R.drawable.colombo_1, R.drawable.colombo_2, R.drawable.colombo_3, R.drawable.colombo_4),
                "Colombo has a long history as a trading port and a colonial city influenced by Portuguese, Dutch, and British rule. Today, modern high-rises sit alongside colonial architecture."
            ),
            Destination(
                "Sigiriya",
                intArrayOf(R.drawable.sigiriya_1, R.drawable.sigiriya_2, R.drawable.sigiriya_3, R.drawable.sigiriya_4),
                "Sigiriya\'s history stretches back to prehistoric times. The site flourished under King Kashyapa I and is famous for its rock fortress and ancient frescoes."
            ),
            Destination(
                "Jaffna",
                intArrayOf(R.drawable.jaffna_1, R.drawable.jaffna_2, R.drawable.jaffna_3, R.drawable.jaffna_4),
                "Jaffna\'s culture is deeply rooted in Tamil traditions. The city has a rich history, recovering from civil war to become a thriving cultural hub."
            ),
            Destination(
                "Polonnaruwa",
                intArrayOf(R.drawable.polonnaruwa_1, R.drawable.polonnaruwa_2, R.drawable.polonnaruwa_3, R.drawable.polonnaruwa_4),
                "Polonnaruwa, Sri Lanka\'s second ancient capital, is a UNESCO World Heritage Site. It was prominent during King Parakramabahu I\'s reign in the 12th century."
            ),
            Destination(
                "Nuwara Eliya",
                intArrayOf(R.drawable.nuwaraeliya_1, R.drawable.nuwaraeliya_2, R.drawable.nuwaraeliya_3, R.drawable.nuwaraeliya_4),
                "Nuwara Eliya, known as 'Little England,' is a hill country city famous for tea production, cool climate, and colonial-era architecture like the Grand Hotel and Hill Club."
            )
        )
        destinationAdapter.submitList(destinations)

        // "Add Destination" button -> Calendar page
        view.findViewById<Button>(R.id.btn_add_destination)?.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CalendarFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}
