package com.id.destinasyik.ui.home

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.id.destinasyik.R
import com.id.destinasyik.data.repository.PlaceRepository
import com.id.destinasyik.databinding.FragmentHomeBinding
import com.id.destinasyik.ui.liked.PeopleLikedAdapter
import com.id.destinasyik.ui.login.LoginActivity
import com.id.destinasyik.ui.recomended.RecommendedAdapter

class HomeFragment : Fragment() {

    private var _binding:FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var recommendedAdapter: RecommendedAdapter
    private lateinit var peopleAdapter: PeopleLikedAdapter
    private val MAX_PEOPLE_LIKED = 5
    private val placeRepository = PlaceRepository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecommendedRecyclerView()
        setupPeopleLikedRecyclerView()
        loadData()
        setupSearch()
        setupProfileClickListener()
    }

    private fun setupProfileClickListener() {
        binding.profileLayout.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecommendedRecyclerView() {
        val recommendedRecyclerView = binding.recommendedRecyclerView
        recommendedAdapter = RecommendedAdapter()
        recommendedRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = recommendedAdapter
        }

        recommendedAdapter.setOnItemClickListener { place ->
            Toast.makeText(requireContext(), "Clicked: ${place.name}", Toast.LENGTH_SHORT).show()
        }

        recommendedAdapter.setOnBookmarkClickListener { place ->
            Toast.makeText(requireContext(), "Bookmarked: ${place.name}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupPeopleLikedRecyclerView() {
        val peopleRecyclerView = binding.peopleLikedRecyclerView
        peopleAdapter = PeopleLikedAdapter(MAX_PEOPLE_LIKED)
        peopleRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = peopleAdapter
            isNestedScrollingEnabled = false
            minimumHeight = resources.getDimensionPixelSize(R.dimen.people_liked_min_height)
            clipToPadding = false
        }
    }

    private fun loadData() {
        recommendedAdapter.submitList(placeRepository.getRecommendedPlaces())
        peopleAdapter.submitList(placeRepository.getLikedPlaces())
    }

    private fun setupSearch() {
        val searchEditText = binding.searchEditText
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                if (query.isNotEmpty()) {
                    val searchResults = placeRepository.searchPlaces(query)
                    recommendedAdapter.submitList(searchResults)
                } else {
                    recommendedAdapter.submitList(placeRepository.getRecommendedPlaces())
                }
            }
        })
    }

}