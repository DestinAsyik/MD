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
import com.google.android.material.tabs.TabLayout
import com.id.destinasyik.R
import com.id.destinasyik.data.repository.PlaceRepository
import com.id.destinasyik.databinding.FragmentHomeBinding
import com.id.destinasyik.ui.recomended.CategoryPlaceFragment
import com.id.destinasyik.ui.recomended.NearestPlaceFragment
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater,container,false)
        val tabLayout = binding.tabLayout
        showFragment(CategoryPlaceFragment())
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> showFragment(CategoryPlaceFragment())
                    1 -> showFragment(NearestPlaceFragment())
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                // Tidak perlu melakukan apa-apa di sini
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                // Tidak perlu melakukan apa-apa di sini
            }
        })
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        peopleAdapter.submitList(placeRepository.getLikedPlaces())
    }

    private fun setupSearch() {
        val searchEditText = binding.searchEditText

    }

    private fun showFragment(fragment: Fragment) {
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.commit()
    }
}