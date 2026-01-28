package com.vendor.shared.domain

import com.vendor.shared.Resources
import org.jetbrains.compose.resources.DrawableResource

enum class Country (
    val dialCode : Int,
    val code : String,
    val flag : DrawableResource
){
    India(
        dialCode = 91,
        code = "IN",
        flag = Resources.Icon.User
    ),
    Serbia(
        dialCode = 381,
        code = "RS",
        flag = Resources.Icon.User
    ),
    Usa(
        dialCode = 1,
        code = "US",
        flag = Resources.Icon.User
    )
}