package com.id.destinasyik.ui.recomended

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.id.destinasyik.R
import com.id.destinasyik.data.local.mock.Place
import com.id.destinasyik.data.remote.response.ReccomPlace
import com.id.destinasyik.databinding.ItemRecommendedPlaceBinding
import com.id.destinasyik.ui.detail.DetailActivity
import java.text.DecimalFormat
import java.util.Locale

class RecommendedAdapter : ListAdapter<ReccomPlace, RecommendedAdapter.PlaceViewHolder>(DIFF_CALLBACK){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val binding = ItemRecommendedPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val place = getItem(position)
        holder.bind(place)
        holder.itemView.setOnClickListener {
            val moveWithObjectIntent = Intent(holder.itemView.context,DetailActivity::class.java)
            moveWithObjectIntent.putExtra("PLACE", place)
            holder.itemView.context.startActivity(moveWithObjectIntent)
        }
    }

    class PlaceViewHolder(val binding: ItemRecommendedPlaceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(place: ReccomPlace) {
            binding.placeName.text=place.placeName
            binding.placeLocation.text=place.city
            binding.ratingText.text=place.ratingAvg.toString()
            val distance = String.format(Locale.US,"%.2f", place.distanceKm)+" Km"
            val isEmpty = "nu Km"
            if(distance.equals(isEmpty)){
                binding.rangePlace.visibility=View.GONE
                binding.tvCategory.text=place.category
            }else{
                binding.rangePlace.text=distance
                binding.tvCategory.visibility=View.GONE
            }
            Glide.with(binding.placeImage.context)
                .load(place.gambar)
                .into(binding.placeImage)
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