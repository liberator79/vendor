package com.vendor.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.vendor.home.domain.DrawerItem
import com.vendor.shared.FontSize
import com.vendor.shared.IconPrimary
import com.vendor.shared.IconSecondary
import com.vendor.shared.TextPrimary
import com.vendor.shared.TextSecondary
import org.jetbrains.compose.resources.painterResource

@Composable
fun DrawerItemCard(
    drawerItem: DrawerItem,
    onSelect : () -> Unit,
    isActive : DrawerItem?
){
    Row (
        modifier = Modifier
            .clip(RoundedCornerShape(99.dp))
            .fillMaxWidth()
            .clickable { onSelect() }
            .padding(all = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ){
        Icon(
            contentDescription = "${drawerItem.title}_icon",
            painter = painterResource(drawerItem.icon),
            tint = if(isActive == drawerItem) IconSecondary else IconPrimary
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = drawerItem.title,
            fontSize = FontSize.EXTRA_REGULAR,
            color = if(isActive == drawerItem) TextSecondary else TextPrimary
        )
    }
}