package com.vendor.data

import com.vendor.data.domain.CustomerRepository
import com.vendor.shared.domain.Customer
import com.vendor.shared.util.RequestState
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest

class CutomerRepositoryImpl : CustomerRepository {
    override suspend fun createCustomer(
        user: FirebaseUser?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try{
            if(user != null){
                val customerCollection = Firebase.firestore.collection(collectionPath = "customer")
                val customer = Customer(
                    id  = user.uid,
                    firstName = user.displayName?.split(" ")?.firstOrNull() ?: "Unkown",
                    lastName = user.displayName?.split(" ")?.lastOrNull() ?: "Unkown",
                    email = user.email ?: "Unkown"
                )

                val customerExists = customerCollection.document(user.uid).get().exists
                if(customerExists){
                    onSuccess()
                }else{
                    customerCollection.document(user.uid).set(customer)
                    onSuccess();
                }
            }else{
                onError("User not found")
            }
        }catch (error : Exception){
            onError("Error while creating Customer : ${error.message}")
        }
    }

    override fun getCurrentUserId(): String? {
        return Firebase.auth.currentUser?.uid
    }

    override suspend fun signout() : RequestState<Unit> {
        return try{
            Firebase.auth.signOut()
            RequestState.Success(data = Unit)
        }catch (error : Exception) {
            RequestState.Error("Error while signing out : ${error.message}")
        }
    }

    override fun readCustomerFlow(): Flow<RequestState<Customer>> = channelFlow{
        try {
            val userId = getCurrentUserId()
            if(userId != null) {
                val customerDB = Firebase.firestore
                customerDB.collection(collectionPath = "customer")
                    .document(userId)
                    .snapshots
                    .collectLatest { document ->
                        if (document.exists) {
                            val customer = Customer(
                                id = document.id,
                                firstName = document.get(field = "firstName"),
                                lastName = document.get(field = "lastName"),
                                email = document.get(field = "email"),
                                phoneNumber = document.get(field = "phoneNumber"),
                                address = document.get(field = "address"),
                                card = document.get(field = "card"),
                                city = document.get(field = "city"),
                                postalCode = document.get(field = "postalCode")
                            )
                            send(RequestState.Success(data = customer))
                        }else{
                            send(RequestState.Error("User not found"))
                        }
                    }
            }else{
                send(RequestState.Error("User not found"))
            }
        }catch (e : Exception){
            send(RequestState.Error("Error while reading customer : ${e.message}"))
        }
    }

    override suspend fun updateCustomer(
        customer: Customer,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            val userId = getCurrentUserId()
            if(userId != null){
                val firestore = Firebase.firestore
                val customerDB = firestore.collection(collectionPath = "customer")
                val customerExist = customerDB.document(customer.id).get()
                //set(customer)
                if(customerExist.exists){
                    customerDB
                        .document(customer.id)
                        .update(
                            "firstname" to customer.firstName,
                            "lastname" to customer.lastName,
                            "phoneNumber" to customer.phoneNumber,
                            "address" to customer.address,
                            "city" to customer.city,
                            "postalCode" to customer.postalCode
                        )
                    onSuccess();
                }else{

                }
                onSuccess()
            }else{
                onError("User not found");
            }
        }catch (e : Exception){
            onError("Error while updating user : ${e.message}")
        }
    }

}