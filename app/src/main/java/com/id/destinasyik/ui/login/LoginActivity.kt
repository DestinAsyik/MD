package com.id.destinasyik.ui.login

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.id.destinasyik.data.remote.response.LoginResponse
import com.id.destinasyik.databinding.ActivityLoginBinding
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

        binding.button3.setOnClickListener { login() }
    }

    private fun login() {
        val username = binding.emailInput.text.toString()
        val password = binding.passwordInput.text.toString()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val user = authenticateUser(username, password)
                withContext(Dispatchers.Main) {
                    // Store the user data and token in the app's session
                    saveUserSession(user)
                    Toast.makeText(this@LoginActivity, "Login berhasil", Toast.LENGTH_SHORT).show()
                    // Navigate to the next screen
                    navigateToMainScreen()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
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