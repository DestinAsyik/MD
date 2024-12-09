package com.id.destinasyik.ui.detail

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.id.destinasyik.R
import com.id.destinasyik.data.remote.response.ReccomPlace
import com.id.destinasyik.databinding.FragmentOverviewBinding
import com.id.destinasyik.databinding.FragmentPricingBinding
import com.id.destinasyik.model.MainViewModel
import java.text.NumberFormat
import java.util.Locale

class PricingFragment : Fragment() {
    private var _binding: FragmentPricingBinding? = null
    private val binding get() = _binding!!
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPricingBinding.inflate(layoutInflater,container,false)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        getLastLocation()
        return binding.root
    }

    private fun loadData(latitude: Double, longitude: Double) {
        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("token",null)
        val tokenBearer = "Bearer "+token
        val place: ReccomPlace? = requireActivity().intent.getParcelableExtra("PLACE") as? ReccomPlace
        place?.itemId?.let { viewModel.getPricing(tokenBearer, it, latitude, longitude ) }
        viewModel.listCost.observe(viewLifecycleOwner){response->
            binding.ticketPrice.text= response.ticketPrice
            binding.tvPertalitePrice.text= response.fuelDetails?.get(0)?.fuelCost
            binding.tvPertamaxPrice.text= response.fuelDetails?.get(1)?.fuelCost
            binding.tvSolarPrice.text= response.fuelDetails?.get(2)?.fuelCost
            val distancePlace = response.distance
            binding.solarRange.text=distancePlace
            binding.pertaliteRange.text=distancePlace
            binding.pertamaxRange.text=distancePlace
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
}