package com.id.destinasyik.ui.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.id.destinasyik.databinding.ActivityRegisterBinding
import com.id.destinasyik.model.MainViewModel
import com.id.destinasyik.ui.login.LoginActivity
import java.util.Calendar
import android.app.DatePickerDialog

class RegisterActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        setupDatePicker()
        setupRegisterButton()
        observeRegistrationStatus()
    }

    private fun setupDatePicker() {
        binding.etBorn.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    // Format the date as YYYY-MM-DD (common backend format)
                    val formattedDay = String.format("%02d", selectedDay)
                    val formattedMonth = String.format("%02d", selectedMonth + 1)
                    val selectedDate = "$selectedYear-$formattedMonth-$formattedDay"
                    binding.etBorn.setText(selectedDate)
                },
                year,
                month,
                day
            ).show()
        }
    }

    private fun setupRegisterButton() {
        binding.buttonRegister.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.passwordInput.text.toString()
            val passwordConfirmation = binding.passwordConfirmation.text.toString()
            val city = binding.etCity.text.toString()
            val tanggalLahir = binding.etBorn.text.toString() // This will be in format "DD/MM/YYYY"
            val name = binding.etFullName.text.toString()

            // Input validation
            when {
                username.isEmpty() -> showToast("Username cannot be empty")
                email.isEmpty() -> showToast("Email cannot be empty")
                password.isEmpty() -> showToast("Password cannot be empty")
                passwordConfirmation.isEmpty() -> showToast("Password confirmation cannot be empty")
                city.isEmpty() -> showToast("City cannot be empty")
                tanggalLahir == "Born" || tanggalLahir.isEmpty() -> showToast("Date of birth cannot be empty")
                name.isEmpty() -> showToast("Name cannot be empty")
                password != passwordConfirmation -> showToast("Passwords do not match")
                else -> {
                    // Default preferred category if none selected
                    val preferredCategory = "Nature" // You can modify this as needed

                    // Proceed with registration
                    viewModel.registerUser(
                        username = username,
                        name = name,
                        password = password,
                        tanggal_lahir = tanggalLahir,
                        email = email,
                        city = city,
                        preferedCategory = preferredCategory
                    )
                }
            }
        }
    }

    private fun observeRegistrationStatus() {
        viewModel.registrationStatus.observe(this) { isRegistered ->
            if (isRegistered) {
                showToast("Registration successful!")
                // Navigate to login screen
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                showToast("Registration failed. Please try again.")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}