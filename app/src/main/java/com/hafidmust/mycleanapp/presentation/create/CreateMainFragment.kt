package com.hafidmust.mycleanapp.presentation.create

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.hafidmust.mycleanapp.R
import com.hafidmust.mycleanapp.data.product.remote.dto.ProductCreateRequest
import com.hafidmust.mycleanapp.databinding.FragmentMainCreateBinding
import com.hafidmust.mycleanapp.presentation.common.extension.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class CreateMainFragment : Fragment(R.layout.fragment_main_create) {

    private var _binding : FragmentMainCreateBinding? = null
    private val binding get() = _binding!!

    private val viewModel : CreateMainViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMainCreateBinding.bind(view)
        observe()
        create()
    }
    private fun create(){
        binding.saveButton.setOnClickListener {
            val name = binding.productNameEditText.text.toString().trim()
            val price = binding.productPriceEditText.text.toString().trim()

            if (validate(name, price)){
                viewModel.createProduct(ProductCreateRequest(name, price.toInt()))
            }
        }
    }
    private fun observe(){
        viewModel.state.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { handleState(it) }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }
    private fun handleState(state : CreateMainFragmentState){
        when(state){
            is CreateMainFragmentState.IsLoading -> handleLoading(state.isLoading)
            is CreateMainFragmentState.SuccessCreate -> findNavController().navigate(R.id.action_create_to_home)
            is CreateMainFragmentState.ShowToast -> requireActivity().showToast(state.message)
            is CreateMainFragmentState.Init -> Unit
        }
    }
    private fun handleLoading(isLoading : Boolean){
        binding.saveButton.isEnabled = !isLoading
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}