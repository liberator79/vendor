package com.vendor.shared.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vendor.shared.Alpha
import com.vendor.shared.ButtonDisabled
import com.vendor.shared.ButtonPrimary
import com.vendor.shared.FontSize
import com.vendor.shared.TextPrimary
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    text : String,
    icon : DrawableResource? = null,
    isEnabled : Boolean = true,
    onClick : () -> Unit
){
    Button(
        modifier = modifier.fillMaxWidth(),
        onClick =onClick,
        enabled = isEnabled,
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = ButtonPrimary,
            contentColor = TextPrimary,
            disabledContentColor = TextPrimary.copy(alpha = Alpha.DISABLED),
            disabledContainerColor = ButtonDisabled
        ),
        contentPadding = PaddingValues(20.dp)
    ){
        if(icon != null) {
            Icon(
                modifier = Modifier.size(14.dp),
                painter = painterResource(icon),
                contentDescription = "button_icon"
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text =text,
            fontSize = FontSize.REGULAR,
            fontWeight = FontWeight.Medium
        )
    }
}