package com.id.destinasyik.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.id.destinasyik.R
import com.id.destinasyik.databinding.FragmentOverviewBinding
import com.id.destinasyik.databinding.FragmentPricingBinding

class PricingFragment : Fragment() {
    private var _binding: FragmentPricingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPricingBinding.inflate(layoutInflater,container,false)
        return binding.root
    }
}