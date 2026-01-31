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
    Nepal(
        dialCode = 977,
        code = "NP",
        flag = Resources.Icon.User
    ),
    Japan(
        dialCode = 81,
        code = "JP",
        flag = Resources.Icon.User
    ),
    Russia(
        dialCode = 7,
        code = "RU",
        flag = Resources.Icon.User
    )
}