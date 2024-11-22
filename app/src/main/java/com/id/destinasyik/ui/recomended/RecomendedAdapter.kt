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
import com.id.destinasyik.data.local.mock.Place
import com.id.destinasyik.data.remote.response.ReccomPlace
import com.id.destinasyik.databinding.ItemRecommendedPlaceBinding

class RecommendedAdapter : ListAdapter<ReccomPlace, RecommendedAdapter.PlaceViewHolder>(DIFF_CALLBACK){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val binding = ItemRecommendedPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val place = getItem(position)
        holder.bind(place)
    }

    class PlaceViewHolder(val binding: ItemRecommendedPlaceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(place: ReccomPlace) {
            binding.placeName.text=place.placeName
            binding.placeLocation.text=place.city
            binding.ratingText.text=place.ratingAvg.toString()
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ReccomPlace>() {
            override fun areItemsTheSame(oldItem: ReccomPlace, newItem: ReccomPlace): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ReccomPlace, newItem: ReccomPlace): Boolean {
                return oldItem == newItem
            }
        }
    }
}