package com.example.travelapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DestinationsAdapter(
    private val destinations: MutableList<DestinationsListFragment.DestinationItem>,
    private val onItemClick: (DestinationsListFragment.DestinationItem) -> Unit
) : RecyclerView.Adapter<DestinationsAdapter.DestinationViewHolder>() {

    var onEditClick: ((DestinationsListFragment.DestinationItem, Int) -> Unit)? = null
    var onDeleteClick: ((DestinationsListFragment.DestinationItem, Int) -> Unit)? = null

    class DestinationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val statusIcon: ImageView = itemView.findViewById(R.id.iv_status)
        val nameTextView: TextView = itemView.findViewById(R.id.tv_destination_name)
        val dateTextView: TextView = itemView.findViewById(R.id.tv_destination_date)
        val editButton: ImageView = itemView.findViewById(R.id.btn_edit)
        val deleteButton: ImageView = itemView.findViewById(R.id.btn_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DestinationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_destination, parent, false)
        return DestinationViewHolder(view)
    }

    override fun onBindViewHolder(holder: DestinationViewHolder, position: Int) {
        val destination = destinations[position]
        
        // Set status icon based on completion
        if (destination.isCompleted) {
            holder.statusIcon.setImageResource(R.drawable.correct)
        } else {
            holder.statusIcon.setImageResource(R.drawable.ic_launcher_foreground)
        }
        
        holder.nameTextView.text = destination.name
        holder.dateTextView.text = destination.date

        // Set up click listeners
        holder.itemView.setOnClickListener {
            onItemClick(destination)
        }

        holder.editButton.setOnClickListener {
            onEditClick?.invoke(destination, position)
        }

        holder.deleteButton.setOnClickListener {
            onDeleteClick?.invoke(destination, position)
        }
    }

    override fun getItemCount(): Int = destinations.size
}
