package com.id.destinasyik.ui.bookmark

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.id.destinasyik.data.remote.response.BookmarksItem
import com.id.destinasyik.data.remote.response.ReccomPlace
import com.id.destinasyik.databinding.ItemLikedPlaceBinding
import com.id.destinasyik.ui.detail.DetailActivity
import com.id.destinasyik.ui.liked.PeopleLikedAdapter

class BookmarkAdapter() : ListAdapter<BookmarksItem, BookmarkAdapter.MyViewHolder>(DIFF_CALLBACK) {

    class MyViewHolder(val binding: ItemLikedPlaceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(place: BookmarksItem) {
            Glide.with(binding.placeImage.context)
                .load(place.destination?.gambar)
                .into(binding.placeImage)
            binding.placeName.text=place.destination?.placeName
            binding.placeLocation.text=place.destination?.city
            binding.ratingText.text=place.destination?.ratingAvg.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemLikedPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val place = getItem(position)
        holder.bind(place)
        holder.itemView.setOnClickListener {
            val moveWithObjectIntent = Intent(holder.itemView.context, DetailActivity::class.java)
            moveWithObjectIntent.putExtra("PLACE", place.destination)
            holder.itemView.context.startActivity(moveWithObjectIntent)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BookmarksItem>() {
            override fun areItemsTheSame(oldItem: BookmarksItem, newItem: BookmarksItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: BookmarksItem, newItem: BookmarksItem): Boolean {
                return oldItem == newItem
            }
        }
    }

}