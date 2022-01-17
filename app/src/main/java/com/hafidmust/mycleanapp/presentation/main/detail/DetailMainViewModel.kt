package com.hafidmust.mycleanapp.presentation.main.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hafidmust.mycleanapp.data.product.remote.dto.ProductUpdateRequest
import com.hafidmust.mycleanapp.domain.common.base.BaseResult
import com.hafidmust.mycleanapp.domain.product.entity.ProductEntity
import com.hafidmust.mycleanapp.domain.product.usecase.DeleteProductByIdUseCase
import com.hafidmust.mycleanapp.domain.product.usecase.GetProductByIdUseCase
import com.hafidmust.mycleanapp.domain.product.usecase.UpdateProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailMainViewModel @Inject constructor(
    private val updateProductUseCase: UpdateProductUseCase,
    private val deleteProductByIdUseCase: DeleteProductByIdUseCase,
    private val getProductByIdUseCase: GetProductByIdUseCase
) : ViewModel() {
    private val _state = MutableStateFlow<DetailMainFragmentState>(DetailMainFragmentState.Init)
    val state : StateFlow<DetailMainFragmentState> get() = _state

    private val _product = MutableStateFlow<ProductEntity?>(null)
    val product : StateFlow<ProductEntity?> get() = _product

    private fun setLoading(isLoading: Boolean){
        _state.value = DetailMainFragmentState.IsLoading(isLoading)
    }

    private fun showToast(message: String){
        _state.value = DetailMainFragmentState.ShowToast(message)
    }

    fun getProductById(id : String){
        viewModelScope.launch {
            getProductByIdUseCase.invoke(id)
                .onStart { setLoading(true) }
                .catch { exception ->
                    setLoading(false)
                    showToast(exception.stackTraceToString())
                }
                .collect {
                    res ->
                    setLoading(false)
                    when(res){
                        is BaseResult.Success ->{
                            _product.value = res.data
                        }
                        is BaseResult.Error -> {
                            showToast(res.rawResponse.message)
                        }
                    }
                }
        }
    }
    fun update(productUpdateRequest: ProductUpdateRequest, id : String){
        viewModelScope.launch {
            updateProductUseCase.invoke(productUpdateRequest, id)
                .onStart { setLoading(true) }
                .catch { exec ->
                    showToast(exec.stackTraceToString())
                    setLoading(false)
                }
                .collect {
                    res ->
                    setLoading(false)
                    when(res){
                        is BaseResult.Success ->{
                            _state.value = DetailMainFragmentState.SuccessUpdate
                        }
                        is BaseResult.Error ->{
                            showToast(res.rawResponse.message)
                        }
                    }
                }
        }
    }
    fun delete(id : String){
        viewModelScope.launch {
            deleteProductByIdUseCase.invoke(id)
                .onStart { setLoading(true) }
                .catch { e ->
                    setLoading(false)
                    showToast(e.stackTraceToString())
                }
                .collect { res ->
                    setLoading(false)
                    when(res){
                        is BaseResult.Success ->{
                            _state.value = DetailMainFragmentState.SuccessDelete
                        }
                        is BaseResult.Error ->{
                            showToast(res.rawResponse.message)
                        }
                    }
                }
        }
    }
}

sealed class DetailMainFragmentState{
    object Init : DetailMainFragmentState()
    data class IsLoading(val isLoading : Boolean) : DetailMainFragmentState()
    object SuccessDelete : DetailMainFragmentState()
    object SuccessUpdate : DetailMainFragmentState()
    data class ShowToast(val message : String) : DetailMainFragmentState()
}