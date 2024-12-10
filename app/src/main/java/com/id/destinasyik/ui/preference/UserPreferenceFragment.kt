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
import androidx.lifecycle.lifecycleScope
import com.id.destinasyik.R
import com.id.destinasyik.data.local.entity.UserProfile
import com.id.destinasyik.databinding.FragmentNearestPlaceBinding
import com.id.destinasyik.databinding.FragmentUserPreferenceBinding
import com.id.destinasyik.model.MainViewModel
import com.id.destinasyik.ui.MainActivity
import com.id.destinasyik.ui.login.LoginActivity
import com.id.destinasyik.ui.profile.EditProfileActivity
import kotlinx.coroutines.launch

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
        updateProfile(viewModel)
        
        binding.btnLogout.setOnClickListener {
            logout(viewModel)
        }
        return binding.root
    }

    private fun loadUserProfile() {
        val sharedPreferences = requireContext().getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("token", "") ?: ""
        val userId = sharedPreferences.getInt("userId", 0)

        if (token.isNotEmpty()) {
            viewModel.getProfile("Bearer $token")
            viewModel.profile.observe(viewLifecycleOwner) { user ->
                user?.let {
                    val userProfile = UserProfile(
                        it.userId ?: 0,
                        it.username ?: "Unknown User",
                        it.email ?: "No Email"
                    )
                    viewModel.saveUserProfile(userProfile)
                    binding.apply {
                        userNameText.text = it.username ?: "Unknown User"
                        userEmailText.text = it.email ?: "No Email"
                    }
                }
            }
        } else {
            viewLifecycleOwner.lifecycleScope.launch {
                val userProfile = viewModel.getUserProfile(userId)
                userProfile?.let {
                    binding.apply {
                        userNameText.text = it.username
                        userEmailText.text = it.email
                    }
                }
            }
        }
    }

    private fun updateProfile(viewModel: MainViewModel) {
        binding.btnPersonalInfo.setOnClickListener {
            val intent = Intent(activity, EditProfileActivity::class.java)
            startActivity(intent)
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
}