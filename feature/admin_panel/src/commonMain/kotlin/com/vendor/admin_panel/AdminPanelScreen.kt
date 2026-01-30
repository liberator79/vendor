package com.vendor.admin_panel

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import com.vendor.shared.BebasNeueFont
import com.vendor.shared.ButtonPrimary
import com.vendor.shared.FontSize
import com.vendor.shared.IconPrimary
import com.vendor.shared.Resources
import com.vendor.shared.Surface
import com.vendor.shared.TextPrimary
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPanelScreen(
    navigateToHome : () -> Unit,
    navigateToManageProduct : (String?) -> Unit
){
    Scaffold(
        containerColor = Surface,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Admin Panel", fontFamily = BebasNeueFont(), fontSize = FontSize.LARGE, color = TextPrimary)
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navigateToHome()
                    }){
                        Icon(
                            painter = painterResource(Resources.Icon.BackArrow),
                            contentDescription = "Back_icon",
                            tint = IconPrimary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {}){
                        Icon(
                            painter = painterResource(Resources.Icon.Search),
                            contentDescription = "Search_icon",
                            tint = IconPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Surface,
                    scrolledContainerColor = Surface,
                    navigationIconContentColor = IconPrimary,
                    titleContentColor = TextPrimary,
                    actionIconContentColor = IconPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {navigateToManageProduct(null)},
                containerColor = ButtonPrimary,
                contentColor = IconPrimary,
                content = {
                    Icon(
                        painter = painterResource(Resources.Icon.Plus),
                        contentDescription = "Add_icon"
                    )
                }
            )
        }
    ) {

    }
}