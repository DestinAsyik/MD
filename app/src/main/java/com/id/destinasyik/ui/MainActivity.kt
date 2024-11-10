package com.id.destinasyik.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.id.destinasyik.R
import com.id.destinasyik.data.repository.PlaceRepository
import com.id.destinasyik.ui.liked.PeopleLikedAdapter
import com.id.destinasyik.ui.recomended.RecommendedAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var recommendedAdapter: RecommendedAdapter
    private lateinit var peopleAdapter: PeopleLikedAdapter
    private val MAX_PEOPLE_LIKED = 5
    private val placeRepository = PlaceRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupRecommendedRecyclerView()
        setupPeopleLikedRecyclerView()
        loadData()
        setupSearch()
    }

    private fun setupRecommendedRecyclerView() {
        val recommendedRecyclerView = findViewById<RecyclerView>(R.id.recommendedRecyclerView)
        recommendedAdapter = RecommendedAdapter()
        recommendedRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = recommendedAdapter
        }

        recommendedAdapter.setOnItemClickListener { place ->
            Toast.makeText(this, "Clicked: ${place.name}", Toast.LENGTH_SHORT).show()
        }

        recommendedAdapter.setOnBookmarkClickListener { place ->
            Toast.makeText(this, "Bookmarked: ${place.name}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupPeopleLikedRecyclerView() {
        val peopleRecyclerView = findViewById<RecyclerView>(R.id.peopleLikedRecyclerView)
        peopleAdapter = PeopleLikedAdapter(MAX_PEOPLE_LIKED)
        peopleRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
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
        val searchEditText = findViewById<EditText>(R.id.searchEditText)
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