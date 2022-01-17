package com.hafidmust.mycleanapp.data.product.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hafidmust.mycleanapp.data.common.utils.WrappedListResponse
import com.hafidmust.mycleanapp.data.common.utils.WrappedResponse
import com.hafidmust.mycleanapp.data.login.remote.dto.LoginResponse
import com.hafidmust.mycleanapp.data.product.remote.api.ProductApi
import com.hafidmust.mycleanapp.data.product.remote.dto.ProductCreateRequest
import com.hafidmust.mycleanapp.data.product.remote.dto.ProductResponse
import com.hafidmust.mycleanapp.data.product.remote.dto.ProductUpdateRequest
import com.hafidmust.mycleanapp.domain.common.base.BaseResult
import com.hafidmust.mycleanapp.domain.product.ProductRepository
import com.hafidmust.mycleanapp.domain.product.entity.ProductEntity
import com.hafidmust.mycleanapp.domain.product.entity.ProductUserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductRespositoryImpl @Inject constructor(private val productApi : ProductApi) : ProductRepository {
    override suspend fun getAllMyProduct(): Flow<BaseResult<List<ProductEntity>, WrappedListResponse<ProductResponse>>> {
        return flow {
            val response = productApi.getAllMyProduct()
            if (response.isSuccessful){
                val body = response.body()
                val products = mutableListOf<ProductEntity>()
                var user : ProductUserEntity?
                body?.data?.forEach { product ->
                    user = ProductUserEntity(product.user.id, product.user.name, product.user.email)
                    products.add(
                        ProductEntity(
                            product.id, product.name, product.price, user!!
                        )
                    )
                }
                emit(BaseResult.Success(products))
            }else{
                val type = object : TypeToken<WrappedListResponse<ProductResponse>>(){}.type
                val err : WrappedListResponse<ProductResponse> = Gson().fromJson(response.errorBody()?.charStream(), type)
                err.code = response.code()
                emit(BaseResult.Error(err))
            }
        }
    }

    override suspend fun getProductById(id: String): Flow<BaseResult<ProductEntity, WrappedResponse<ProductResponse>>> {
        return flow {
            val response = productApi.getProductById(id)
            if (response.isSuccessful){
                val body = response.body()
                val user = ProductUserEntity(body?.data?.user?.id!!, body.data?.user?.name!!, body.data?.user?.email!!)
                val product = ProductEntity(body.data?.id!!, body.data?.name!!, body.data?.price!!, user)
                emit(BaseResult.Success(product))
            }else{
                val type = object : TypeToken<WrappedResponse<ProductResponse>>(){}.type
                val err : WrappedResponse<ProductResponse> = Gson().fromJson(response.errorBody()?.charStream(), type)
                err.code = response.code()
                emit(BaseResult.Error(err))
            }
        }
    }

    override suspend fun updateProduct(productUpdateRequest: ProductUpdateRequest, id : String): Flow<BaseResult<ProductEntity, WrappedResponse<ProductResponse>>> {
        return flow {
            val response = productApi.updateProduct(productUpdateRequest, id)
            if (response.isSuccessful){
                val body = response.body()
                val user = ProductUserEntity(body?.data?.user?.id!!, body.data?.user?.name!!, body.data?.user?.email!!)
                val product = ProductEntity(body.data?.id!!, body.data?.name!!, body.data?.price!!, user)
                emit(BaseResult.Success(product))
            }else{
                val type = object : TypeToken<WrappedResponse<ProductResponse>>(){}.type
                val err : WrappedResponse<ProductResponse> = Gson().fromJson(response.errorBody()?.charStream(), type)
                err.code = response.code()
                emit(BaseResult.Error(err))
            }
        }
    }

    override suspend fun deleteProductById(id: String): Flow<BaseResult<Unit, WrappedResponse<ProductResponse>>> {
        return flow {
            val response = productApi.deleteProduct(id)
            if (response.isSuccessful){
                emit(BaseResult.Success(Unit))
            }else{
                val type = object : TypeToken<WrappedResponse<ProductResponse>>(){}.type
                val err : WrappedResponse<ProductResponse> = Gson().fromJson(response.errorBody()?.charStream(), type)
                err.code = response.code()
                emit(BaseResult.Error(err))
            }
        }
    }

    override suspend fun createProduct(productCreateRequest: ProductCreateRequest): Flow<BaseResult<ProductEntity, WrappedResponse<ProductResponse>>> {
        return flow {
            val response = productApi.createProduct(productCreateRequest)
            if (response.isSuccessful){
                val body = response.body()
                val user = ProductUserEntity(body?.data?.user?.id!!, body.data?.user?.name!!, body.data?.user?.email!!)
                val product = ProductEntity(body.data?.id!!, body.data?.name!!, body.data?.price!!, user)
                emit(BaseResult.Success(product))
            }else{
                val type = object : TypeToken<WrappedResponse<ProductResponse>>(){}.type
                val err : WrappedResponse<ProductResponse> = Gson().fromJson(response.errorBody()?.charStream(), type)
                err.code = response.code()
                emit(BaseResult.Error(err))
            }
        }
    }
}