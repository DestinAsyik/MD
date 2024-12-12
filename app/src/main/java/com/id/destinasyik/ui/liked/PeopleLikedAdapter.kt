package com.id.destinasyik.ui.liked

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.id.destinasyik.R
import com.id.destinasyik.data.local.mock.LikedPlace
import com.id.destinasyik.data.remote.response.ReccomPlace
import com.id.destinasyik.databinding.ItemLikedPlaceBinding
import com.id.destinasyik.databinding.ItemRecommendedPlaceBinding
import com.id.destinasyik.ui.detail.DetailActivity
import com.id.destinasyik.ui.recomended.RecommendedAdapter.PlaceViewHolder

class PeopleLikedAdapter() : ListAdapter<ReccomPlace,PeopleLikedAdapter.LikedPlaceViewHolder>(DIFF_CALLBACK) {

    class LikedPlaceViewHolder(val binding: ItemLikedPlaceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(place: ReccomPlace) {
            binding.placeName.text=place.placeName
            binding.placeLocation.text=place.city
            binding.ratingText.text=place.ratingAvg.toString()
            Glide.with(binding.placeImage.context)
                .load(place.gambar)
                .into(binding.placeImage)
            binding.tvCategory.text=place.category
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikedPlaceViewHolder {
        val binding = ItemLikedPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LikedPlaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LikedPlaceViewHolder, position: Int) {
        val place = getItem(position)
        holder.bind(place)
        holder.itemView.setOnClickListener {
            val moveWithObjectIntent = Intent(holder.itemView.context, DetailActivity::class.java)
            moveWithObjectIntent.putExtra("PLACE", place)
            holder.itemView.context.startActivity(moveWithObjectIntent)
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