package com.vendor.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.vendor.admin_panel.AdminPanelScreen
import com.vendor.auth.AuthScreen
import com.vendor.home.HomeGraphScreen
import com.vendor.manage_product.ManageProductScreen
import com.vendor.profile.ProfileScreen
import com.vendor.shared.navigation.Screen

@Composable
fun SetUpNavGraph(startDestination : Screen = Screen.Auth){
    val navController = rememberNavController();
    NavHost(
        navController = navController,
        startDestination = startDestination
    ){
        composable<Screen.Auth>{
            AuthScreen(
                navigateToHome = {
                    navController.navigate(Screen.HomeGraph){
                        popUpTo<Screen.Auth> { inclusive = true }
                    }
                }
            )
        }
        composable<Screen.HomeGraph>{
            HomeGraphScreen(
                navigateToAuth = {
                    navController.navigate(Screen.Auth){
                        popUpTo<Screen.HomeGraph> { inclusive = true }
                    }
                },
                navigateToProfile = {
                    navController.navigate(Screen.Profile)
                },
                navigateToAdmin = {
                    navController.navigate(Screen.AdminPanel)
                }
            )
        }
        composable<Screen.Profile>{
            ProfileScreen(navigateToHome = {
                navController.navigateUp()
            })
        }
        composable<Screen.AdminPanel>{
            AdminPanelScreen(
                navigateToHome = {
                    navController.navigateUp()
                },
                navigateToManageProduct = {id ->
                    navController.navigate(Screen.ManageProduct(id))
                }
            )
        }
        composable<Screen.ManageProduct>{
            val id = it.toRoute<Screen.ManageProduct>().id
            ManageProductScreen(
                id = id,
                navigateBack = {
                    navController.navigateUp()
                }
            )
        }

    }
}
