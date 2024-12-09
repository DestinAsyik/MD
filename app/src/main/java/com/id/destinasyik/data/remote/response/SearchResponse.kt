package com.id.destinasyik.data.remote.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class SearchResponse(

	@field:SerializedName("data")
	val data: PageData? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

@Parcelize
data class ResultsItem(

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
	val ratingAvg: Float? = null,

	@field:SerializedName("latitude")
	val latitude: Double? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("category")
	val category: String? = null,

	@field:SerializedName("gambar")
	val gambar: String? = null,

	@field:SerializedName("rating_count")
	val ratingCount: Int? = null,

	@field:SerializedName("longitude")
	val longitude: Double? = null
):Parcelable


data class PageData(
	@field:SerializedName("totalItems")
	val totalItems: Int? = null,

	@field:SerializedName("totalPages")
	val totalPages: Int? = null,

	@field:SerializedName("currentPage")
	val currentPage: Int? = null,

	@field:SerializedName("results")
	val results: List<ReccomPlace?>? = null
)

