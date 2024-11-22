package com.id.destinasyik.data.remote.response

import com.google.gson.annotations.SerializedName

data class PlaceResponse(

	@field:SerializedName("preferredCategory")
	val preferredCategory: String? = null,

	@field:SerializedName("reccomByContent")
	val reccomByContent: List<ReccomPlace?>? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class ReccomPlace(

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
	val gambar: Any? = null,

	@field:SerializedName("rating_count")
	val ratingCount: Int? = null,

	@field:SerializedName("longitude")
	val longitude: Int? = null
)
