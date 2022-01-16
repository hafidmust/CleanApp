package com.hafidmust.mycleanapp.domain.register.usecase

import com.hafidmust.mycleanapp.data.common.utils.WrappedResponse
import com.hafidmust.mycleanapp.data.register.remote.dto.RegisterRequest
import com.hafidmust.mycleanapp.data.register.remote.dto.RegisterResponse
import com.hafidmust.mycleanapp.domain.common.base.BaseResult
import com.hafidmust.mycleanapp.domain.register.RegisterRepository
import com.hafidmust.mycleanapp.domain.register.entity.RegisterEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegisterUseCase @Inject constructor(private val registerRepository: RegisterRepository) {
    suspend fun invoke(registerRequest: RegisterRequest) : Flow<BaseResult<RegisterEntity, WrappedResponse<RegisterResponse>>>{
        return registerRepository.register(registerRequest)
    }
}