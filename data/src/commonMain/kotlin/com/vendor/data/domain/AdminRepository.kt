package com.vendor.data.domain

import com.vendor.shared.domain.Product
import dev.gitlive.firebase.storage.File

interface AdminRepository {
    fun getCurrentUser() : String?
    suspend fun createNewProduct(
        product : Product,
        onSuccess : () -> Unit,
        onError : (String) -> Unit
    )
    suspend fun uploadImageToStorage(
        file : File
    ) : String?
}