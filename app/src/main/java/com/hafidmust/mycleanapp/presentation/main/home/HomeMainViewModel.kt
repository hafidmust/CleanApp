package com.hafidmust.mycleanapp.presentation.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hafidmust.mycleanapp.data.common.utils.WrappedResponse
import com.hafidmust.mycleanapp.data.login.remote.dto.LoginResponse
import com.hafidmust.mycleanapp.data.product.remote.dto.ProductResponse
import com.hafidmust.mycleanapp.domain.common.base.BaseResult
import com.hafidmust.mycleanapp.domain.login.entity.LoginEntity
import com.hafidmust.mycleanapp.domain.product.entity.ProductEntity
import com.hafidmust.mycleanapp.domain.product.usecase.GetAllMyProductUseCase
import com.hafidmust.mycleanapp.presentation.login.LoginActivityState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeMainViewModel @Inject constructor(private val getAllMyProductUseCase: GetAllMyProductUseCase) : ViewModel() {
    private val state = MutableStateFlow<HomeMainFragmentState>(HomeMainFragmentState.Init)
    val mState : StateFlow<HomeMainFragmentState> get() = state

    private val products = MutableStateFlow<List<ProductEntity>>(mutableListOf())
    val mProducts : StateFlow<List<ProductEntity>> get() = products

    init {
        fetchAllMyProduct()
    }

    private fun setLoading(){
        state.value = HomeMainFragmentState.IsLoading(true)
    }
    private fun hideLoading(){
        state.value = HomeMainFragmentState.IsLoading(false)
    }
    private fun showToast(message : String){
        state.value = HomeMainFragmentState.ShowToast(message)
    }
    fun fetchAllMyProduct(){
        viewModelScope.launch {
            getAllMyProductUseCase.invoke()
                .onStart {
                    setLoading()
                }
                .catch { exception ->
                    hideLoading()
                    showToast(exception.stackTraceToString())
                }
                .collect {  result ->
                    hideLoading()
                    when(result){
                        is BaseResult.Success ->{
                            products.value = result.data
                        }
                        is BaseResult.Error ->{
                            showToast(result.rawResponse.message)
                        }

                    }
                }
        }
    }

}
sealed class HomeMainFragmentState{
    object Init : HomeMainFragmentState()
    data class IsLoading(val isLoading : Boolean) : HomeMainFragmentState()
    data class ShowToast(val message: String) : HomeMainFragmentState()
}