package com.id.destinasyik.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.id.destinasyik.R
import com.id.destinasyik.data.remote.response.ReccomPlace
import com.id.destinasyik.databinding.FragmentHomeBinding
import com.id.destinasyik.databinding.FragmentOverviewBinding


class OverviewFragment : Fragment() {
    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOverviewBinding.inflate(layoutInflater,container,false)
        val place: ReccomPlace? = requireActivity().intent.getParcelableExtra("PLACE") as? ReccomPlace
        binding.tvOverview.text=place?.description
        binding.buttonBook.setOnClickListener {
            Toast.makeText(requireContext(),"Still Under Development!",Toast.LENGTH_SHORT).show()
        }
        return binding.root
    }

}