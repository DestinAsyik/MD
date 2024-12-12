package com.id.destinasyik.data.remote.response

import com.google.gson.annotations.SerializedName

data class TokenError(
	@field:SerializedName("message")
	val message: String? = null
)
