package com.id.destinasyik.ui.preference

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.id.destinasyik.R
import com.id.destinasyik.databinding.ActivityPreferedInputBinding
import com.id.destinasyik.model.MainViewModel
import com.id.destinasyik.ui.MainActivity

class PreferedInputActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPreferedInputBinding
    private lateinit var viewModel: MainViewModel
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreferedInputBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setupViewModel()
        setupClickListeners()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private fun setupClickListeners() {
        binding.button3.setOnClickListener {
            savePreferences()
        }
    }

    private fun savePreferences() {
        val selectedPreferences = getSelectedPreferences()
        if (selectedPreferences.isEmpty()) {
            Toast.makeText(this, "Please select at least one preference", Toast.LENGTH_SHORT).show()
            return
        }

        // Get the stored token
        val sharedPreferences: SharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("token", "") ?: ""

        // Update user profile with selected preferences
        viewModel.updateProfile(
            username = "", // These fields will be unchanged
            name = "",
            dateOfBirth = "",
            email = "",
            city = "",
            preferredCategory = selectedPreferences,
            authToken = "Bearer $token"
        )

        // Navigate to main activity
        navigateToMain()
    }

    private fun getSelectedPreferences(): String {
        val selectedPreference = with(binding) {
            when (radioPreferences.checkedRadioButtonId) {
                R.id.preferedBudaya -> "Budaya"
                R.id.preferedTaman -> "Taman Hiburan"
                R.id.preferedBahari -> "Bahari"
                R.id.preferedAlam -> "Cagar Alam"
                R.id.preferedPerbelanjaan -> "Pusat Perbelanjaan"
                R.id.preferedReligius -> "Tempat Ibadah"
                R.id.preferedAgrowisata -> "Agrowisata"
                else -> ""
            }
        }
        
        return if (selectedPreference.isEmpty()) "" else selectedPreference
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        // Prevent going back - user must select preferences
        Toast.makeText(this, "Please select your travel preferences", Toast.LENGTH_SHORT).show()
    }
}