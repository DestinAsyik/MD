package com.id.destinasyik.ui.profile

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.id.destinasyik.databinding.ActivityEditProfileBinding
import com.id.destinasyik.model.MainViewModel
import java.util.Calendar

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        setupBackButton()
        setupDatePicker()
        setupTravelPreferencesDropdown()
        setupEditProfileButton()
        observeProfileData()
    }

    private fun setupBackButton() {
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
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

    private fun setupTravelPreferencesDropdown() {
        val typePreference = arrayOf(
            "Budaya", "Taman Hiburan", "Cagar Alam", "Bahari",
            "Pusat Perbelanjaan", "Tempat Ibadah", "Agrowisata"
        )

        (binding.typePreferences as? MaterialAutoCompleteTextView)?.setSimpleItems(typePreference)
    }

    private fun setupEditProfileButton() {
        binding.buttonEditProfile.setOnClickListener {
            val name = binding.etName.text.toString()
            val username = binding.etUsername.text.toString()
            val email = binding.etEmail.text.toString()
            val dateOfBirth = binding.etBorn.text.toString()
            val city = binding.etCity.text.toString()
            val preferredCategory = binding.typePreferences.text.toString()

            when {
                name.isEmpty() -> showToast("Name cannot be empty")
                username.isEmpty() -> showToast("Username cannot be empty")
                email.isEmpty() -> showToast("Email cannot be empty")
                dateOfBirth.isEmpty() -> showToast("Date of birth cannot be empty")
                city.isEmpty() -> showToast("City cannot be empty")
                preferredCategory.isEmpty() -> showToast("Please select travel preferences")
                else -> {
                    binding.progressBar.visibility = View.VISIBLE
                    val token = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                        .getString("token", "") ?: ""
                    viewModel.updateProfile(
                        username, name, email, dateOfBirth, city, preferredCategory, "Bearer $token"
                    )
                }
            }
        }
    }

    private fun observeProfileData() {
        viewModel.userProfile.observe(this) { profileResponse ->
            profileResponse?.user?.let { profile ->
                binding.apply {
                    etName.setText(profile.name)
                    etUsername.setText(profile.username)
                    etEmail.setText(profile.email)
                    etBorn.setText(profile.tanggalLahir)
                    etCity.setText(profile.city)
                    (typePreferences as? MaterialAutoCompleteTextView)?.setText(profile.preferedCategory, false)
                }
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(this) { errorMessage ->
            errorMessage?.let { showToast(it) }
        }

        viewModel.successMessage.observe(this) { successMessage ->
            successMessage?.let { showToast(it) }
        }

        val token = getSharedPreferences("UserPrefs", MODE_PRIVATE)
            .getString("token", "") ?: ""
        viewModel.fetchUserProfile("Bearer $token")
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}