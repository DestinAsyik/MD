package com.id.destinasyik.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.id.destinasyik.data.remote.response.BookmarkResponse
import com.id.destinasyik.data.remote.response.BookmarksItem
import com.id.destinasyik.data.remote.response.DeleteResponse
import com.id.destinasyik.data.remote.response.Destination
import com.id.destinasyik.data.remote.response.GetBookmarkResponse
import com.id.destinasyik.data.remote.response.LoginResponse
import com.id.destinasyik.data.remote.response.LogoutResponse
import com.id.destinasyik.data.remote.response.ProfileResponse
import com.id.destinasyik.data.remote.response.ReccomPlace
import com.id.destinasyik.data.remote.response.RecommByCategoryResponse
import com.id.destinasyik.data.remote.response.RecommByNearbyResponse
import com.id.destinasyik.data.remote.response.RegisterResponse
import com.id.destinasyik.data.remote.response.UpdateProfile
import com.id.destinasyik.data.remote.response.User
import com.id.destinasyik.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.launch
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
    private val _bookmarkedPlace = MutableLiveData<List<BookmarksItem?>?>()
    val bookmarkedPlace: LiveData<List<BookmarksItem?>?> = _bookmarkedPlace
    private val _loadingEvent = MutableLiveData<Boolean>()
    val loadingEvent: LiveData<Boolean> = _loadingEvent

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
            addProperty("passsword", password)
            addProperty("age", age)
            addProperty("email", email)
            addProperty("city", city)
            addProperty("prefered_category", preferedCategory)
        }
        val client = ApiConfig.getApiService().registerUser(jsonObject)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    _registrationStatus.value = true
                    Log.e("Login Auth", "Succesfully Login")
                } else {
                    _registrationStatus.value = false
                    Log.e("Login Auth", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _registrationStatus.value = false
                Log.e("Login Auth", "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun loginAuth(username: String, password: String) {
        val jsonObject = JsonObject().apply {
            addProperty("username", username)
            addProperty("password", password)
        }
        val client = ApiConfig.getApiService().loginUser(jsonObject)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful && response.body()?.token != null) {
                    _login.value = response.body()
                    Log.d("Login Auth", "Successfully Login")
                } else {
                    Log.e("Login Auth", "Login failed: ${response.code()} - ${response.message()}")
                    Log.e("Login Auth", "Response body: ${response.errorBody()?.string()}")
                    
                    _login.value = LoginResponse(
                        message = response.body()?.message ?: "Invalid credentials",
                        user = null,
                        token = null
                    )
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("Login Auth", "onFailure: ${t.message.toString()}")
                _login.value = LoginResponse(
                    message = "Network error: ${t.message}",
                    user = null,
                    token = null
                )
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


    //Bookmark Secction
    fun addBookmark(authToken: String, itemId: Int){
        val jsonObject = JsonObject().apply {
            addProperty("item_id", itemId)
        }
        val client = ApiConfig.getApiService().addBookmark(authToken, jsonObject)
        client.enqueue(object: Callback<BookmarkResponse>{
            override fun onResponse(
                call: Call<BookmarkResponse>,
                response: Response<BookmarkResponse>
            ) {
                if (response.isSuccessful){
                    Log.e("Bookmark", "Succesfully Bookmark Place")
                }else{
                    Log.e("Bookmark", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<BookmarkResponse>, t: Throwable) {
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

    fun deleteBookmark(authToken: String, id:Int){
        val client = ApiConfig.getApiService().deleteBookmark(authToken, id)
        client.enqueue(object: Callback<DeleteResponse>{
            override fun onResponse(
                call: Call<DeleteResponse>,
                response: Response<DeleteResponse>
            ) {
                if (response.isSuccessful){
                    Log.e("Bookmark", "Succesfully Delete Bookmarked Place")
                }else{
                    Log.e("Bookmark", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DeleteResponse>, t: Throwable) {
                Log.e("Bookmark", "onFailure: ${t.message.toString()}")
            }

        })
    }


}