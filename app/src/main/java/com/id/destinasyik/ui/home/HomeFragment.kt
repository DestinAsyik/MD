package com.id.destinasyik.ui.home

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.id.destinasyik.R
import com.id.destinasyik.data.repository.PlaceRepository
import com.id.destinasyik.databinding.FragmentHomeBinding
import com.id.destinasyik.model.MainViewModel
import com.id.destinasyik.ui.recomended.CategoryPlaceFragment
import com.id.destinasyik.ui.recomended.NearestPlaceFragment
import com.id.destinasyik.ui.liked.PeopleLikedAdapter
import com.id.destinasyik.ui.login.LoginActivity
import com.id.destinasyik.ui.recomended.RecommendedAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding:FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var peopleAdapter: PeopleLikedAdapter
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater,container,false)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
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
        setupPeopleLikedRecyclerView()
        loadData()
        setupSearch()
        setupProfileClickListener()
        viewModel.loadingEvent.observe(viewLifecycleOwner){
            loadingPage(it)
        }
        return binding?.root
    }


    private fun setupProfileClickListener() {
        binding.profileLayout.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }
    }


    private fun setupPeopleLikedRecyclerView() {
        val peopleRecyclerView = binding.peopleLikedRecyclerView
        peopleAdapter = PeopleLikedAdapter()
        peopleRecyclerView.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = peopleAdapter
        }
    }

    private fun loadData() {
        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("token",null)
        val tokenBearer = "Bearer "+token
        viewModel.recommPeopleLiked(tokenBearer)
        viewModel.placeRecommPeopleLiked.observe(viewLifecycleOwner){places->
            peopleAdapter.submitList(places)
        }
    }

    private fun setupSearch() {
        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("token",null)
        val tokenBearer = "Bearer "+token
        val rvSearch = binding.rvSearch
        val adapterSearch = PlacePagingAdapter()
        rvSearch.apply {
            adapter = adapterSearch
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        binding.searchEditText.addTextChangedListener (object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val searchText = p0.toString()
                if(searchText.isNullOrEmpty()){
                    lifecycleScope.launch {
                        adapterSearch.submitData(PagingData.empty())
                    }
                }else{
                    Log.d("Search Keyword","$searchText")
                    lifecycleScope.launch {
                        viewModel.searchDestination(tokenBearer,searchText).collectLatest {response->
                            adapterSearch.submitData(response)
                        }
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }

    private fun showFragment(fragment: Fragment) {
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.commit()
    }

    private fun loadingPage(isLoading: Boolean) {
        binding.loadingBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}