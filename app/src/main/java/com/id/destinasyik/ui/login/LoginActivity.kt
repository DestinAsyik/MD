package com.id.destinasyik.ui.login

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.id.destinasyik.data.remote.response.LoginResponse
import com.id.destinasyik.databinding.ActivityLoginBinding
import com.id.destinasyik.model.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding.button3.setOnClickListener { login(viewModel) }
    }

    private fun login(viewModel: MainViewModel) {
        val username = binding.emailInput.text.toString()
        val password = binding.passwordInput.text.toString()
        viewModel.loginAuth(username,password)
        viewModel.login.observe(this, Observer { response ->
            if(response!=null){
                saveUserSession(response)
                Log.d("AUTH TOKEN","${response.token}")
                Toast.makeText(this@LoginActivity, "Login berhasil", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this@LoginActivity, "Failed to Login", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private suspend fun authenticateUser(username: String, password: String): LoginResponse {
        val client = OkHttpClient()
        val requestBody = FormBody.Builder()
            .add("username", username)
            .add("password", password)
            .build()

        val request = Request.Builder()
            .url("https://bangkit2024.up.railway.app/api/destinAsyik/auth/login")
            .post(requestBody)
            .build()

        return withContext(Dispatchers.IO) {
            try {
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    Gson().fromJson(responseBody, LoginResponse::class.java)
                } else {
                    throw Exception("Invalid username or password")
                }
            } catch (e: IOException) {
                throw Exception("Error connecting to the server")
            }
        }
    }

    private fun saveUserSession(userData: LoginResponse) {
        // Save the user data and token in the app's session
        // This could involve storing the data in shared preferences, a database, or a session management library
    }

    private fun navigateToMainScreen() {
        // Navigate to the main screen of the app
    }
}