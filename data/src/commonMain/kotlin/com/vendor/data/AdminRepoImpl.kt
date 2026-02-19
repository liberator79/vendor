package com.vendor.data

import com.vendor.data.domain.AdminRepository
import com.vendor.shared.domain.Product
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.storage.File
import dev.gitlive.firebase.storage.storage
import kotlinx.coroutines.withTimeout
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class AdminRepoImpl : AdminRepository {
    override fun getCurrentUser(): String? = Firebase.auth.currentUser?.uid

    override suspend fun createNewProduct(
        product: Product,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try{
            val currentUserId = getCurrentUser()
            if(currentUserId != null) {
                val firestore = Firebase.firestore
                val productCollection = firestore.collection("products")
                productCollection.document(product.id).set(product)
                onSuccess()
            }else{
                onError("User is not available")
            }
        }catch(e : Exception){
            onError("Error while creating a new product : ${e.message}")
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun uploadImageToStorage(file: File): String? {
        return if(getCurrentUser() != null){
            val storage = Firebase.storage.reference
            val imagePath = storage.child(path = "images/${Uuid.random().toHexString()}")
            try {
                withTimeout(
                    timeMillis = 20000
                ){
                    imagePath.putFile(file)
                    imagePath.getDownloadUrl()
                }
            }catch(e : Exception){
                null
            }
        }else null
    }

}