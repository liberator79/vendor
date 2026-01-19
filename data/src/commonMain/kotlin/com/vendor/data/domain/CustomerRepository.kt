package com.vendor.data.domain

import dev.gitlive.firebase.auth.FirebaseUser

interface CustomerRepository {
    suspend fun createCustomer(
        user : FirebaseUser?,
        onSuccess : () -> Unit,
        onError : (String) -> Unit
    )

    fun getCurrentUserId() : String?
}