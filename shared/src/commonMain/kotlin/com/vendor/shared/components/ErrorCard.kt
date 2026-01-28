package com.vendor.shared.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import com.vendor.shared.FontSize
import org.jetbrains.compose.resources.DrawableResource

@Composable
fun ErrorCard(
    modifier : Modifier = Modifier,
    errorText : String = "No Results",
    errorIcons : DrawableResource? = null,
    fontSize : TextUnit = FontSize.SMALL,
){
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ){
        Text(
            text = errorText,
            modifier = Modifier.fillMaxWidth(),
            fontSize = fontSize,
            textAlign = TextAlign.Center,
        )
    }
}