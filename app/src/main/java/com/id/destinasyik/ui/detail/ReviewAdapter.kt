package com.id.destinasyik.ui.detail

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.id.destinasyik.data.remote.response.BookmarksItem
import com.id.destinasyik.data.remote.response.GetReviewResponseItem
import com.id.destinasyik.databinding.ItemLikedPlaceBinding
import com.id.destinasyik.databinding.ItemReviewBinding
import com.id.destinasyik.ui.bookmark.BookmarkAdapter

class ReviewAdapter: ListAdapter<GetReviewResponseItem, ReviewAdapter.MyViewHolder>(DIFF_CALLBACK) {

    class MyViewHolder(val binding: ItemReviewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(review: GetReviewResponseItem) {
            binding.tvUsername.text=review.user?.username
            binding.tvRating.rating= review.rating!!
            binding.tvReview.text=review.review
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val review = getItem(position)
        holder.bind(review)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<GetReviewResponseItem>() {
            override fun areItemsTheSame(oldItem: GetReviewResponseItem, newItem: GetReviewResponseItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: GetReviewResponseItem, newItem: GetReviewResponseItem): Boolean {
                return oldItem == newItem
            }
        }
    }

}