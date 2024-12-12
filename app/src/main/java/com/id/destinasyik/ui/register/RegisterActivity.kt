package com.id.destinasyik.ui.register

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.id.destinasyik.databinding.ActivityRegisterBinding
import com.id.destinasyik.model.MainViewModel
import com.id.destinasyik.ui.login.LoginActivity
import java.util.Calendar
import android.app.DatePickerDialog
import androidx.lifecycle.Observer
import com.google.android.material.textfield.MaterialAutoCompleteTextView

class RegisterActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        setupDatePicker()
        setupRegisterButton()
        setupTravelPreferencesSpinner()
    }

    private fun setupTravelPreferencesSpinner() {
        val typePreference = arrayOf(
            "Budaya", "Taman Hiburan", "Cagar Alam", "Bahari",
            "Pusat Perbelanjaan", "Tempat Ibadah", "Agrowisata"
        )
        
        (binding.typePreferences as? MaterialAutoCompleteTextView)?.setSimpleItems(typePreference)
    }

    private fun setupDatePicker() {
        binding.inputBorn.setOnClickListener {
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
                    binding.inputBorn.setText(selectedDate)
                },
                year,
                month,
                day
            ).show()
        }
    }

    private fun validateInputs(): Boolean {
        val username = binding.etUsername.text.toString()
        val email = binding.etEmail.text.toString()
        val password = binding.passwordInput.text.toString()
        val passwordConfirmation = binding.passwordConfirmation.text.toString()
        val city = binding.etCity.text.toString()
        val tanggalLahir = binding.inputBorn.text.toString()
        val name = binding.etFullName.text.toString()
        val preferredCategory = binding.typePreferences.text.toString()

        if (username.isEmpty()){
            binding.etUsername.error = "Username cannot be empty"
        }

        if (email.isEmpty()) {
            binding.etEmail.error = "Email cannot be empty"
            return false
        }

        if (password.isEmpty()) {
            binding.passwordInput.error = "Password cannot be empty"
            return false
        }

        if (passwordConfirmation.isEmpty()) {
            binding.passwordInput.error = "Password Confirmation cannot be empty"
            return false
        }

        if (passwordConfirmation!=password) {
            binding.passwordConfirmation.error = "Password Confirmation must be the same with password"
            return false
        }

        if (city.isEmpty()) {
            binding.etCity.error = "City cannot be empty"
            return false
        }

        if (tanggalLahir.isEmpty()) {
            binding.inputBorn.error = "Birth Day cannot be empty"
            return false
        }

        if (name.isEmpty()) {
            binding.etFullName.error = "Full Name cannot be empty"
            return false
        }

        if (preferredCategory.isEmpty()) {
            binding.typePreferences.error = "Must Choose One Prefered Category"
            return false
        }

        return true
    }

    private fun setupRegisterButton() {
        binding.buttonRegister.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.passwordInput.text.toString()
            val passwordConfirmation = binding.passwordConfirmation.text.toString()
            val city = binding.etCity.text.toString()
            val tanggalLahir = binding.inputBorn.text.toString()
            val name = binding.etFullName.text.toString()
            val preferredCategory = binding.typePreferences.text.toString()
            if(validateInputs()){
                viewModel.registerUser(
                    username = username,
                    name = name,
                    password = password,
                    tanggal_lahir = tanggalLahir,
                    email = email,
                    city = city,
                    preferedCategory = preferredCategory

                )
                viewModel.errorRegisterStatus.observe(this, Observer { error->
                    error?.let{
                        if(error.isEmpty()){
                            showToast("Succesfully Register an Account!")
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        }else{
                            showToast("Failed to Register : $error")
                            viewModel.clearErrorStatus()
                        }
                    }
                })
            }
        }

        binding.btnSignIn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}