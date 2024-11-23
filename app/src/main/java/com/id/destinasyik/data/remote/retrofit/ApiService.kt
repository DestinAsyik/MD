package com.id.destinasyik.data.remote.retrofit

import com.google.gson.JsonObject
import com.id.destinasyik.data.remote.response.BookmarkResponse
import com.id.destinasyik.data.remote.response.DeleteResponse
import com.id.destinasyik.data.remote.response.GetBookmarkResponse
import com.id.destinasyik.data.remote.response.LoginResponse
import com.id.destinasyik.data.remote.response.LogoutResponse
import com.id.destinasyik.data.remote.response.ProfileResponse
import com.id.destinasyik.data.remote.response.RecommByCategoryResponse
import com.id.destinasyik.data.remote.response.RecommByNearbyResponse
import com.id.destinasyik.data.remote.response.RegisterResponse
import com.id.destinasyik.data.remote.response.UpdateProfile
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

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
    @POST("reccomendation/content")
    fun recomCategory(
        @Header("Authorization") authToken: String,
    ): Call<RecommByCategoryResponse>

    @POST("reccomendation/nearby")
    fun recomNearby(
        @Header("Authorization") authToken: String,
        @Body jsonObject: JsonObject
    ): Call<RecommByNearbyResponse>

    //Bookmark Section
    @POST("bookmark/add")
    fun addBookmark(
        @Header("Authorization") authToken: String,
        @Body jsonObject: JsonObject
    ): Call<BookmarkResponse>

    @GET("bookmark/add")
    fun getBookmark(
        @Header("Authorization") authToken: String,
    ): Call<GetBookmarkResponse>

    @DELETE("bookmark/delete/{id}")
    fun deleteBookmark(
        @Header("Authorization") authToken: String,
        @Path("id") id: Int
    ): Call<DeleteResponse>


}