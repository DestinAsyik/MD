package com.id.destinasyik.data.remote.response

import com.google.gson.annotations.SerializedName

data class AddReviewResponse(

	@field:SerializedName("item_id")
	val itemId: Int? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("review")
	val review: String? = null,

	@field:SerializedName("rating")
	val rating: Any? = null
)
