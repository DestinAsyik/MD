package com.id.destinasyik.ui.detail

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.id.destinasyik.R
import com.id.destinasyik.data.remote.response.ReccomPlace
import com.id.destinasyik.databinding.FragmentPricingBinding
import com.id.destinasyik.databinding.FragmentReviewBinding
import com.id.destinasyik.model.MainViewModel
import com.id.destinasyik.ui.recomended.RecommendedAdapter


class ReviewFragment : Fragment() {
    private var _binding: FragmentReviewBinding? = null
    private val binding get() = _binding!!
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var viewModel: MainViewModel
    private lateinit var location: Location
    private lateinit var reviewAdapter: ReviewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReviewBinding.inflate(layoutInflater,container,false)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        getLastLocation()
        setupRecycleView()
        loadData()
        return binding.root
    }

    private fun loadData(){
        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("token",null)
        val tokenBearer = "Bearer "+token
        val place: ReccomPlace? = requireActivity().intent.getParcelableExtra("PLACE") as? ReccomPlace
        place?.itemId?.let { viewModel.getReview(tokenBearer, it) }
        viewModel.placeReviews.observe(viewLifecycleOwner){response->
            Log.d("REVIEWS", "$response")
            reviewAdapter.submitList(response)
        }
        binding.btnSendReview.setOnClickListener {
            val rating = binding.inputRating.rating
            val review = binding.etReview.text.toString()
            place?.itemId?.let { it1 ->
                viewModel.addReview(tokenBearer,
                    it1, rating, review, location.latitude, location.longitude)
            }
        }
    }

    private fun setupRecycleView() {
        val recommendedRecyclerView = binding.rcReview
        reviewAdapter = ReviewAdapter()
        recommendedRecyclerView.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = reviewAdapter
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation(){
        if(isLocationPermissionGranted()){
            val lastLocation = fusedLocationProviderClient.lastLocation
            lastLocation.addOnSuccessListener {
                if(it != null){
                    location=it
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

}