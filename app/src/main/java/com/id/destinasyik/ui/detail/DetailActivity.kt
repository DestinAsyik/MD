package com.id.destinasyik.ui.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.id.destinasyik.R
import com.id.destinasyik.data.remote.response.ReccomPlace
import com.id.destinasyik.databinding.ActivityDetailBinding
import com.id.destinasyik.model.MainViewModel
import java.text.DecimalFormat
import java.util.Locale

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var adapter: DetailPagerAdapter
    private lateinit var viewModel: MainViewModel
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        supportActionBar?.hide()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
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
        if(place?.distanceKm.toString().equals("null")){
            getLastLocation()
        }else{
            val distance = String.format(Locale.US, "%.2f", place?.distanceKm)+" Km"
            binding.tvRange.text=distance
        }
        val sharedPreferences: SharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("token",null)
        val tokenBearer = "Bearer "+token
        loadData(place)
        checkIsBookmarked(tokenBearer, place)
        checkIsLiked(tokenBearer, place)
        binding.btnDirection.setOnClickListener {
            place?.let {
                val latitude = place.latitude
                val longitude = place.longitude
                val gmmIntentUri = Uri.parse("google.navigation:q=$latitude,$longitude")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)

                mapIntent.setPackage("com.google.android.apps.maps")

                startActivity(mapIntent)
            }
        }
        binding.btnBookmark.setOnClickListener {
            place?.itemId?.let { it1 -> viewModel.addBookmark(tokenBearer, it1) }
        }
        binding.btnLike.setOnClickListener {
            place?.itemId?.let { it1 -> viewModel.addLikes(tokenBearer, it1) }
        }
        binding.btnBack.setOnClickListener{
            onBackPressed()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation(){
        if(isLocationPermissionGranted()){
            val lastLocation = fusedLocationProviderClient.lastLocation
            lastLocation.addOnSuccessListener {
                if(it != null){
                    loadDataRange(it.latitude,it.longitude)
                    Log.d("Current Loc","Latitude : ${it.latitude}, Longitude : ${it.longitude}")
                }
            }

            lastLocation.addOnFailureListener {
                Log.d("Current Loc","Failed to get Current Location")
            }
        }
    }

    private fun checkIsBookmarked(authToken: String, place: ReccomPlace?){
        place?.itemId?.let { it1 -> viewModel.statusBookmark(authToken, it1) }
        viewModel.statusBookmark.observe(this, Observer { response->
            response.isBookmarked?.let { iconChangeBookmark(it) }
        })
    }

    private fun checkIsLiked(authToken: String, place: ReccomPlace?){
        place?.itemId?.let { viewModel.statusLikes(authToken, it) }
        viewModel.statusLike.observe(this, Observer { response->
            response.data?.isLiked?.let { iconChangeLiked(it) }
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

    private fun loadDataRange(latitude: Double, longitude: Double) {
        val sharedPreferences: SharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("token",null)
        val tokenBearer = "Bearer "+token
        val place: ReccomPlace? = intent.getParcelableExtra("PLACE") as? ReccomPlace
        place?.itemId?.let { viewModel.getPricing(tokenBearer, it, latitude, longitude ) }
        viewModel.listCost.observe(this, Observer { response->
            binding.tvRange.text=response.distance
        })
    }

    private fun isLocationPermissionGranted(): Boolean {
        return if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1001
            )
            false
        } else {
            true
        }
    }
}