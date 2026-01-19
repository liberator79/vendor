package com.vendor.home.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.vendor.home.domain.BottomBarDestination
import com.vendor.shared.IconPrimary
import com.vendor.shared.IconSecondary
import com.vendor.shared.SurfaceLighter
import org.jetbrains.compose.resources.painterResource

@Composable
fun BottomBar(
    isActive : Boolean,
    modifier: Modifier = Modifier,
    onSelect : (BottomBarDestination) -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceLighter)
            .padding(vertical = 24.dp, horizontal = 36.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        BottomBarDestination.entries.forEach {destination ->
            val animateColor by animateColorAsState(
                if(isActive) IconSecondary else IconPrimary
            )
                Icon(
                    modifier = Modifier.clickable { onSelect(destination) },
                    painter = painterResource(destination.icon),
                    tint = animateColor,
                    contentDescription = destination.title
                )
        }
    }
}

