package com.id.destinasyik.ui.login

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.id.destinasyik.data.remote.response.LoginResponse
import com.id.destinasyik.databinding.ActivityLoginBinding
import com.id.destinasyik.model.MainViewModel
import com.id.destinasyik.ui.MainActivity
import com.id.destinasyik.ui.preference.PreferedInputActivity
import com.id.destinasyik.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setupViewModel()
        checkLoginStatus()
        setupClickListeners()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        
        viewModel.login.observe(this) { response ->
            handleLoginResponse(response)
        }
    }

    private fun checkLoginStatus() {
        if (isLoggedIn()) {
            navigateToMainScreen(false)
        }
    }

    private fun setupClickListeners() {
        binding.button3.setOnClickListener {
            if (validateInputs()) {
                login()
            }
        }

        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun validateInputs(): Boolean {
        val email = binding.emailInput.text.toString()
        val password = binding.passwordInput.text.toString()

        if (email.isEmpty()) {
            binding.emailInput.error = "Email cannot be empty"
            return false
        }

        if (password.isEmpty()) {
            binding.passwordInput.error = "Password cannot be empty"
            return false
        }

        return true
    }

    private fun login() {
        val username = binding.emailInput.text.toString()
        val password = binding.passwordInput.text.toString()
        viewModel.loginAuth(username, password)
        viewModel.errorLoginStatus.observe(this, Observer { error->
            error?.let{
                if(error.isEmpty()){
                    Toast.makeText(this@LoginActivity, "Login Succesful!", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this@LoginActivity, "Failed to Login : $error", Toast.LENGTH_SHORT).show()
                    viewModel.clearErrorStatus()
                }
            }
        })
    }

    private fun handleLoginResponse(response: LoginResponse?) {
        if (response != null) {
            saveUserSession(response)
            Log.d("LoginActivity", "Token: ${response.token}")
            Log.d("LoginActivity", "User: ${response.user}")
            Log.d("LoginActivity", "PreferedCategory: ${response.user?.preferedCategory}")
            
            Toast.makeText(this@LoginActivity, "Login successful", Toast.LENGTH_SHORT).show()
            
            if (response.user?.preferedCategory.isNullOrEmpty()) {
                Log.d("LoginActivity", "Navigating to PreferedInput")
                navigateToMainScreen(true)
            } else {
                Log.d("LoginActivity", "Navigating to Main")
                navigateToMainScreen(false)
            }
        } else {
            Log.e("LoginActivity", "Login response is null")
            Toast.makeText(this@LoginActivity, "Login failed. Please check your credentials.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveUserSession(userData: LoginResponse) {
        val sharedPreferences: SharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        userData.user?.userId?.let { editor.putInt("userId", it) }
        editor.putString("token", userData.token)
        editor.apply()
    }

    private fun isLoggedIn(): Boolean {
        val sharedPreferences: SharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)
        return !token.isNullOrEmpty()
    }

    private fun navigateToMainScreen(isFirstTime: Boolean) {
        val intent = if (isFirstTime) {
            Intent(this, PreferedInputActivity::class.java)
        } else {
            Intent(this, MainActivity::class.java)
        }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}