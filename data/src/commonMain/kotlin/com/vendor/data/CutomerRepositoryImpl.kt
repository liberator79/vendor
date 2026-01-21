package com.vendor.data

import com.vendor.data.domain.CustomerRepository
import com.vendor.shared.domain.Customer
import com.vendor.shared.util.RequestState
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore

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

}