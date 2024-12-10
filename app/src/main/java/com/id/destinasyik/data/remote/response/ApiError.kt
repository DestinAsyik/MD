package com.id.destinasyik.data.remote.response

import com.google.gson.annotations.SerializedName

data class ApiError(
    @SerializedName("error") val error: String
)