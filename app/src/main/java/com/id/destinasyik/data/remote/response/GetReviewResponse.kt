package com.id.destinasyik.data.remote.response

import com.google.gson.annotations.SerializedName

data class GetReviewResponse(

	@field:SerializedName("GetReviewResponse")
	val getReviewResponse: List<GetReviewResponseItem?>? = null
)

data class Username(

	@field:SerializedName("username")
	val username: String? = null
)

data class GetReviewResponseItem(

	@field:SerializedName("review_id")
	val reviewId: Int? = null,

	@field:SerializedName("User")
	val user: Username? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("item_id")
	val itemId: Int? = null,

	@field:SerializedName("review")
	val review: String? = null,

	@field:SerializedName("rating")
	val rating: Float? = null
)
