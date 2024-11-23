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
import com.id.destinasyik.databinding.FragmentNearestPlaceBinding
import com.id.destinasyik.databinding.FragmentUserPreferenceBinding
import com.id.destinasyik.model.MainViewModel
import com.id.destinasyik.ui.MainActivity
import com.id.destinasyik.ui.login.LoginActivity

class UserPreferenceFragment : Fragment() {
    private var _binding: FragmentUserPreferenceBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserPreferenceBinding.inflate(layoutInflater, container, false)
        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding.btnLogout.setOnClickListener {
            logout(viewModel)
        }
        return binding?.root
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