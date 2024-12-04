package com.id.destinasyik.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class BookmarksItem(

	@field:SerializedName("bookmark_id")
	val bookmarkId: Int? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("item_id")
	val itemId: Int? = null,

	@field:SerializedName("destination")
	val destination: Destination? = null
)

@Parcelize
data class Destination(

	@field:SerializedName("place_name")
	val placeName: String? = null,

	@field:SerializedName("coordinate")
	val coordinate: String? = null,

	@field:SerializedName("item_id")
	val itemId: Int? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("price")
	val price: Int? = null,

	@field:SerializedName("rating_avg")
	val ratingAvg: Int? = null,

	@field:SerializedName("latitude")
	val latitude: Int? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("category")
	val category: String? = null,

	@field:SerializedName("gambar")
	val gambar: String? = null,

	@field:SerializedName("rating_count")
	val ratingCount: Int? = null,

	@field:SerializedName("longitude")
	val longitude: Int? = null
):Parcelable

data class GetBookmarkResponse(

	@field:SerializedName("bookmarks")
	val bookmarks: List<BookmarksItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null
)
