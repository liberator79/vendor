package com.vendor.home.domain


import com.vendor.shared.Resources
import org.jetbrains.compose.resources.DrawableResource

enum class DrawerItem(
    val title : String,
    val icon : DrawableResource
) {
    Profile(
        title = "Profile",
        icon = Resources.Icon.User
    ),
    ContactUs(
        title = "Contact Us",
        icon = Resources.Icon.Edit
    ),
    Logout(
        title = "Log out",
        icon = Resources.Icon.Log
    ),
    Admin(
        title = "Adming Pannel",
        icon = Resources.Icon.Unlock
    )
}