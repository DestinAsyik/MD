package com.id.destinasyik.ui.liked

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.id.destinasyik.R
import com.id.destinasyik.data.local.mock.LikedPlace

class PeopleLikedAdapter(private val maxItems: Int) : RecyclerView.Adapter<PeopleLikedAdapter.LikedPlaceViewHolder>() {
    private var places = listOf<LikedPlace>()
    private var onJoinClickListener: ((LikedPlace) -> Unit)? = null

    class LikedPlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameText: TextView = itemView.findViewById(R.id.placeName)
        val locationText: TextView = itemView.findViewById(R.id.placeLocation)
        val joinedButton: Button = itemView.findViewById(R.id.joinedButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikedPlaceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_liked_place, parent, false)
        return LikedPlaceViewHolder(view)
    }

    override fun onBindViewHolder(holder: LikedPlaceViewHolder, position: Int) {
        val place = places[position]
        holder.apply {
            nameText.text = place.name
            locationText.text = place.location
            joinedButton.text = if (place.isJoined) "Joined" else "Join"
            joinedButton.isSelected = place.isJoined
            joinedButton.setOnClickListener {
                onJoinClickListener?.invoke(place)
            }
        }
    }

    override fun getItemCount() = minOf(places.size, maxItems)

    fun submitList(newPlaces: List<LikedPlace>) {
        places = newPlaces
        notifyDataSetChanged()
    }
}