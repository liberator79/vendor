package com.vendor.shared.components.dialog

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.vendor.shared.Alpha
import com.vendor.shared.FontSize
import com.vendor.shared.IconPrimary
import com.vendor.shared.IconWhite
import com.vendor.shared.Resources
import com.vendor.shared.Surface
import com.vendor.shared.SurfaceLighter
import com.vendor.shared.SurfaceSecondary
import com.vendor.shared.TextPrimary
import com.vendor.shared.TextSecondary
import com.vendor.shared.components.CustomTextField
import com.vendor.shared.components.ErrorCard
import com.vendor.shared.domain.ProductCategory
import org.jetbrains.compose.resources.painterResource

@Composable
fun CategoriesDialog(
    category : ProductCategory,
    onCategorySelect : (ProductCategory) -> Unit,
    onDismiss : () -> Unit
){
    val allCategories = remember { ProductCategory.entries.toList() }
    var selectedCategory by remember { mutableStateOf(category) }
    AlertDialog(
        containerColor = Surface,
        title = {
            Text(text = "Pick a Category", fontSize = FontSize.EXTRA_MEDIUM, color = TextPrimary)
        },
        text = {
            Column(
                modifier = Modifier
                    .height(300.dp)
                    .fillMaxWidth()
            ) {
                if(allCategories.isNotEmpty()){
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(
                            items = allCategories,
                            key = { it.name }
                        ) { cateogyItem ->
                            CategoryPicker(
                                category = cateogyItem,
                                isSelected = (selectedCategory == cateogyItem),
                                onSelect = {category ->
                                    selectedCategory = category
                                },
                                selectedCategory = selectedCategory
                            )
                        }
                    }
                }else{
                    ErrorCard(errorText = "Dial Code not found", modifier = Modifier.weight(1f))
                }
            }
        },
        onDismissRequest = {onDismiss()},
        confirmButton = {
            TextButton(
                onClick ={onCategorySelect(selectedCategory)},
                colors = ButtonDefaults.textButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = TextSecondary
                )
            ){
                Text(text = "Confirm", fontSize = FontSize.MEDIUM, fontWeight = FontWeight.Medium)
            }
        },
        dismissButton = {
            TextButton(
                onClick ={onDismiss()},
                colors = ButtonDefaults.textButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = TextPrimary.copy(alpha = Alpha.HALF)
                )
            ){
                Text(text = "Cancel", fontSize = FontSize.MEDIUM, fontWeight = FontWeight.Medium)
            }
        }
    )
}

@Composable
fun CategoryPicker(
    category : ProductCategory,
    isSelected : Boolean,
    onSelect : (ProductCategory) -> Unit,
    modifier: Modifier = Modifier,
    selectedCategory : ProductCategory
){
    val animatedBackground by animateColorAsState(
        targetValue = if(category == selectedCategory) category.color.copy(alpha = Alpha.TWENTY_PERCENT) else Color.Transparent
    )
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(animatedBackground)
            .clickable{onSelect(category)}
            .padding(
            vertical = 16.dp,
            horizontal = 12.dp
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row (
            modifier = Modifier
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically,
        ){
            Box(
                modifier = Modifier
                    .size(12.dp)
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
    Box(
        modifier = modifier
            .size(20.dp)
            .clip(CircleShape),
        contentAlignment = Alignment.Center
    ){
        AnimatedVisibility(
            visible = isSelected
        ){
            Icon(
                modifier = Modifier.size(14.dp),
                painter = painterResource(Resources.Icon.Check),
                contentDescription = "check",
                tint = IconPrimary
            )
        }
    }
}

