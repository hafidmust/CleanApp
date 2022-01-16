package com.hafidmust.mycleanapp.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hafidmust.mycleanapp.data.common.utils.WrappedResponse
import com.hafidmust.mycleanapp.data.register.remote.dto.RegisterRequest
import com.hafidmust.mycleanapp.data.register.remote.dto.RegisterResponse
import com.hafidmust.mycleanapp.domain.common.base.BaseResult
import com.hafidmust.mycleanapp.domain.register.entity.RegisterEntity
import com.hafidmust.mycleanapp.domain.register.usecase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val registerUseCase: RegisterUseCase) : ViewModel() {
    private val state = MutableStateFlow<RegisterActivityState>(RegisterActivityState.init)
    val mState : StateFlow<RegisterActivityState> get() = state

    private fun setLoading(){
        state.value = RegisterActivityState.isLoading(true)
    }

    private fun hideLoading(){
        state.value = RegisterActivityState.isLoading(false)
    }
    private fun showToast(message: String){
        state.value = RegisterActivityState.showToast(message)
    }
    private fun successRegister(registerEntity: RegisterEntity){
        state.value = RegisterActivityState.successRegister(registerEntity)
    }
    private fun errorRegister(rawResponse: WrappedResponse<RegisterResponse>){
        state.value = RegisterActivityState.errorRegister(rawResponse)
    }
    fun register(registerRequest: RegisterRequest){
        viewModelScope.launch {
            registerUseCase.invoke(registerRequest)
                .onStart {
                    setLoading()
                }
                .catch {
                    hideLoading()
                    showToast(it.stackTraceToString())
                }
                .collect {
                    hideLoading()
                    when(it){
                        is BaseResult.Success ->{
                            successRegister(it.data)
                        }
                        is BaseResult.Error ->{
                            errorRegister(it.rawResponse)
                        }
                    }
                }
        }
    }

}

sealed class RegisterActivityState{
    object init: RegisterActivityState()
    data class isLoading(val isLoading : Boolean) : RegisterActivityState()
    data class showToast(val message : String) : RegisterActivityState()
    data class successRegister(val registerEntity : RegisterEntity) : RegisterActivityState()
    data class errorRegister(val rawResponse: WrappedResponse<RegisterResponse>) : RegisterActivityState()
}