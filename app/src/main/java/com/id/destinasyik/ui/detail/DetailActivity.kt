package com.id.destinasyik.ui.detail

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.id.destinasyik.R
import com.id.destinasyik.data.remote.response.ReccomPlace
import com.id.destinasyik.databinding.ActivityDetailBinding
import com.id.destinasyik.model.MainViewModel

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var adapter: DetailPagerAdapter
    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        supportActionBar?.hide()
        tabLayout = binding.tabLayout
        viewPager2 = binding.viewPager
        adapter = DetailPagerAdapter(this)
        viewPager2.adapter=adapter
        TabLayoutMediator(tabLayout,viewPager2){tab,position->
            tab.text = when(position){
                0 -> "Overview"
                1 -> "Review"
                else -> "Pricing"
            }
        }.attach()
        val place: ReccomPlace? = intent.getParcelableExtra("PLACE") as? ReccomPlace
        Log.d("PLACE DETAIL","$place")
        val sharedPreferences: SharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("token",null)
        val tokenBearer = "Bearer "+token
        loadData(place)
        checkIsBookmarked(tokenBearer, place)
        binding.btnBookmark.setOnClickListener {
            place?.itemId?.let { it1 -> viewModel.addBookmark(tokenBearer, it1) }
        }
    }

    private fun checkIsBookmarked(authToken: String, place: ReccomPlace?){
        place?.itemId?.let { it1 -> viewModel.statusBookmark(authToken, it1) }
        viewModel.statusBookmark.observe(this, Observer { response->
            response.isBookmarked?.let { iconChangeBookmark(it) }
        })
    }

    private fun iconChangeBookmark(isBookmark: Boolean){
        val icon = if(isBookmark){
            R.drawable.already_bookmark
        }else{
            R.drawable.bookmark_yet
        }
        binding.btnBookmark.setImageResource(icon)
    }

    private fun iconChangeLiked(isLiked: Boolean){
        val icon = if(isLiked){
            R.drawable.already_love
        }else{
            R.drawable.love_yet
        }
        binding.btnLike.setImageResource(icon)
    }

    private fun loadData(place: ReccomPlace?) {
        place?.let{
            binding.tvPlaceName.text=it.placeName
            Glide.with(this)
                .load(it.gambar)
                .into(binding.tvImagePlace)
        }
    }
}