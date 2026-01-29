package com.vendor.shared.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vendor.shared.FontSize
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun InfoCard(
    modifier : Modifier = Modifier,
    title : String,
    subTitle : String,
    icon : DrawableResource
){
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.size(60.dp),
            painter = painterResource(icon),
            contentDescription = "info_icon"
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = title,
            fontSize = FontSize.MEDIUM,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = subTitle,
            fontSize = FontSize.REGULAR,
        )
    }
}