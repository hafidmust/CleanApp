package com.hafidmust.mycleanapp.data.product

import com.hafidmust.mycleanapp.data.common.module.NetworkModule
import com.hafidmust.mycleanapp.data.product.remote.api.ProductApi
import com.hafidmust.mycleanapp.data.product.repository.ProductRespositoryImpl
import com.hafidmust.mycleanapp.domain.product.ProductRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class ProductModule {
    @Singleton
    @Provides
    fun provideProductApi(retrofit : Retrofit) : ProductApi{
        return retrofit.create(ProductApi::class.java)
    }

    @Singleton
    @Provides
    fun provideProductRepository(productApi : ProductApi) : ProductRepository{
        return ProductRespositoryImpl(productApi)
    }
}