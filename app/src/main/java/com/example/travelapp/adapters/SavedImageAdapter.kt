package com.example.travelapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.travelapp.R
import com.example.travelapp.models.SavedImage

class SavedImageAdapter(private val onImageClick: (SavedImage) -> Unit) : RecyclerView.Adapter<SavedImageAdapter.SavedImageViewHolder>() {

    private var images: List<SavedImage> = emptyList()

    fun submitList(newImages: List<SavedImage>) {
        images = newImages
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_saved_image_card, parent, false)
        return SavedImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: SavedImageViewHolder, position: Int) {
        val image = images[position]
        holder.bind(image)
    }

    override fun getItemCount(): Int = images.size

    inner class SavedImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivSavedImage: ImageView = itemView.findViewById(R.id.iv_saved_image)
        private val tvImageName: TextView = itemView.findViewById(R.id.tv_image_name)
        private val ivStar: ImageView = itemView.findViewById(R.id.iv_star)

        fun bind(image: SavedImage) {
            tvImageName.text = image.name
            // Set image (placeholder for now)
            ivSavedImage.setImageResource(R.drawable.ninearch) // Replace with actual image loading
            ivStar.setImageResource(if (image.isStarred) R.drawable.ic_starred else R.drawable.ic_unstarred) // Assuming you have these drawables

            itemView.setOnClickListener { onImageClick(image) }
            ivStar.setOnClickListener { /* TODO: Handle star click */ }
        }
    }
}
