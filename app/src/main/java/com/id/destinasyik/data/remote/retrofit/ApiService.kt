package com.id.destinasyik.data.remote.retrofit

import com.google.gson.JsonObject
import com.id.destinasyik.data.remote.response.AddBookmarkResponse
import com.id.destinasyik.data.remote.response.GetBookmarkResponse
import com.id.destinasyik.data.remote.response.LoginResponse
import com.id.destinasyik.data.remote.response.LogoutResponse
import com.id.destinasyik.data.remote.response.ProfileResponse
import com.id.destinasyik.data.remote.response.RecommByCategoryResponse
import com.id.destinasyik.data.remote.response.RecommByNearbyResponse
import com.id.destinasyik.data.remote.response.RecommByPeopleLiked
import com.id.destinasyik.data.remote.response.RegisterResponse
import com.id.destinasyik.data.remote.response.UpdateProfile
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    //User Section
    @POST("auth/login")
    fun loginUser(
        @Body jsonObject: JsonObject
    ): Call<LoginResponse>

    @POST("auth/register")
    fun registerUser(
        @Body jsonObject: JsonObject
    ): Call<RegisterResponse>

    @GET("profile")
    fun getProfile(
        @Header("Authorization") authToken: String
    ): Call<ProfileResponse>

    @POST("auth/logout")
    fun logOut(
        @Header("Authorization") authToken: String
    ): Call<LogoutResponse>

    @POST("profile/update")
    fun updateProfile(
        @Header("Authorization") authToken: String,
        @Body jsonObject: JsonObject
    ): Call<UpdateProfile>

    //Recom Section
    @POST("recommendation/content")
    fun recomCategory(
        @Header("Authorization") authToken: String,
    ): Call<RecommByCategoryResponse>

    @POST("recommendation/nearby")
    fun recomNearby(
        @Header("Authorization") authToken: String,
        @Body jsonObject: JsonObject
    ): Call<RecommByNearbyResponse>

    @POST("recommendation/colaborative")
    fun recomPeopleLiked(
        @Header("Authorization") authToken: String,
    ): Call<RecommByPeopleLiked>

    //Bookmark Section
    @POST("bookmarks/toggle-bookmark")
    fun addBookmark(
        @Header("Authorization") authToken: String,
        @Body jsonObject: JsonObject
    ): Call<AddBookmarkResponse>

    @GET("bookmarks/get")
    fun getBookmark(
        @Header("Authorization") authToken: String,
    ): Call<GetBookmarkResponse>

    //Like Toggle
    @POST("likes/toggle-like")
    fun addLike(
        @Header("Authorization") authToken: String,
        @Body jsonObject: JsonObject
    ): Call<AddBookmarkResponse>
}