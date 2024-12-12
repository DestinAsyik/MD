package com.id.destinasyik.ui.preference

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.lifecycle.ViewModelProvider
import com.id.destinasyik.R
import com.id.destinasyik.data.remote.response.UpdateProfile
import com.id.destinasyik.databinding.FragmentNearestPlaceBinding
import com.id.destinasyik.databinding.FragmentUserPreferenceBinding
import com.id.destinasyik.model.MainViewModel
import com.id.destinasyik.ui.MainActivity
import com.id.destinasyik.ui.login.LoginActivity
import com.id.destinasyik.ui.profile.EditProfileActivity
import com.id.destinasyik.ui.register.RegisterActivity
import com.id.destinasyik.ui.updatepw.UpdatePasswordActivity
import androidx.appcompat.app.AppCompatDelegate

class UserPreferenceFragment : Fragment() {
    private var _binding: FragmentUserPreferenceBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserPreferenceBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        
        loadUserProfile()
        setupAction()
        setupThemeSwitch()
        return binding.root
    }

    private fun loadUserProfile() {
        val sharedPreferences = requireContext().getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("token", "") ?: ""
        
        if (token.isNotEmpty()) {
            viewModel.getProfile("Bearer $token")
            viewModel.profile.observe(viewLifecycleOwner) { user ->
                user?.let {
                    // Update the TextViews in your card layout
                    binding.apply {
                        userNameText.text = it.name
                        userEmailText.text = it.email
                    }
                }
            }
        }
    }

    private fun logout(viewModel: MainViewModel) {
        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("token",null)
        val tokenBearer = "Bearer "+token
        viewModel.logout(tokenBearer)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putInt("userId",0)
        editor.putString("token","")
        editor.apply()
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
    }

    private fun setupAction(){
        binding.btnPersonalInfo.setOnClickListener {
            startActivity(Intent(requireContext(), EditProfileActivity::class.java))
        }

        binding.btnChangePassword.setOnClickListener {
            startActivity(Intent(requireContext(), UpdatePasswordActivity::class.java))
        }

        binding.btnLogout.setOnClickListener {
            logout(viewModel)
        }
    }

    private fun setupThemeSwitch() {
        // Get the saved theme preference
        val sharedPreferences = requireContext().getSharedPreferences("ThemePrefs", MODE_PRIVATE)
        val isDarkMode = sharedPreferences.getBoolean("isDarkMode", false)
        
        // Set the initial switch state
        binding.themeSwitch.isChecked = isDarkMode

        binding.themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            // Save the theme preference
            sharedPreferences.edit().putBoolean("isDarkMode", isChecked).apply()
            
            // Apply the theme
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadUserProfile()
    }
}