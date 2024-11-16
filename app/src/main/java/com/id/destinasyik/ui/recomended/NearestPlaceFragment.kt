package com.id.destinasyik.ui.recomended

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.id.destinasyik.data.repository.PlaceRepository
import com.id.destinasyik.databinding.FragmentNearestPlaceBinding

class NearestPlaceFragment : Fragment() {
    private var _binding: FragmentNearestPlaceBinding? = null
    private val binding get() = _binding!!
    private lateinit var recommendedAdapter: RecommendedAdapter
    private val placeRepository = PlaceRepository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNearestPlaceBinding.inflate(layoutInflater, container, false)
        setupRecommendedRecyclerView()
        loadData()
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

        recommendedAdapter.setOnItemClickListener { place ->
            Toast.makeText(requireContext(), "Clicked: ${place.name}", Toast.LENGTH_SHORT).show()
        }

        recommendedAdapter.setOnBookmarkClickListener { place ->
            Toast.makeText(requireContext(), "Bookmarked: ${place.name}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadData() {
        recommendedAdapter.submitList(placeRepository.getRecommendedPlaces())
    }
}