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

    private fun login(username: String, password: String){
        val jsonObject = JsonObject().apply {
            addProperty("username", username)
            addProperty("password", password)
        }
        val client = ApiConfig.getApiService().login(jsonObject)
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