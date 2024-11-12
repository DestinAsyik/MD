package com.id.destinasyik.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.id.destinasyik.R
import com.id.destinasyik.data.repository.PlaceRepository
import com.id.destinasyik.databinding.FragmentHomeBinding
import com.id.destinasyik.ui.liked.PeopleLikedAdapter
import com.id.destinasyik.ui.recomended.RecommendedAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
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