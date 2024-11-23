package com.id.destinasyik.ui.recomended

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.id.destinasyik.data.repository.PlaceRepository
import com.id.destinasyik.databinding.FragmentCategoryPlaceBinding
import com.id.destinasyik.model.MainViewModel

class CategoryPlaceFragment : Fragment() {
    private var _binding: FragmentCategoryPlaceBinding? = null
    private val binding get() = _binding!!
    private lateinit var recommendedAdapter: RecommendedAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCategoryPlaceBinding.inflate(layoutInflater, container, false)
        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        setupRecommendedRecyclerView()
        loadData(viewModel)
        viewModel.loadingEvent.observe(viewLifecycleOwner){
            loadingPage(it)
        }
        return binding?.root
    }

    private fun setupRecommendedRecyclerView() {
        val recommendedRecyclerView = binding.rvRecommend
        recommendedAdapter = RecommendedAdapter()
        recommendedRecyclerView.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = recommendedAdapter
        }
    }

    private fun loadData(viewModel: MainViewModel) {
        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("token",null)
        val tokenBearer = "Bearer "+token
        viewModel.reccomCategory(tokenBearer)
        viewModel.placeReccomCategory.observe(viewLifecycleOwner){ place->
            Log.d("Reccom Category","$place")
            recommendedAdapter.submitList(place)
        }
    }

    private fun loadingPage(isLoading: Boolean) {
        binding.loadingBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}