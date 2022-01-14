package com.hafidmust.mycleanapp.domain.login.usecase

import com.hafidmust.mycleanapp.data.common.utils.WrappedResponse
import com.hafidmust.mycleanapp.data.login.remote.dto.LoginRequest
import com.hafidmust.mycleanapp.data.login.remote.dto.LoginResponse
import com.hafidmust.mycleanapp.domain.common.base.BaseResult
import com.hafidmust.mycleanapp.domain.login.LoginRepository
import com.hafidmust.mycleanapp.domain.login.entity.LoginEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val loginRepository: LoginRepository) {
    suspend fun invoke(loginRequest: LoginRequest) : Flow<BaseResult<LoginEntity, WrappedResponse<LoginResponse>>>{
        return loginRepository.login(loginRequest)
    }
}