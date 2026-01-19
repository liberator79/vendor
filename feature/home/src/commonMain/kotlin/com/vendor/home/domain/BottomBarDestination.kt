package com.vendor.home.domain

import com.vendor.shared.Resources
import com.vendor.shared.navigation.Screen
import org.jetbrains.compose.resources.DrawableResource

enum class BottomBarDestination(
    val icon : DrawableResource,
    val title : String,
    val screen : Screen
) {
    CustomerFeed(
        icon = Resources.Icon.Home,
        title = "Vendor",
        screen = Screen.CustomerFeed
    ),
    Cart(
        icon = Resources.Icon.ShoppingCart,
        title = "Basket",
        screen = Screen.Cart
    ),
    Categories(
        icon = Resources.Icon.Grid,
        title = "Categories",
        screen = Screen.Categories
    )
}