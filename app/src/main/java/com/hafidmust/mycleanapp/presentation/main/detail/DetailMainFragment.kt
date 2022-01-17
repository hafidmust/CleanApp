package com.hafidmust.mycleanapp.presentation.main.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.hafidmust.mycleanapp.R
import com.hafidmust.mycleanapp.databinding.FragmentMainDetailBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class DetailMainFragment : Fragment(R.layout.fragment_main_detail) {

    private var _binding: FragmentMainDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMainDetailBinding.bind(view)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}