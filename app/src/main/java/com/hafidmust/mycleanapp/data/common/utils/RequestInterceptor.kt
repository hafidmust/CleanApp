package com.hafidmust.mycleanapp.data.common.utils

import com.hafidmust.mycleanapp.infrastructure.utils.SharedPrefs
import okhttp3.Interceptor
import okhttp3.Response

class RequestInterceptor(private val pref: SharedPrefs) : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = pref.getToken()
        val newReq = chain.request().newBuilder().addHeader("Authorization", token).build()
        return chain.proceed(newReq)
    }
}