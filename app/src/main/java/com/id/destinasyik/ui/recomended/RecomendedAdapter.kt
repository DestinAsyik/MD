package com.id.destinasyik.ui.recomended

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.id.destinasyik.R
import com.id.destinasyik.data.mock.Place

class RecommendedAdapter : ListAdapter<Place, RecommendedAdapter.PlaceViewHolder>(PlaceDiffCallback()) {

    private var onItemClickListener: ((Place) -> Unit)? = null
    private var onFavoriteClickListener: ((Place) -> Unit)? = null

    fun setOnItemClickListener(listener: (Place) -> Unit) {
        onItemClickListener = listener
    }

    fun setOnBookmarkClickListener(listener: (Place) -> Unit) {
        onFavoriteClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recommended_place, parent, false)
        return PlaceViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val place = getItem(position)
        holder.bind(place)
    }

    inner class PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardView: MaterialCardView = itemView.findViewById(R.id.cardView)
        private val imageView: ImageView = itemView.findViewById(R.id.placeImage)
        private val nameText: TextView = itemView.findViewById(R.id.placeName)
        private val locationText: TextView = itemView.findViewById(R.id.placeLocation)
        private val priceText: TextView = itemView.findViewById(R.id.placePrice)
        private val durationText: TextView = itemView.findViewById(R.id.placeDuration)
        private val favoriteButton: ImageView = itemView.findViewById(R.id.favoriteButton)
        private val ratingText: TextView = itemView.findViewById(R.id.ratingText)

        fun bind(place: Place) {
            // Set basic info
            nameText.text = place.name
            locationText.text = place.location
            priceText.text = place.toDisplayPrice()
            durationText.text = place.getFormattedDuration()
            ratingText.text = String.format("%.1f", place.rating)

            imageView.setImageResource(place.imageResource)

            cardView.setOnClickListener {
                onItemClickListener?.invoke(place)
            }

            favoriteButton.setOnClickListener {
                onFavoriteClickListener?.invoke(place)
                it.animate()
                    .scaleX(0.8f)
                    .scaleY(0.8f)
                    .setDuration(100)
                    .withEndAction {
                        it.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(100)
                            .start()
                    }
                    .start()
            }

            itemView.apply {
                alpha = 0f
                translationY = 50f
                animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setDuration(300)
                    .setStartDelay(position * 50L)
                    .start()
            }
        }
    }

    class PlaceDiffCallback : DiffUtil.ItemCallback<Place>() {
        override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean {
            return oldItem == newItem
        }
    }
}