package com.id.destinasyik.data.remote.retrofit

import com.google.gson.JsonObject
import com.id.destinasyik.data.remote.response.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("auth/login")
    fun login(
        @Body jsonObject: JsonObject
    ): Call<LoginResponse>
}