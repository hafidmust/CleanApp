package com.hafidmust.mycleanapp.data.login.remote.api

import com.hafidmust.mycleanapp.data.common.utils.WrappedResponse
import com.hafidmust.mycleanapp.data.login.remote.dto.LoginRequest
import com.hafidmust.mycleanapp.data.login.remote.dto.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {
    @POST("auth/login")
    suspend fun login(
        @Body
        loginRequest: LoginRequest
    ) : Response<WrappedResponse<LoginResponse>>
}