package com.vendor.home.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vendor.home.domain.DrawerItem
import com.vendor.shared.BebasNeueFont
import com.vendor.shared.FontSize
import com.vendor.shared.TextSecondary

@Composable
fun CustomDrawer(
    onProfileClick : () -> Unit,
    onLogoutClick : () -> Unit,
    onAdminPannelClick : () -> Unit,
    onContactUsClick : () -> Unit,
    isCustomDrawerOpen : Boolean
){
    var selectedItem by remember { mutableStateOf<DrawerItem?>(null) }
    if(!isCustomDrawerOpen){
        selectedItem = null
    }
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.6f)
            .padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(modifier = Modifier.height(50.dp))
        Text(
            text = "Vending",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = TextSecondary,
            fontFamily = BebasNeueFont(),
            fontSize = FontSize.EXTRA_LARGE
        )
        Spacer(modifier = Modifier.height(50.dp))
        DrawerItem.entries.take(3).forEach { drawerItem ->
            Box(
                modifier = Modifier.padding(vertical = 12.dp)
            ){
                DrawerItemCard(
                    drawerItem,
                    onSelect = {
                        when(drawerItem){
                            DrawerItem.Profile -> {
                                selectedItem = DrawerItem.Profile
                                onProfileClick()
                            }
                            DrawerItem.ContactUs -> {
                                selectedItem = DrawerItem.ContactUs
                                onContactUsClick()
                            }
                            DrawerItem.Logout -> {
                                selectedItem = DrawerItem.Logout
                                onLogoutClick()
                            }
                            else -> {}
                        }
                    },
                    isActive = selectedItem
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        DrawerItemCard(
            drawerItem = DrawerItem.Admin,
            onSelect = {
                selectedItem = DrawerItem.Admin
                onAdminPannelClick()
            },
            isActive = selectedItem,
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}

