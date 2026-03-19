package com.vendor.data.domain

import com.vendor.shared.domain.Product
import com.vendor.shared.util.RequestState
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {
    fun getCurrentUser() : String?
    fun readDiscountedProducts() : Flow<RequestState<List<Product>>>
    fun readNewProducts() : Flow<RequestState<List<Product>>>
}