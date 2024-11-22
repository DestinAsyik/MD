package com.id.destinasyik.data.remote.response

import com.google.gson.annotations.SerializedName

data class BookmarkResponse(

	@field:SerializedName("newBookmark")
	val newBookmark: NewBookmark? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class NewBookmark(

	@field:SerializedName("bookmark_id")
	val bookmarkId: Int? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("item_id")
	val itemId: Int? = null
)
