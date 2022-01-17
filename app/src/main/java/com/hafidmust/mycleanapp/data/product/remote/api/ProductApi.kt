package com.hafidmust.mycleanapp.data.product.remote.api

import com.hafidmust.mycleanapp.data.common.utils.WrappedListResponse
import com.hafidmust.mycleanapp.data.common.utils.WrappedResponse
import com.hafidmust.mycleanapp.data.product.remote.dto.ProductCreateRequest
import com.hafidmust.mycleanapp.data.product.remote.dto.ProductResponse
import com.hafidmust.mycleanapp.data.product.remote.dto.ProductUpdateRequest
import retrofit2.Response
import retrofit2.http.*

interface ProductApi {
    @GET("product/")
    suspend fun getAllMyProduct() : Response<WrappedListResponse<ProductResponse>>

    @GET("product/{id}")
    suspend fun getProductById(
        @Path("id") id : String
    ) : Response<WrappedResponse<ProductResponse>>

    @PUT("product/{id}")
    suspend fun updateProduct(
        @Body productUpdateRequest: ProductUpdateRequest, @Path("id") id : String
    ) : Response<WrappedResponse<ProductResponse>>

    @DELETE("product/{id}")
    suspend fun deleteProduct(@Path("id") id : String) : Response<WrappedResponse<ProductResponse>>

    @POST("product/")
    suspend fun createProduct(@Body productCreateRequest: ProductCreateRequest) : Response<WrappedResponse<ProductResponse>>
}