package com.id.destinasyik.ui.recomended

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.id.destinasyik.data.repository.PlaceRepository
import com.id.destinasyik.databinding.FragmentNearestPlaceBinding
import com.id.destinasyik.model.MainViewModel

class NearestPlaceFragment : Fragment() {
    private var _binding: FragmentNearestPlaceBinding? = null
    private val binding get() = _binding!!
    private lateinit var recommendedAdapter: RecommendedAdapter
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNearestPlaceBinding.inflate(layoutInflater, container, false)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        setupRecommendedRecyclerView()
        getLastLocation()
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

    private fun loadData(latitude: Double, longitude: Double) {
        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("token",null)
        val tokenBearer = "Bearer "+token
        viewModel.recommNearby(tokenBearer,latitude,longitude)
        viewModel.placeReccomNearby.observe(viewLifecycleOwner){ place->
            Log.d("Reccom Nearby","$place")
            recommendedAdapter.submitList(place)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation(){
        if(isLocationPermissionGranted()){
            val lastLocation = fusedLocationProviderClient.lastLocation
            lastLocation.addOnSuccessListener {
                if(it != null){
                    loadData(it.latitude,it.longitude)
                    Log.d("Current Loc","Latitude : ${it.latitude}, Longitude : ${it.longitude}")
                }
            }

            lastLocation.addOnFailureListener {
                Log.d("Current Loc","Failed to get Current Location")
            }
        }
    }


    private fun isLocationPermissionGranted(): Boolean {
        return if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
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

    private fun loadingPage(isLoading: Boolean) {
        binding.loadingBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}