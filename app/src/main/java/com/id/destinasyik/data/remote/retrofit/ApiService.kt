package com.id.destinasyik.data.remote.retrofit

import com.google.gson.JsonObject
import com.id.destinasyik.data.remote.response.LoginResponse
import com.id.destinasyik.data.remote.response.ProfileResponse
import com.id.destinasyik.data.remote.response.RegisterResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("auth/login")
    fun loginUser(
        @Body jsonObject: JsonObject
    ): Call<LoginResponse>

    @POST("auth/register")
    fun registerUser(
        @Body jsonObject: JsonObject
    ): Call<RegisterResponse>

    @POST("/profile")
    fun getProfile(
        @Header("Authorization") authToken: String
    ): Call<ProfileResponse>
}