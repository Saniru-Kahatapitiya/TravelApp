package com.example.travelapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.travelapp.R
import com.example.travelapp.models.Collection

class CollectionAdapter(private val onCollectionClick: (Collection) -> Unit) : RecyclerView.Adapter<CollectionAdapter.CollectionViewHolder>() {

    private var collections: List<Collection> = emptyList()

    fun submitList(newCollections: List<Collection>) {
        collections = newCollections
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_collection_card, parent, false)
        return CollectionViewHolder(view)
    }

    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {
        val collection = collections[position]
        holder.bind(collection)
    }

    override fun getItemCount(): Int = collections.size

    inner class CollectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivCollectionImage: ImageView = itemView.findViewById(R.id.iv_collection_image)
        private val tvCollectionName: TextView = itemView.findViewById(R.id.tv_collection_name)

        fun bind(collection: Collection) {
            tvCollectionName.text = collection.name
            // Set collection image (placeholder for now)
            ivCollectionImage.setImageResource(R.drawable.waterfall) // Replace with actual image loading based on collection.coverImage

            itemView.setOnClickListener { onCollectionClick(collection) }
        }
    }
}
