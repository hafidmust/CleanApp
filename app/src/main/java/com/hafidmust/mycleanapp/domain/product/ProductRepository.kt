package com.hafidmust.mycleanapp.domain.product

import com.hafidmust.mycleanapp.data.common.utils.WrappedListResponse
import com.hafidmust.mycleanapp.data.common.utils.WrappedResponse
import com.hafidmust.mycleanapp.data.product.remote.dto.ProductCreateRequest
import com.hafidmust.mycleanapp.data.product.remote.dto.ProductResponse
import com.hafidmust.mycleanapp.data.product.remote.dto.ProductUpdateRequest
import com.hafidmust.mycleanapp.domain.common.base.BaseResult
import com.hafidmust.mycleanapp.domain.product.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getAllMyProduct() : Flow<BaseResult<List<ProductEntity>, WrappedListResponse<ProductResponse>>>
    suspend fun getProductById(id : String) : Flow<BaseResult<ProductEntity, WrappedResponse<ProductResponse>>>
    suspend fun updateProduct(productUpdateRequest: ProductUpdateRequest, id : String) : Flow<BaseResult<ProductEntity, WrappedResponse<ProductResponse>>>
    suspend fun deleteProductById(id : String) : Flow<BaseResult<Unit, WrappedResponse<ProductResponse>>>
    suspend fun createProduct(productCreateRequest: ProductCreateRequest) : Flow<BaseResult<ProductEntity, WrappedResponse<ProductResponse>>>
}