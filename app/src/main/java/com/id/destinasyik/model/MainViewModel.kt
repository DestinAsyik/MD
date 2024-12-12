package com.id.destinasyik.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.id.destinasyik.data.local.AppDatabase
import com.id.destinasyik.data.local.entity.UserProfile
import com.id.destinasyik.data.paging.PlacePagingSource
import com.id.destinasyik.data.remote.response.AddBookmarkResponse
import com.id.destinasyik.data.remote.response.AddReviewResponse
import com.id.destinasyik.data.remote.response.ApiError
import com.id.destinasyik.data.remote.response.BookmarksItem
import com.id.destinasyik.data.remote.response.GetBookmarkResponse
import com.id.destinasyik.data.remote.response.GetReviewResponseItem
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
import com.id.destinasyik.data.remote.response.TokenError
import com.id.destinasyik.data.remote.response.UpdateProfile
import com.id.destinasyik.data.remote.response.User
import com.id.destinasyik.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val userProfileDao = AppDatabase.getDatabase(application).userProfileDao()

    private val _invalidToken = MutableLiveData<String?>()
    val invalidToken: LiveData<String?> = _invalidToken

    private val _login = MutableLiveData<LoginResponse>()
    val login: LiveData<LoginResponse> = _login
    private val _errorLoginStatus = MutableLiveData<String?>()
    val errorLoginStatus: LiveData<String?> = _errorLoginStatus

    private val _register = MutableLiveData<RegisterResponse>()
    val register: LiveData<RegisterResponse> = _register
    private val _errorRegisterStatus = MutableLiveData<String?>()
    val errorRegisterStatus: LiveData<String?> = _errorRegisterStatus

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

    private val _addReview = MutableLiveData<AddReviewResponse>()
    val addReview: LiveData<AddReviewResponse> = _addReview
    private val _reviewErrorStatus = MutableLiveData<String?>()
    val reviewErrorStatus: LiveData<String?> = _reviewErrorStatus

    private val _placeReviews = MutableLiveData<List<GetReviewResponseItem?>?>()
    val placeReviews: LiveData<List<GetReviewResponseItem?>?> = _placeReviews

    private val _userProfile = MutableLiveData<ProfileResponse>()
    val userProfile: LiveData<ProfileResponse> = _userProfile

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _successMessage = MutableLiveData<String?>()
    val successMessage: LiveData<String?> = _successMessage

    fun clearErrorStatus() {
        _errorRegisterStatus.value = null
        _errorLoginStatus.value = null
        _reviewErrorStatus.value = null
        _invalidToken.value = null
    }

    fun registerUser (
        username: String,
        name: String,
        password: String,
        email: String,
        tanggal_lahir: String,
        city: String,
        preferedCategory: String
    ) {
        val jsonObject = JsonObject().apply {
            addProperty("username", username)
            addProperty("name", name)
            addProperty("password", password)
            addProperty("email", email)
            addProperty("tanggal_lahir", tanggal_lahir)
            addProperty("city", city)
            addProperty("prefered_category", preferedCategory)
        }
        val client = ApiConfig.getApiService().registerUser(jsonObject)
        client.enqueue(object : Callback<RegisterResponse>{
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful){
                    _register.value=response.body()
                    Log.e("Register Auth", "Succesfully Register")
                }else{
                    val errorBody = response.errorBody()?.string() // Ambil error body sebagai string
                    errorBody?.let {
                        val apiError = Gson().fromJson(it, ApiError::class.java) // Parsing dengan Gson
                        _errorRegisterStatus.value = apiError.error
                    }
                    Log.e("Register Auth", "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
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
                    val errorBody = response.errorBody()?.string() // Ambil error body sebagai string
                    errorBody?.let {
                        val apiError = Gson().fromJson(it, ApiError::class.java) // Parsing dengan Gson
                        _errorLoginStatus.value = apiError.error
                    }
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
                    val errorBody = response.errorBody()?.string() // Ambil error body sebagai string
                    errorBody?.let {
                        val apiError = Gson().fromJson(it, TokenError::class.java) // Parsing dengan Gson
                        _invalidToken.value = apiError.message
                        Log.d("INVALID TOKEN MVM PARSE","${apiError.message}")
                    }
                    Log.d("INVALID TOKEN MVM","$errorBody")

                    Log.e("User Profile", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                Log.e("User Profile", "onFailure: ${t.message.toString()}")
            }

        })
    }

    fun updateProfile(
        username: String,
        name: String,
        email: String,
        dateOfBirth: String,
        city: String,
        preferredCategory: String,
        authToken: String
    ) {
        _isLoading.value = true
        val jsonObject = JsonObject().apply {
            addProperty("username", username)
            addProperty("name", name)
            addProperty("email", email)
            addProperty("tanggal_lahir", dateOfBirth)
            addProperty("city", city)
            addProperty("prefered_category", preferredCategory)
        }

        val client = ApiConfig.getApiService().updateProfile(authToken, jsonObject)
        client.enqueue(object : Callback<UpdateProfile> {
            override fun onResponse(
                call: Call<UpdateProfile>,
                response: Response<UpdateProfile>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    fetchUserProfile(authToken)
                    _successMessage.value = "Profile updated successfully!"
                    _error.value = null
                } else {
                    val errorBody = response.errorBody()?.string()
                    errorBody?.let {
                        val apiError = Gson().fromJson(it, ApiError::class.java) // Parsing dengan Gson
                        _error.value = apiError.error
                    }
                }
            }

            override fun onFailure(call: Call<UpdateProfile>, t: Throwable) {
                _isLoading.value = false
                _error.value = "Error: ${t.message}"
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
        _loadingEvent.value=true
        val client = ApiConfig.getApiService().getBookmark(authToken)
        client.enqueue(object: Callback<GetBookmarkResponse>{
            override fun onResponse(
                call: Call<GetBookmarkResponse>,
                response: Response<GetBookmarkResponse>
            ) {
                if (response.isSuccessful){
                    _loadingEvent.value=false
                    _bookmarkedPlace.value=response.body()?.bookmarks
                    Log.e("Bookmark", "Succesfully Fetch All Bookmarked Place")
                }else{
                    _loadingEvent.value=false
                    val errorBody = response.errorBody()?.string() // Ambil error body sebagai string
                    errorBody?.let {
                        val apiError = Gson().fromJson(it, TokenError::class.java) // Parsing dengan Gson
                        _invalidToken.value = apiError.message
                    }
                    Log.e("Bookmark", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GetBookmarkResponse>, t: Throwable) {
                _loadingEvent.value=false
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

    fun addReview(authToken: String, itemId: Int, rating: Float, review: String, latitude: Double, longitude: Double){
        val jsonObject = JsonObject().apply {
            addProperty("rating", rating)
            addProperty("review", review)
            addProperty("userLat", latitude)
            addProperty("userLon", longitude)
        }
        val client = ApiConfig.getApiService().addReview(authToken, jsonObject, itemId)
        client.enqueue(object: Callback<AddReviewResponse>{
            override fun onResponse(
                call: Call<AddReviewResponse>,
                response: Response<AddReviewResponse>
            ) {
                if (response.isSuccessful){
                    _addReview.value = response.body()
                    Log.e("Travel Cost", "Succesfully Fetch Travel Cost")
                }else{
                    val errorBody = response.errorBody()?.string() // Ambil error body sebagai string
                    errorBody?.let {
                        val apiError = Gson().fromJson(it, ApiError::class.java) // Parsing dengan Gson
                        _reviewErrorStatus.value = apiError.error
                    }
                    Log.e("Travel Cost", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<AddReviewResponse>, t: Throwable) {
                Log.e("Travel Cost", "onFailure: ${t.message.toString()}")
            }

        })
    }

    fun getReview(authToken: String, itemId: Int){
        val client = ApiConfig.getApiService().getPlaceReviews(authToken, itemId)
        client.enqueue(object: Callback<List<GetReviewResponseItem>>{
            override fun onResponse(
                call: Call<List<GetReviewResponseItem>>,
                response: Response<List<GetReviewResponseItem>>
            ) {
                if (response.isSuccessful){
                    _placeReviews.value=response.body()
                    Log.e("Reviews", "Succesfully Get Reviews")
                }else{
                    Log.e("Reviews", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<GetReviewResponseItem>>, t: Throwable) {
                Log.e("Reviews", "onFailure: ${t.message.toString()}")
            }

        })
    }

    //search destination
    fun searchDestination(authToken: String, keyword: String): Flow<PagingData<ReccomPlace>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20, // Ukuran halaman
                enablePlaceholders = false // Jangan gunakan placeholder
            ),
            pagingSourceFactory = { PlacePagingSource(keyword, authToken) }
        ).flow.cachedIn(viewModelScope)
    }

    fun saveUserProfile(userProfile: UserProfile) {
        viewModelScope.launch {
            userProfileDao.insertUserProfile(userProfile)
        }
    }

    suspend fun getUserProfile(userId: Int): UserProfile? {
        return userProfileDao.getUserProfile(userId)
    }

    fun fetchUserProfile(authToken: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getProfile(authToken)
        client.enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(
                call: Call<ProfileResponse>,
                response: Response<ProfileResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _userProfile.value = response.body()
                    _error.value = null
                } else {
                    _error.value = "Failed to fetch profile: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                _isLoading.value = false
                _error.value = "Error: ${t.message}"
            }
        })
    }
}