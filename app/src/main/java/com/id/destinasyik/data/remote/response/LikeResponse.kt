package com.id.destinasyik.data.remote.response

import com.google.gson.annotations.SerializedName

data class LikeResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Data(
	@field:SerializedName("isLiked")
	val isLiked: Boolean? = null
)
