package com.id.destinasyik.ui.forgotpw

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.id.destinasyik.databinding.ActivityForgotPasswordBinding
import com.id.destinasyik.model.MainViewModel

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

//        setupViewModel()
        setupClickListeners()
    }

//    private fun setupViewModel() {
//        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
//
//        // Observe forgot password response
//        viewModel.forgotPasswordResponse.observe(this) { success ->
//            if (success) {
//                showToast("Reset password link has been sent to your email")
//                finish() // Close the activity and return to login
//            } else {
//                showToast("Failed to send reset password link. Please try again.")
//            }
//        }
//    }

    private fun setupClickListeners() {
        // Back button click listener
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        // Send button click listener
//        binding.buttonSend.setOnClickListener {
//            if (validateEmail()) {
//                sendResetPasswordEmail()
//            }
//        }
    }

    private fun validateEmail(): Boolean {
        val email = binding.etEmail.text.toString().trim()
        
        return when {
            email.isEmpty() -> {
                binding.emailInputLayout.error = "Email cannot be empty"
                false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding.emailInputLayout.error = "Please enter a valid email address"
                false
            }
            else -> {
                binding.emailInputLayout.error = null
                true
            }
        }
    }

//    private fun sendResetPasswordEmail() {
//        val email = binding.etEmail.text.toString().trim()
//        viewModel.sendResetPasswordEmail(email)
//    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}