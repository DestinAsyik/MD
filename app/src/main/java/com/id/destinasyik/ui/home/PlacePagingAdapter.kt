package com.id.destinasyik.ui.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.id.destinasyik.data.remote.response.ReccomPlace
import com.id.destinasyik.data.remote.response.ResultsItem
import com.id.destinasyik.databinding.ItemRecommendedPlaceBinding
import com.id.destinasyik.ui.detail.DetailActivity

class PlacePagingAdapter: PagingDataAdapter<ReccomPlace, PlacePagingAdapter.PlaceViewHolder>(DIFF_CALLBACK){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val binding = ItemRecommendedPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val place = getItem(position)
        place?.let { holder.bind(it) }
        holder.itemView.setOnClickListener {
            val moveWithObjectIntent = Intent(holder.itemView.context, DetailActivity::class.java)
            moveWithObjectIntent.putExtra("PLACE", place)
            holder.itemView.context.startActivity(moveWithObjectIntent)
        }
    }

    class PlaceViewHolder(val binding: ItemRecommendedPlaceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(place: ReccomPlace) {
            binding.placeName.text=place.placeName
            binding.placeLocation.text=place.city
            binding.ratingText.text=place.ratingAvg.toString()
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