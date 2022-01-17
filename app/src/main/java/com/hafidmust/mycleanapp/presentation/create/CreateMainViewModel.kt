package com.hafidmust.mycleanapp.presentation.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hafidmust.mycleanapp.data.product.remote.dto.ProductCreateRequest
import com.hafidmust.mycleanapp.domain.common.base.BaseResult
import com.hafidmust.mycleanapp.domain.product.usecase.CreateProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateMainViewModel @Inject constructor(
    private val createProductUseCase: CreateProductUseCase
) : ViewModel() {
    private val _state = MutableStateFlow<CreateMainFragmentState>(CreateMainFragmentState.Init)
    val state : StateFlow<CreateMainFragmentState> get() = _state

    private fun setLoading(isLoading: Boolean){
        _state.value = CreateMainFragmentState.IsLoading(isLoading)
    }

    private fun showToast(message: String){
        _state.value = CreateMainFragmentState.ShowToast(message)
    }

    fun createProduct(productCreateRequest: ProductCreateRequest){
        viewModelScope.launch {
            createProductUseCase.invoke(productCreateRequest)
                .onStart { setLoading(true) }
                .catch { e ->
                    setLoading(false)
                    showToast(e.stackTraceToString())
                }
                .collect { res ->
                    setLoading(false)
                    when(res){
                        is BaseResult.Success -> {
                            _state.value = CreateMainFragmentState.SuccessCreate
                        }
                        is BaseResult.Error -> {
                            showToast(res.rawResponse.message)
                        }
                    }
                }
        }
    }
}

sealed class CreateMainFragmentState {
    object Init: CreateMainFragmentState()
    object SuccessCreate : CreateMainFragmentState()
    data class IsLoading(val isLoading: Boolean) : CreateMainFragmentState()
    data class ShowToast(val message: String) : CreateMainFragmentState()
}