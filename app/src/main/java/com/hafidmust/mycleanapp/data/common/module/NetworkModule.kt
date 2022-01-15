package com.hafidmust.mycleanapp.data.common.module

import androidx.viewbinding.BuildConfig
import com.hafidmust.mycleanapp.BuildConfig.API_BASE_URL
import com.hafidmust.mycleanapp.data.common.utils.RequestInterceptor
import com.hafidmust.mycleanapp.infrastructure.utils.SharedPrefs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit(oktHttpClient: OkHttpClient) : Retrofit{
        return Retrofit.Builder().apply {
            addConverterFactory(GsonConverterFactory.create())
            client(oktHttpClient)
            baseUrl("https://golang-heroku.herokuapp.com/api/")
        }.build()
    }

    @Singleton
    @Provides
    fun provideHttpClient(requestInterceptor : RequestInterceptor): OkHttpClient{
        return OkHttpClient.Builder().apply {
            connectTimeout(60, TimeUnit.SECONDS)
            writeTimeout(60, TimeUnit.SECONDS)
            readTimeout(60, TimeUnit.SECONDS)
            addInterceptor(requestInterceptor)
        }.build()
    }

    @Provides
    fun provideRequestInterceptor(pref: SharedPrefs) : RequestInterceptor{
        return RequestInterceptor(pref)
    }
}