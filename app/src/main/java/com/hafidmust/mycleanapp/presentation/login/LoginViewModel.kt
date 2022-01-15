package com.hafidmust.mycleanapp.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hafidmust.mycleanapp.data.common.utils.WrappedResponse
import com.hafidmust.mycleanapp.data.login.remote.dto.LoginRequest
import com.hafidmust.mycleanapp.data.login.remote.dto.LoginResponse
import com.hafidmust.mycleanapp.domain.common.base.BaseResult
import com.hafidmust.mycleanapp.domain.login.entity.LoginEntity
import com.hafidmust.mycleanapp.domain.login.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUseCase : LoginUseCase) : ViewModel() {
    private val state = MutableStateFlow<LoginActivityState>(LoginActivityState.Init)
    val mState : StateFlow<LoginActivityState> get() = state

    private fun setLoading(){
        state.value = LoginActivityState.isLoading(true)
    }
    private fun hideLoading(){
        state.value = LoginActivityState.isLoading(false)
    }
    private fun showToast(message: String){
        state.value = LoginActivityState.showToast(message)
    }
    private fun successLogin(loginEntity: LoginEntity){
        state.value = LoginActivityState.successLogin(loginEntity)
    }
    private fun errorLogin(rawResponse: WrappedResponse<LoginResponse>){
        state.value = LoginActivityState.errorLogin(rawResponse)
    }
    fun login(loginRequest: LoginRequest){
        viewModelScope.launch {
            loginUseCase.invoke(loginRequest)
                .onStart {
                    setLoading()
                }
                .catch { exception ->
                    hideLoading()
                    showToast(exception.stackTraceToString())
                }
                .collect { result ->
                    hideLoading()
                    when(result){
                        is BaseResult.Success -> successLogin(result.data)
                        is BaseResult.Error -> errorLogin(result.rawResponse)
                    }

                }
        }
    }
}
sealed class LoginActivityState{
    object Init : LoginActivityState()
    data class isLoading(val isLoading : Boolean) : LoginActivityState()
    data class showToast(val message: String) : LoginActivityState()
    data class successLogin(val loginEntity: LoginEntity) : LoginActivityState()
    data class errorLogin(val rawResponse: WrappedResponse<LoginResponse>) : LoginActivityState()
}