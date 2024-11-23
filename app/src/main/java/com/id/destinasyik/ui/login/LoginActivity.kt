package com.id.destinasyik.ui.login

import android.content.Intent
import android.content.SharedPreferences
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
import com.id.destinasyik.ui.MainActivity
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
    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        if(isLoggedIn()){
            navigateToMainScreen()
        }
        binding.button3.setOnClickListener {
            login()
        }
    }

    private fun login(){
        val username = binding.emailInput.text.toString()
        val password = binding.passwordInput.text.toString()
        var isAuthenticated = false
        viewModel.loginAuth(username,password)
        viewModel.login.observe(this, Observer { response ->
            if(response!=null){
                saveUserSession(response)
                Log.d("AUTH TOKEN","${response.token}")
                Toast.makeText(this@LoginActivity, "Login berhasil", Toast.LENGTH_SHORT).show()
                navigateToMainScreen()
            }else{
                Toast.makeText(this@LoginActivity, "Failed to Login", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveUserSession(userData: LoginResponse) {
        val sharedPreferences: SharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        userData.user?.userId?.let { editor.putInt("userId", it) }
        editor.putString("token",userData.token)
        editor.apply()
    }

    private fun isLoggedIn (): Boolean{
        val sharedPreferences: SharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("token",null)
        if(token?.isNotEmpty() == true){
            return true
        }
        return false
    }

    private fun navigateToMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}