package com.example.travelapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.travelapp.R
import com.example.travelapp.models.Destination

class DestinationAdapter(private val onDestinationClick: (Destination) -> Unit) : RecyclerView.Adapter<DestinationAdapter.DestinationViewHolder>() {

    private var destinations: List<Destination> = emptyList()

    fun submitList(newDestinations: List<Destination>) {
        destinations = newDestinations
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DestinationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_destination_card, parent, false)
        return DestinationViewHolder(view)
    }

    override fun onBindViewHolder(holder: DestinationViewHolder, position: Int) {
        val destination = destinations[position]
        holder.bind(destination)
    }

    override fun getItemCount(): Int = destinations.size

    inner class DestinationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivDestinationImage: ImageView = itemView.findViewById(R.id.iv_destination_image)
        private val tvDestinationName: TextView = itemView.findViewById(R.id.tv_destination_name)

        fun bind(destination: Destination) {
            tvDestinationName.text = destination.name
            if (destination.images.isNotEmpty()) {
                ivDestinationImage.setImageResource(destination.images[0])
            }
            itemView.setOnClickListener { onDestinationClick(destination) }
        }
    }
}
