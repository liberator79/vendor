package com.vendor.data

import com.vendor.data.domain.AdminRepository
import com.vendor.shared.domain.Product
import com.vendor.shared.util.RequestState
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.Direction
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.storage.File
import dev.gitlive.firebase.storage.storage
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.timeout
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.statement.bodyAsText
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.InternalAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext
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
                productCollection.document(product.id).set(product.copy(title = product.title.lowercase()))
                onSuccess()
            }else{
                onError("User is not available")
            }
        }catch(e : Exception){
            onError("Error while creating a new product : ${e.message}")
        }
    }

    @OptIn(ExperimentalUuidApi::class, InternalAPI::class)
    override suspend fun uploadImageToStorage(file: ByteArray): String? {
        if (getCurrentUser() == null) return null

        val client = HttpClient{}
        val cloudName = "dwv4kw7go"
        val uploadPreset = "vendor"

        return try {
            withContext(Dispatchers.IO) {
                val response = client.submitFormWithBinaryData(
                    url = "https://api.cloudinary.com/v1_1/$cloudName/image/upload",
                    formData = formData {
                        append("\"upload_preset\"", "vendor")
                        append("\"file\"", file, Headers.build {
                            append(HttpHeaders.ContentType, "image/jpeg")
                            append(HttpHeaders.ContentDisposition, "filename=\"image.jpg\"")
                        })
                    }
                )

                if (response.status == HttpStatusCode.OK) {
                    val body = response.bodyAsText()
                    body.split("\"secure_url\":\"")[1].split("\"")[0]
                } else {
                    println("Upload failed with status: ${response.status} ${response.bodyAsText()}")
                    null
                }
            }
        } catch (e: Exception) {
            println("CRASH MESSAGE: ${e.message}")
            null
        } finally {
            client.close()
        }
    }

    override fun readLatestTenProduct(): Flow<RequestState<List<Product>>> = channelFlow {
        try {
            val userId = getCurrentUser()
            if(userId != null) {
                val database = Firebase.firestore
                database.collection(collectionPath = "products")
                    .orderBy("createdAt", Direction.DESCENDING)
                    .limit(10)
                    .snapshots
                    .collectLatest {query ->
                        val products = query.documents.map {document ->
                            Product(
                                id = document.id,
                               // title = (document.get("title") as? String)?.uppercase() ?: "UNKNOWN TITLE",
                                title = document.get(field = "title"),
                                description = document.get(field = "description"),
                                thumbnail = document.get(field = "thumbnail"),
                                category = document.get(field = "category"),
                                flavours = document.get(field = "flavours"),
                                weight = document.get(field = "weight"),
                                price = document.get(field = "price"),
                                isPopular = document.get(field = "isPopular"),
                                isNew = document.get(field = "isNew"),
                                isDiscounted = document.get(field = "isDiscounted"),
                                createdAt = document.get(field = "createdAt")
                            )
                        }
                        send(RequestState.Success(data = products))
                    }
            }else{
                send(RequestState.Error("User is not available"))
            }
        }catch (e : Exception){
            send(RequestState.Error("Error while reading products : ${e.message}"))
        }
    }

    override suspend fun getProductById(productId : String): RequestState<Product> {
       return try{
           val userId = getCurrentUser()
           if(userId != null){
               val database = Firebase.firestore
               val productDetails = database.collection(collectionPath = "products")
                   .document(productId)
                   .get()
               if(productDetails.exists){
                    val product = Product(
                        id = productDetails.id,
                        title = productDetails.get(field = "title"),
                        description = productDetails.get(field = "description"),
                        thumbnail = productDetails.get(field = "thumbnail"),
                        category = productDetails.get(field = "category"),
                        flavours = productDetails.get(field = "flavours"),
                        weight = productDetails.get(field = "weight"),
                        price = productDetails.get(field = "price"),
                        isPopular = productDetails.get(field = "isPopular"),
                        isNew = productDetails.get(field = "isNew"),
                        isDiscounted = productDetails.get(field = "isDiscounted"),
                        createdAt = productDetails.get(field = "createdAt")
                    )
                   RequestState.Success(data = product.copy(title = product.title.uppercase()))
               }else{
                   RequestState.Error("Product is not available")
               }
           }else{
               RequestState.Error("User is not available")
           }
       }catch (e : Exception){
           RequestState.Error("Error while reading products : ${e.message}")
       }
    }

    override suspend fun updateThumbnail(
        productId: String,
        downloadUrl: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try{
            val userId = getCurrentUser()
            if(userId != null){
                val database = Firebase.firestore
                val productCollection = database.collection("products")
                val existingProduct = productCollection
                    .document(productId)
                    .get()
                if(existingProduct.exists) {
                    productCollection
                        .document(productId)
                        .update(
                                "thumbnail" to downloadUrl
                        )
                    onSuccess()
                }else{
                    onError("Product not found")
                }
                onSuccess()
            }else{
                onError("User not found")
            }
        }catch(e : Exception){
            onError("Error while updating Image")
        }
    }

    override suspend fun updateProduct(
        product: Product,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try{
            val userId = getCurrentUser()
            if(userId != null){
                println("UpdatingFirestore ${product}")
                val database = Firebase.firestore
                val productCollection = database.collection("products")
                val existingProduct = productCollection
                    .document(product.id)
                    .get()
                if(existingProduct.exists) {
                    productCollection
                        .document(product.id)
                        .update(product.copy(title = product.title.lowercase()))
                    onSuccess()
                }else{
                    onError("Product not found")
                }
                //onSuccess()
            }else{
                onError("User not found")
            }
        }catch(e : Exception){
            onError("Error while updating Image")
        }
    }

    override suspend fun deleteProduct(
        productId: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try{
            val userId = getCurrentUser()
            if(userId != null){
                val database = Firebase.firestore
                val productCollection = database.collection("products")
                val productExist = productCollection.document(productId).get()
                if(productExist.exists){
                    productCollection
                        .document(productId)
                        .delete()
                    onSuccess()
                }else{
                    onError("Product not found")
                }
            }else{
                onError("User not found")
            }
        }catch(e : Exception){
            onError("Error while Deleting Product ${e.message}")
        }
    }

    override fun searchProductsByTitle(searchQuery: String): Flow<RequestState<List<Product>>> = channelFlow {
        try {
            val userId = getCurrentUser()
            if(userId != null){
                val database = Firebase.firestore
                val query = searchQuery.trim().lowercase()
                val endText = query + "\uf8ff"
                database.collection(collectionPath = "products")
                    .orderBy("title")
                    .startAt(query)
                    .endAt(endText)
                    .snapshots
                    .collectLatest { query ->
                        val products = query.documents.map { document ->
                            Product(
                                id = document.id,
                                title = document.get(field = "title"),
                                description = document.get("description"),
                                thumbnail = document.get(field = "thumbnail"),
                                category = document.get(field = "category"),
                                flavours = document.get(field = "flavours"),
                                weight = document.get(field = "weight"),
                                price = document.get(field = "price"),
                                isPopular = document.get(field = "isPopular"),
                                isNew = document.get(field = "isNew"),
                                isDiscounted = document.get(field = "isDiscounted"),
                                createdAt = document.get(field = "createdAt")
                            )
                        }
                        send(RequestState.Success(products))
                    }
            }else{
                send(RequestState.Error("User is not available"))
            }
        }catch (
            e : Exception
        ){
            send(RequestState.Error("Error while searching products : ${e.message}"))
        }
    }

}