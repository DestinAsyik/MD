package com.id.destinasyik.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.id.destinasyik.data.remote.response.LoginResponse
import com.id.destinasyik.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val _login = MutableLiveData<LoginResponse>()
    val login: LiveData<LoginResponse> = _login
    private val _registrationStatus = MutableLiveData<Boolean>()
    val registrationStatus: LiveData<Boolean> get() = _registrationStatus

    fun registerUser (
        username: String,
        name: String,
        password: String,
        age: Int,
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
        client.enqueue(object : Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful){
                    _registrationStatus.value = true
                    Log.e("Login Auth", "Succesfully Login")
                }else{
                    _registrationStatus.value = false
                    Log.e("Login Auth", "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
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
}