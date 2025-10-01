package com.example.travelapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.travelapp.R
import com.example.travelapp.models.Trip
import java.text.SimpleDateFormat
import java.util.Locale

class TripAdapter(private val onTripClick: (Trip) -> Unit) : RecyclerView.Adapter<TripAdapter.TripViewHolder>() {

    private var trips: List<Trip> = emptyList()

    fun submitList(newTrips: List<Trip>) {
        trips = newTrips
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_trip_card, parent, false)
        return TripViewHolder(view)
    }

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        val trip = trips[position]
        holder.bind(trip)
    }

    override fun getItemCount(): Int = trips.size

    inner class TripViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivTripImage: ImageView = itemView.findViewById(R.id.iv_trip_image)
        private val tvTripName: TextView = itemView.findViewById(R.id.tv_trip_name)
        private val tvTripDates: TextView = itemView.findViewById(R.id.tv_trip_dates)
        private val tvTripTimestamp: TextView = itemView.findViewById(R.id.tv_trip_timestamp)
        private val ivEditTrip: ImageView = itemView.findViewById(R.id.iv_edit_trip)
        private val ivDeleteTrip: ImageView = itemView.findViewById(R.id.iv_delete_trip)

        fun bind(trip: Trip) {
            tvTripName.text = trip.name

            val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            val startDate = trip.startDate?.let { dateFormat.format(it) } ?: "TBD"
            val endDate = trip.endDate?.let { dateFormat.format(it) } ?: "TBD"
            tvTripDates.text = "$startDate - $endDate"

            val timestampFormat = SimpleDateFormat("MMM dd HH:mm", Locale.getDefault())
            tvTripTimestamp.text = timestampFormat.format(trip.createdAt)

            // Set image (you can customize this based on trip data)
            ivTripImage.setImageResource(R.drawable.ninearch) // Placeholder image

            itemView.setOnClickListener { onTripClick(trip) }
            ivEditTrip.setOnClickListener { /* TODO: Handle edit click */ }
            ivDeleteTrip.setOnClickListener { /* TODO: Handle delete click */ }
        }
    }
}
