package com.hafidmust.mycleanapp.domain.product.usecase

import com.hafidmust.mycleanapp.data.common.utils.WrappedListResponse
import com.hafidmust.mycleanapp.data.product.remote.dto.ProductResponse
import com.hafidmust.mycleanapp.domain.common.base.BaseResult
import com.hafidmust.mycleanapp.domain.product.ProductRepository
import com.hafidmust.mycleanapp.domain.product.entity.ProductEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllMyProductUseCase @Inject constructor(private val productRepository: ProductRepository){
    suspend fun invoke() : Flow<BaseResult<List<ProductEntity>, WrappedListResponse<ProductResponse>>>{
       return productRepository.getAllMyProduct()
    }
}