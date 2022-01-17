package com.hafidmust.mycleanapp.presentation.main.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.hafidmust.mycleanapp.R
import com.hafidmust.mycleanapp.data.product.remote.dto.ProductUpdateRequest
import com.hafidmust.mycleanapp.databinding.FragmentMainDetailBinding
import com.hafidmust.mycleanapp.domain.product.entity.ProductEntity
import com.hafidmust.mycleanapp.presentation.common.extension.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@AndroidEntryPoint
class DetailMainFragment : Fragment(R.layout.fragment_main_detail) {

    private var _binding: FragmentMainDetailBinding? = null

    private val binding get() = _binding!!

    private val viewModel : DetailMainViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMainDetailBinding.bind(view)
        update()
        delete()
        observe()
        fetchProductById()
    }
    private fun fetchProductById(){
        val id = arguments?.getInt("product_id") ?: 0
        viewModel.getProductById(id.toString())
    }
    private fun observe(){
        observeState()
        observeProduct()
    }
    private fun observeState(){
        viewModel.state.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { handleState(it) }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }
    private fun observeProduct(){
        viewModel.product.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { p -> handleProduct(p) }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }
    private fun handleProduct(product : ProductEntity?){
        product?.let {
            binding.productNameEditText.setText(it.name)
            binding.productPriceEditText.setText(it.price.toString())
        }
    }
    private fun handleState(state : DetailMainFragmentState){
        when(state){
            is DetailMainFragmentState.SuccessDelete -> findNavController().navigate(R.id.action_detail_to_home)
            is DetailMainFragmentState.SuccessUpdate -> findNavController().navigate(R.id.action_detail_to_home)
            is DetailMainFragmentState.IsLoading -> handleLoading(state.isLoading)
            is DetailMainFragmentState.ShowToast -> requireActivity().showToast(state.message)
            is DetailMainFragmentState.Init -> Unit
        }
    }

    private fun handleLoading(isLoading : Boolean){
        binding.updateButton.isEnabled = !isLoading
        binding.deleteButton.isEnabled = !isLoading
    }

    private fun update(){
        binding.updateButton.setOnClickListener {
            val name = binding.productNameEditText.text.toString().trim()
            val price = binding.productPriceEditText.textAlignment.toString().trim()
            if (validate(name, price)){
                val id = arguments?.getInt("product_id") ?:0
                viewModel.update(ProductUpdateRequest(name, price.toInt()), id.toString())
            }
        }
    }

    private fun delete(){
        binding.deleteButton.setOnClickListener {
            val id = arguments?.getInt("product_id") ?: 0
            if (id  != 0){
                viewModel.delete(id.toString())
            }
        }
    }

    private fun setNameError(e : String?){
        binding.productNameInput.error = e
    }
    private fun setPriceError(e: String?) {
        binding.productPriceInput.error = e
    }
    private fun resetAllError(){
        setNameError(null)
        setPriceError(null)
    }
    private fun validate(name : String, price: String) : Boolean{
        resetAllError()

        if (name.isEmpty()){
            setNameError(getString(R.string.error_product_name_not_valid))
            return false
        }
        if (price.toIntOrNull() == null){
            setPriceError(getString(R.string.error_price_not_valid))
            return false
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}