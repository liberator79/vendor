package com.vendor.profile.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.vendor.shared.Alpha
import com.vendor.shared.BorderError
import com.vendor.shared.BorderIdle
import com.vendor.shared.FontSize
import com.vendor.shared.SurfaceLighter
import com.vendor.shared.TextPrimary

@Composable
fun CustomTextField(
    modifier : Modifier = Modifier,
    value : String,
    onValueChange : (String) -> Unit,
    placeholder : String? = null,
    enabled : Boolean = true,
    error : Boolean = false,
    expanded : Boolean = false,
    keyBoardOptions : KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text
    )
){
    val boarderColor by animateColorAsState(
        targetValue = if(error) BorderError else BorderIdle
    )

    TextField(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = boarderColor,
                shape = RoundedCornerShape(6.dp)
            ),

        enabled = enabled,
        value = value,
        onValueChange = onValueChange,
        placeholder = if(placeholder != null) {
            @Composable {
                Text(
                    modifier =Modifier.alpha(Alpha.DISABLED),
                    text = placeholder,
                    fontSize = FontSize.REGULAR
                )
            }
        } else null,
        singleLine = !expanded,
        shape = RoundedCornerShape(6.dp),
        keyBoardOptions = keyBoardOptions,
        colors = TextFieldDefaults.colors(
            disabledTextColor = TextPrimary.copy(alpha = Alpha.DISABLED),
            focusedTextColor = TextPrimary
        )
    )
}