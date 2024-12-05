package com.id.destinasyik.ui.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.id.destinasyik.databinding.ActivityRegisterBinding
import com.id.destinasyik.model.MainViewModel
import com.id.destinasyik.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setupViewModel()
        setupClickListeners()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        
        viewModel.registrationStatus.observe(this) { isRegistered ->
            if (isRegistered) {
                Toast.makeText(this, "Registration successful! Please login.", Toast.LENGTH_SHORT).show()
                navigateToLogin()
            }
        }
    }

    private fun setupClickListeners() {
        binding.buttonRegister.setOnClickListener {
            if (validateInputs()) {
                registerUser()
            }
        }
    }

    private fun validateInputs(): Boolean {
        val username = binding.etUsername.text.toString()
        val email = binding.etEmail.text.toString()
        val password = binding.passwordInput.text.toString()
        val passwordConfirmation = binding.passwordConfirmation.text.toString()
        val fullName = binding.etFullName.text.toString()
        val city = binding.etCity.text.toString()
        val age = binding.etBorn.text.toString()

        if (username.isEmpty()) {
            binding.etUsername.error = "Username cannot be empty"
            return false
        }

        if (email.isEmpty()) {
            binding.etEmail.error = "Email cannot be empty"
            return false
        }

        if (!isValidEmail(email)) {
            binding.etEmail.error = "Please enter a valid email"
            return false
        }

        if (password.isEmpty()) {
            binding.passwordInput.error = "Password cannot be empty"
            return false
        }

        if (password.length < 6) {
            binding.passwordInput.error = "Password must be at least 6 characters"
            return false
        }

        if (passwordConfirmation.isEmpty()) {
            binding.passwordConfirmation.error = "Please confirm your password"
            return false
        }

        if (password != passwordConfirmation) {
            binding.passwordConfirmation.error = "Passwords do not match"
            return false
        }

        if (fullName.isEmpty()) {
            binding.etFullName.error = "Full name cannot be empty"
            return false
        }

        if (city.isEmpty()) {
            binding.etCity.error = "City cannot be empty"
            return false
        }

        if (age.isEmpty()) {
            binding.etBorn.error = "Age cannot be empty"
            return false
        }

        return true
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun registerUser() {
        val username = binding.etUsername.text.toString()
        val email = binding.etEmail.text.toString()
        val password = binding.passwordInput.text.toString()
        val fullName = binding.etFullName.text.toString()
        val city = binding.etCity.text.toString()
        val age = binding.etBorn.text.toString()

        try {
            viewModel.registerUser(
                username = username,
                name = fullName,
                password = password,
                age = age,
                email = email,
                city = city,
                preferedCategory = " "
            )
        } catch (e: Exception) {
            Toast.makeText(this, "Registration failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        navigateToLogin()
    }
}