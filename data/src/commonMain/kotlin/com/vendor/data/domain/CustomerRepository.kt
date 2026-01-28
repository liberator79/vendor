package com.vendor.data.domain

import com.vendor.shared.domain.Customer
import com.vendor.shared.util.RequestState
import dev.gitlive.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface CustomerRepository {
    suspend fun createCustomer(
        user : FirebaseUser?,
        onSuccess : () -> Unit,
        onError : (String) -> Unit
    )

    fun getCurrentUserId() : String?

    suspend fun signout() : RequestState<Unit>

    fun readCustomerFlow() : Flow<RequestState<Customer>>

    suspend fun updateCustomer(
        customer : Customer,
        onSuccess: () -> Unit,
        onError : (String) -> Unit
    )
}