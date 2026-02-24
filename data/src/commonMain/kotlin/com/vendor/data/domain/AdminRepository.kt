package com.vendor.data.domain

import com.vendor.shared.domain.Product
import com.vendor.shared.util.RequestState
import dev.gitlive.firebase.storage.File
import kotlinx.coroutines.flow.Flow

interface AdminRepository {
    fun getCurrentUser() : String?
    suspend fun createNewProduct(
        product : Product,
        onSuccess : () -> Unit,
        onError : (String) -> Unit
    )
    suspend fun uploadImageToStorage(
        file : ByteArray
    ) : String?

    fun readLatestTenProduct() : Flow<RequestState<List<Product>>>

    suspend fun getProductById(productId : String) : RequestState<Product>

    suspend fun updateThumbnail(
        productId : String,
        downloadUrl : String,
        onSuccess : () -> Unit,
        onError : (String) -> Unit
    )

    suspend fun updateProduct(
        product: Product,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    )

    suspend fun deleteProduct(
        productId : String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    )

    fun searchProductsByTitle(
        searchQuery : String
    ) : Flow<RequestState<List<Product>>>
}