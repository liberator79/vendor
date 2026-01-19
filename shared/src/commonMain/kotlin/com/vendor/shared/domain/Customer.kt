package com.vendor.shared.domain

import kotlinx.serialization.Serializable

@Serializable
data class Customer(
    val id : String,
    val firstName : String,
    val lastName : String,
    val email : String,
    val address : UserAddress? = null,
    val phoneNumber : PhoneNumber? = null,
    val card : List<CartItem> = emptyList()
)
@Serializable
data class UserAddress(
    val city : String,
    val postalCode : String,
    val address : String
)
@Serializable
data class PhoneNumber (
    val countryCode : Int,
    val phoneNumber : String
)