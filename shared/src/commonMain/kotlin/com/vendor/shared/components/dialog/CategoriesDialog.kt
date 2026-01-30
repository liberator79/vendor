package com.vendor.shared.components.dialog

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.vendor.shared.FontSize
import com.vendor.shared.IconWhite
import com.vendor.shared.Resources
import com.vendor.shared.SurfaceLighter
import com.vendor.shared.SurfaceSecondary
import com.vendor.shared.TextPrimary
import com.vendor.shared.domain.ProductCategory
import org.jetbrains.compose.resources.painterResource

@Composable
fun CategoriesDialog(
    onCategorySelect : () -> Unit,
    onDismiss : () -> Unit
){
    val allCategories = remember { ProductCategory.entries.toList() }

    LazyColumn(

    ) {
        items(
            items = allCategories,
            key = {it.name}
        ){categoryItem ->
//            Cate(
//                country = categoryItem,
//                isSelected = false,
//                onSelect = {}
//            )
        }
    }
}

@Composable
fun CategoryPicker(
    category : ProductCategory,
    isSelected : Boolean,
    onSelect : () -> Unit,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable{onSelect()}
    ) {
        Row (
            modifier = Modifier
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically,
        ){
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(color = category.color)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = category.title,
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold,
                fontSize = FontSize.REGULAR,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Selectoor(
            isSelected = isSelected
        )
    }
}

@Composable
fun Selectoor(
    isSelected : Boolean,
    modifier : Modifier = Modifier
){
    val animatedBackground by animateColorAsState(
        targetValue = if(isSelected) SurfaceSecondary else SurfaceLighter
    )
    Box(
        modifier = modifier
            .size(20.dp)
            .clip(CircleShape)
            .background(animatedBackground),
        contentAlignment = Alignment.Center
    ){
        AnimatedVisibility(
            visible = isSelected
        ){
            Icon(
                painter = painterResource(Resources.Icon.Check),
                contentDescription = "check",
                modifier = Modifier.size(14.dp),
                tint = IconWhite
            )
        }
    }
}

