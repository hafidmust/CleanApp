package com.hafidmust.mycleanapp.domain.login

import com.hafidmust.mycleanapp.data.common.utils.WrappedResponse
import com.hafidmust.mycleanapp.data.login.remote.dto.LoginRequest
import com.hafidmust.mycleanapp.data.login.remote.dto.LoginResponse
import com.hafidmust.mycleanapp.domain.common.base.BaseResult
import com.hafidmust.mycleanapp.domain.login.entity.LoginEntity
import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    suspend fun login(loginRequest: LoginRequest) : Flow<BaseResult<LoginEntity, WrappedResponse<LoginResponse>>>
}