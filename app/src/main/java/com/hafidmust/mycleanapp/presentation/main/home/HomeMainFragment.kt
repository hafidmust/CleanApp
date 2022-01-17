package com.hafidmust.mycleanapp.presentation.main.home

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hafidmust.mycleanapp.R
import com.hafidmust.mycleanapp.databinding.FragmentMainHomeBinding
import com.hafidmust.mycleanapp.domain.product.entity.ProductEntity
import com.hafidmust.mycleanapp.presentation.common.extension.gone
import com.hafidmust.mycleanapp.presentation.common.extension.showToast
import com.hafidmust.mycleanapp.presentation.common.extension.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class HomeMainFragment : Fragment(R.layout.fragment_main_home) {

    private var _binding: FragmentMainHomeBinding? = null


    private val binding get() = _binding!!
    private val viewModel : HomeMainViewModel by viewModels()



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMainHomeBinding.bind(view)
        setupRecyclerView()
        observe()
        fetchMyProducts()

    }

    private fun fetchMyProducts(){
        viewModel.fetchAllMyProduct()
    }

    private fun setupRecyclerView(){
        val mAdapter = HomeProductAdapter(mutableListOf())
        mAdapter.setItemTap(object : HomeProductAdapter.onItemTap{
            override fun onTap(product: ProductEntity) {
                val bundle = bundleOf("product_id" to product.id)
                findNavController().navigate(R.id.action_home_to_detail, bundle)
            }
        })
        binding.rvProduct.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireActivity())
        }
    }

    private fun observeState(){
        viewModel.mState.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { state -> handleState(state) }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observeProducts(){
        viewModel.mProducts.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { products -> handleProducts(products) }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observe(){
        observeState()
        observeProducts()
    }
    private fun handleState(state: HomeMainFragmentState){
        when(state){
            is HomeMainFragmentState.ShowToast -> requireActivity().showToast(state.message)
            is HomeMainFragmentState.IsLoading -> handleLoading(state.isLoading)
            is HomeMainFragmentState.Init -> Unit
        }
    }
    private fun handleLoading(isLoading: Boolean){
        if (isLoading){
            binding.pbarLoading.visibility = View.VISIBLE
        }else{
            binding.pbarLoading.visibility = View.GONE
        }
    }
    private fun handleProducts(products : List<ProductEntity>){
        binding.rvProduct.adapter?.let { adapter ->
            if (adapter is HomeProductAdapter){
                adapter.updateList(products)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}