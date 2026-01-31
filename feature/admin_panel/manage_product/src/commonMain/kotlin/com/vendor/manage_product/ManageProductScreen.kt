package com.vendor.manage_product

import ContentWithMessageBar
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.vendor.shared.BebasNeueFont
import com.vendor.shared.BorderIdle
import com.vendor.shared.FontSize
import com.vendor.shared.IconPrimary
import com.vendor.shared.Resources
import com.vendor.shared.Surface
import com.vendor.shared.SurfaceLighter
import com.vendor.shared.TextPrimary
import com.vendor.shared.components.AlertTextField
import com.vendor.shared.components.CustomTextField
import com.vendor.shared.components.PrimaryButton
import com.vendor.shared.components.dialog.CategoriesDialog
import com.vendor.shared.domain.ProductCategory
import org.jetbrains.compose.resources.painterResource
import rememberMessageBarState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageProductScreen(
    id : String?,
    navigateBack : () -> Unit
){
    val messageBarState = rememberMessageBarState()
    var categoty by remember { mutableStateOf(ProductCategory.Protein)}
    var showCategoriesDialog by remember{mutableStateOf(false)}

    AnimatedVisibility(
        visible = showCategoriesDialog
    ){
        CategoriesDialog(
            onCategorySelect = {selectedCategory ->
                categoty = selectedCategory
                showCategoriesDialog = false
            },
            onDismiss = {showCategoriesDialog = false},
            category = categoty
        )
    }

    Scaffold(
        containerColor = Surface,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if(id == null)"Edit Product" else "New Product",
                        fontFamily = BebasNeueFont(),
                        fontSize = FontSize.LARGE,
                        color = TextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navigateBack()
                    }){
                        Icon(
                            painter = painterResource(Resources.Icon.BackArrow),
                            contentDescription = "Back_icon",
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
        }
    ) {padding ->

        ContentWithMessageBar(
            messageBarState = messageBarState,
            errorMaxLines = 2,
            contentBackgroundColor = Surface,
            modifier = Modifier
                .padding(
                    top = padding.calculateTopPadding(),
                    bottom = padding.calculateBottomPadding()
                )
        ){

            Column(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 24.dp, top = 12.dp)

            ){

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .imePadding(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .border(
                                width = 1.dp,
                                color = BorderIdle,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .background(SurfaceLighter)
                            .clickable{},
                        contentAlignment = Alignment.Center
                    ){
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(Resources.Icon.Plus),
                            contentDescription = "Camera_icon",
                            tint = IconPrimary
                        )
                    }
                    CustomTextField(
                        value = "",
                        onValueChange = {},
                        placeholder = "Title"
                    )
                    CustomTextField(
                        modifier = Modifier.height(168.dp),
                        value = "",
                        onValueChange = {},
                        placeholder = "Description",
                        expanded = true
                    )
                    AlertTextField(
                        modifier = Modifier.fillMaxWidth(),
                        text = categoty.title,
                        onClick = {
                            showCategoriesDialog = true
                        },
                    )
                    CustomTextField(
                        value = "",
                        onValueChange = {},
                        placeholder = "Weight (Optional)",
                        keyBoardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        )
                    )
                    CustomTextField(
                        value = "",
                        onValueChange = {},
                        placeholder = "Flavours (optional)"
                    )
                    CustomTextField(
                        value = "",
                        onValueChange = {},
                        placeholder = "Price",
                        keyBoardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        )
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
                PrimaryButton(
                    text = if(id == null) "Add Product" else "Update Product",
                    onClick = {},
                    icon = Resources.Icon.Plus,
                )
            }
        }
    }
}