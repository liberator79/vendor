package com.vendor.data

import com.vendor.data.domain.ProductsRepository
import com.vendor.shared.domain.Product
import com.vendor.shared.util.RequestState
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest

class ProductsRepositoryImpl : ProductsRepository{
    override fun getCurrentUser(): String? = Firebase.auth.currentUser?.uid

    override fun readDiscountedProducts(): Flow<RequestState<List<Product>>> = channelFlow {
            try {
                val userId = getCurrentUser()
                if(userId != null) {
                    val database = Firebase.firestore
                    database.collection(collectionPath = "products")
                        .where { "isDiscounted" equalTo true }
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

    override fun readNewProducts(): Flow<RequestState<List<Product>>> = channelFlow  {
        try {
            val userId = getCurrentUser()
            if(userId != null) {
                val database = Firebase.firestore
                database.collection(collectionPath = "products")
                    .where { "isNew" equalTo true }
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
}