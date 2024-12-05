package com.id.destinasyik.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.id.destinasyik.data.remote.response.AddBookmarkResponse
import com.id.destinasyik.data.remote.response.BookmarksItem
import com.id.destinasyik.data.remote.response.DeleteResponse
import com.id.destinasyik.data.remote.response.FuelDetailsItem
import com.id.destinasyik.data.remote.response.GetBookmarkResponse
import com.id.destinasyik.data.remote.response.LikeResponse
import com.id.destinasyik.data.remote.response.LoginResponse
import com.id.destinasyik.data.remote.response.LogoutResponse
import com.id.destinasyik.data.remote.response.PricingResponse
import com.id.destinasyik.data.remote.response.ProfileResponse
import com.id.destinasyik.data.remote.response.ReccomPlace
import com.id.destinasyik.data.remote.response.RecommByCategoryResponse
import com.id.destinasyik.data.remote.response.RecommByNearbyResponse
import com.id.destinasyik.data.remote.response.RecommByPeopleLiked
import com.id.destinasyik.data.remote.response.RegisterResponse
import com.id.destinasyik.data.remote.response.UpdateProfile
import com.id.destinasyik.data.remote.response.User
import com.id.destinasyik.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val _login = MutableLiveData<LoginResponse>()
    val login: LiveData<LoginResponse> = _login
    private val _registrationStatus = MutableLiveData<Boolean>()
    val registrationStatus: LiveData<Boolean> get() = _registrationStatus
    private val _profile = MutableLiveData<User?>()
    val profile: LiveData<User?> = _profile
    private val _placeReccomCategory = MutableLiveData<List<ReccomPlace?>?>()
    val placeReccomCategory: LiveData<List<ReccomPlace?>?> = _placeReccomCategory
    private val _placeReccomNearby = MutableLiveData<List<ReccomPlace?>?>()
    val placeReccomNearby: LiveData<List<ReccomPlace?>?> = _placeReccomNearby
    private val _placeRecommPeopleLiked = MutableLiveData<List<ReccomPlace?>?>()
    val placeRecommPeopleLiked: LiveData<List<ReccomPlace?>?> = _placeRecommPeopleLiked
    private val _bookmarkedPlace = MutableLiveData<List<BookmarksItem?>?>()
    val bookmarkedPlace: LiveData<List<BookmarksItem?>?> = _bookmarkedPlace
    private val _loadingEvent = MutableLiveData<Boolean>()
    val loadingEvent: LiveData<Boolean> = _loadingEvent
    private val _bookmarkResponse = MutableLiveData<AddBookmarkResponse>()
    val bookmarkResponse: LiveData<AddBookmarkResponse> = _bookmarkResponse
    private val _statusBookmark = MutableLiveData<AddBookmarkResponse>()
    val statusBookmark: LiveData<AddBookmarkResponse> = _statusBookmark

    private val _likeResponse = MutableLiveData<LikeResponse>()
    val likeResponse: LiveData<LikeResponse> = _likeResponse
    private val _statusLike = MutableLiveData<LikeResponse>()
    val statusLike: LiveData<LikeResponse> = _statusLike

    private val _listCost = MutableLiveData<PricingResponse>()
    val listCost: LiveData<PricingResponse> = _listCost

    fun registerUser (
        username: String,
        name: String,
        password: String,
        age: String,
        email: String,
        city: String,
        preferedCategory: String
    ) {
        val jsonObject = JsonObject().apply {
            addProperty("username", username)
            addProperty("name", name)
            addProperty("password", password)
            addProperty("age", age)
            addProperty("email", email)
            addProperty("city", city)
            addProperty("prefered_category", preferedCategory)
        }
        val client = ApiConfig.getApiService().registerUser(jsonObject)
        client.enqueue(object : Callback<RegisterResponse>{
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful){
                    _registrationStatus.value = true
                    Log.e("Register Auth", "Succesfully Register")
                }else{
                    _registrationStatus.value = false
                    Log.e("Register Auth", "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _registrationStatus.value = false
                Log.e("Login Auth", "onFailure: ${t.message.toString()}")
            }

        })
    }


    fun loginAuth(username: String, password: String){
        val jsonObject = JsonObject().apply {
            addProperty("username", username)
            addProperty("password", password)
        }
        val client = ApiConfig.getApiService().loginUser(jsonObject)
        client.enqueue(object : Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful){
                    _login.value = response.body()
                    Log.e("Login Auth", "Succesfully Login")
                }else{
                    Log.e("Login Auth", "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("Login Auth", "onFailure: ${t.message.toString()}")
            }
        })

    }

    fun getProfile(authToken: String){
        val client = ApiConfig.getApiService().getProfile(authToken)
        client.enqueue(object: Callback<ProfileResponse>{
            override fun onResponse(
                call: Call<ProfileResponse>,
                response: Response<ProfileResponse>
            ) {
                if (response.isSuccessful){
                    _profile.value = response.body()?.user
                    Log.e("User Profile", "Succesfully Fetch Profile")
                }else{
                    Log.e("User Profile", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                Log.e("User Profile", "onFailure: ${t.message.toString()}")
            }

        })
    }

    fun updateProfile(username: String, name: String, age: Int, email: String, city: String, preferedCategory: String, authToken: String){
        val jsonObject = JsonObject().apply {
            addProperty("username", username)
            addProperty("name", name)
            addProperty("age", age)
            addProperty("email", email)
            addProperty("city", city)
            addProperty("prefered_category", preferedCategory)
        }
        val client = ApiConfig.getApiService().updateProfile(authToken, jsonObject)
        client.enqueue(object: Callback<UpdateProfile>{
            override fun onResponse(call: Call<UpdateProfile>, response: Response<UpdateProfile>) {
                if (response.isSuccessful){
                    Log.e("User Profile", "Succesfully Update Profile")
                }else{
                    Log.e("User Profile", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UpdateProfile>, t: Throwable) {
                Log.e("User Profile", "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun logout(authToken: String){
        val client = ApiConfig.getApiService().logOut(authToken)
        client.enqueue(object: Callback<LogoutResponse>{
            override fun onResponse(
                call: Call<LogoutResponse>,
                response: Response<LogoutResponse>
            ) {
                if (response.isSuccessful){
                    Log.e("Logout", "Succesfully Logout")
                }else{
                    Log.e("Logout", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LogoutResponse>, t: Throwable) {
                Log.e("Logout", "onFailure: ${t.message.toString()}")
            }

        })
    }

    //Recommendation Section
    fun reccomCategory(authToken: String){
        _loadingEvent.value=true
        val client = ApiConfig.getApiService().recomCategory(authToken)
        client.enqueue(object: Callback<RecommByCategoryResponse>{
            override fun onResponse(call: Call<RecommByCategoryResponse>, response: Response<RecommByCategoryResponse>) {
                if(response.isSuccessful){
                    _loadingEvent.value=false
                    _placeReccomCategory.value = response.body()?.reccomByContent
                    Log.e("Recom Place", "Succesfully Recom By Category")
                }else{
                    Log.e("Recom Place", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RecommByCategoryResponse>, t: Throwable) {
                Log.e("Recom Place", "onFailure: ${t.message.toString()}")
            }

        })
    }

    fun recommNearby(authToken: String, latitude: Double, longitude: Double){
        _loadingEvent.value=true
        val jsonObject = JsonObject().apply {
            addProperty("latitude", latitude)
            addProperty("longitude", longitude)
        }
        val client = ApiConfig.getApiService().recomNearby(authToken, jsonObject)
        client.enqueue(object: Callback<RecommByNearbyResponse>{
            override fun onResponse(call: Call<RecommByNearbyResponse>, response: Response<RecommByNearbyResponse>) {
                if(response.isSuccessful){
                    _loadingEvent.value=false
                    _placeReccomNearby.value=response.body()?.reccomByJarak
                    Log.e("Recom Place", "Succesfully Recom By Nearby")
                }else{
                    Log.e("Recom Place", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RecommByNearbyResponse>, t: Throwable) {
                Log.e("Recom Place", "onFailure: ${t.message.toString()}")
            }

        })
    }

    fun recommPeopleLiked(authToken: String){
        _loadingEvent.value=true
        val client = ApiConfig.getApiService().recomPeopleLiked(authToken)
        client.enqueue(object: Callback<RecommByPeopleLiked>{
            override fun onResponse(
                call: Call<RecommByPeopleLiked>,
                response: Response<RecommByPeopleLiked>
            ) {
                if(response.isSuccessful){
                    _loadingEvent.value=false
                    _placeRecommPeopleLiked.value=response.body()?.recommPeopleLiked
                    Log.e("Recom Place", "Succesfully Recom By People Liked")
                }else{
                    Log.e("Recom Place", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RecommByPeopleLiked>, t: Throwable) {
                Log.e("Recom Place", "onFailure: ${t.message.toString()}")
            }

        })

    }

    //Bookmark Secction
    fun addBookmark(authToken: String, itemId: Int){
        val jsonObject = JsonObject().apply {
            addProperty("item_id", itemId)
        }
        val client = ApiConfig.getApiService().addBookmark(authToken, jsonObject)
        client.enqueue(object: Callback<AddBookmarkResponse>{
            override fun onResponse(
                call: Call<AddBookmarkResponse>,
                response: Response<AddBookmarkResponse>
            ) {
                if (response.isSuccessful){
                    _bookmarkResponse.value=response.body()
                    _statusBookmark.value=response.body()
                    Log.e("Bookmark", "Succesfully Bookmark Place")
                }else{
                    Log.e("Bookmark", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<AddBookmarkResponse>, t: Throwable) {
                Log.e("Bookmark", "onFailure: ${t.message.toString()}")
            }
        })
    }

    //Bookmark Secction
    fun statusBookmark(authToken: String, itemId: Int){
        val jsonObject = JsonObject().apply {
            addProperty("item_id", itemId)
        }
        val client = ApiConfig.getApiService().addBookmark(authToken, jsonObject)
        client.enqueue(object: Callback<AddBookmarkResponse>{
            override fun onResponse(
                call: Call<AddBookmarkResponse>,
                response: Response<AddBookmarkResponse>
            ) {
                if (response.isSuccessful){
                    addBookmark(authToken, itemId)
                    Log.e("Bookmark", "Succesfully Bookmark Place")
                }else{
                    Log.e("Bookmark", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<AddBookmarkResponse>, t: Throwable) {
                Log.e("Bookmark", "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getBookmark(authToken: String){
        val client = ApiConfig.getApiService().getBookmark(authToken)
        client.enqueue(object: Callback<GetBookmarkResponse>{
            override fun onResponse(
                call: Call<GetBookmarkResponse>,
                response: Response<GetBookmarkResponse>
            ) {
                if (response.isSuccessful){
                    _bookmarkedPlace.value=response.body()?.bookmarks
                    Log.e("Bookmark", "Succesfully Fetch All Bookmarked Place")
                }else{
                    Log.e("Bookmark", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GetBookmarkResponse>, t: Throwable) {
                Log.e("Bookmark", "onFailure: ${t.message.toString()}")
            }

        })
    }

    //Like Section
    //Bookmark Secction
    fun addLikes(authToken: String, itemId: Int){
        val jsonObject = JsonObject().apply {
            addProperty("item_id", itemId)
        }
        val client = ApiConfig.getApiService().addLike(authToken, jsonObject)
        client.enqueue(object: Callback<LikeResponse>{
            override fun onResponse(
                call: Call<LikeResponse>,
                response: Response<LikeResponse>
            ) {
                if (response.isSuccessful){
                    _likeResponse.value=response.body()
                    _statusLike.value=response.body()
                    Log.e("Like", "Succesfully Like Place")
                }else{
                    Log.e("Like", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LikeResponse>, t: Throwable) {
                Log.e("Like", "onFailure: ${t.message.toString()}")
            }
        })
    }

    //Bookmark Secction
    fun statusLikes(authToken: String, itemId: Int){
        val jsonObject = JsonObject().apply {
            addProperty("item_id", itemId)
        }
        val client = ApiConfig.getApiService().addLike(authToken, jsonObject)
        client.enqueue(object: Callback<LikeResponse>{
            override fun onResponse(
                call: Call<LikeResponse>,
                response: Response<LikeResponse>
            ) {
                if (response.isSuccessful){
                    addLikes(authToken, itemId)
                    Log.e("Like", "Succesfully Fetch Status Like Place")
                }else{
                    Log.e("Like", "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<LikeResponse>, t: Throwable) {
                Log.e("Like", "onFailure: ${t.message.toString()}")
            }
        })
    }

    //Pricing Section
    fun getPricing(authToken: String, itemId: Int, latitude: Double, longitude: Double){
        val jsonObject = JsonObject().apply {
            addProperty("userLat", latitude)
            addProperty("userLon", longitude)
        }
        val client = ApiConfig.getApiService().fuelPricing(authToken, jsonObject, itemId)
        client.enqueue(object: Callback<PricingResponse>{
            override fun onResponse(
                call: Call<PricingResponse>,
                response: Response<PricingResponse>
            ) {
                if (response.isSuccessful){
                    _listCost.value=response.body()
                    Log.e("Travel Cost", "Succesfully Fetch Travel Cost")
                }else{
                    Log.e("Travel Cost", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<PricingResponse>, t: Throwable) {
                Log.e("Travel Cost", "onFailure: ${t.message.toString()}")
            }

        })
    }
}