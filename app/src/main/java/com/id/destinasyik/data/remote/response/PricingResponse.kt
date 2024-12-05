package com.id.destinasyik.data.remote.response

import com.google.gson.annotations.SerializedName

data class PricingResponse(

	@field:SerializedName("distance")
	val distance: String? = null,

	@field:SerializedName("ticketPrice")
	val ticketPrice: String? = null,

	@field:SerializedName("fuelDetails")
	val fuelDetails: List<FuelDetailsItem?>? = null
)

data class FuelDetailsItem(

	@field:SerializedName("fuelNeeded")
	val fuelNeeded: String? = null,

	@field:SerializedName("fuelType")
	val fuelType: String? = null,

	@field:SerializedName("totalCost")
	val totalCost: String? = null,

	@field:SerializedName("fuelCost")
	val fuelCost: String? = null
)
