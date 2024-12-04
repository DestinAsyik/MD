package com.id.destinasyik.ui.bookmark

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.id.destinasyik.R
import com.id.destinasyik.databinding.FragmentBookmarkBinding
import com.id.destinasyik.databinding.FragmentHomeBinding
import com.id.destinasyik.model.MainViewModel
import com.id.destinasyik.ui.liked.PeopleLikedAdapter
import com.id.destinasyik.ui.recomended.RecommendedAdapter

class BookmarkFragment : Fragment() {
    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel
    private lateinit var bookmarkAdapter: BookmarkAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookmarkBinding.inflate(layoutInflater,container,false)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        setupRecommendedRecyclerView()
        loadData()
        return binding?.root
    }

    private fun setupRecommendedRecyclerView() {
        val recommendedRecyclerView = binding.bookmarkRecycleView
        bookmarkAdapter = BookmarkAdapter()
        recommendedRecyclerView.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = bookmarkAdapter
        }
    }

    private fun loadData() {
        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("token",null)
        val tokenBearer = "Bearer "+token
        viewModel.getBookmark(tokenBearer)
        viewModel.bookmarkedPlace.observe(viewLifecycleOwner){ place->
            Log.d("Reccom Category","$place")
            bookmarkAdapter.submitList(place)
        }
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }
}
