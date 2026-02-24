package com.vendor.manage_product

import ContentWithMessageBar
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.vendor.manage_product.util.PhotoPicker
import com.vendor.shared.BebasNeueFont
import com.vendor.shared.BorderIdle
import com.vendor.shared.FontSize
import com.vendor.shared.IconPrimary
import com.vendor.shared.Resources
import com.vendor.shared.Surface
import com.vendor.shared.SurfaceBrand
import com.vendor.shared.SurfaceDarker
import com.vendor.shared.SurfaceError
import com.vendor.shared.SurfaceLighter
import com.vendor.shared.SurfaceSecondary
import com.vendor.shared.TextPrimary
import com.vendor.shared.TextSecondary
import com.vendor.shared.TextWhite
import com.vendor.shared.components.AlertTextField
import com.vendor.shared.components.CustomTextField
import com.vendor.shared.components.ErrorCard
import com.vendor.shared.components.LoadingCard
import com.vendor.shared.components.PrimaryButton
import com.vendor.shared.components.dialog.CategoriesDialog
import com.vendor.shared.util.DisplayResult
import com.vendor.shared.util.RequestState
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import rememberMessageBarState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageProductScreen(
    id : String?,
    navigateBack : () -> Unit
){
    val messageBarState = rememberMessageBarState()
    val viewModel = koinViewModel<ManageProductViewModel>()
    val screenState = viewModel.screenState
    var showCategoriesDialog by remember{mutableStateOf(false)}
    var showVerticalMenu by remember{mutableStateOf(false)}
    val isFormValid = viewModel.isFormValid
    val thumbnailUploaderState = viewModel.thumbnailUploaderState
    var showUnsavedDialog by remember{mutableStateOf(false)}
    val photoPicker = koinInject<PhotoPicker>()

    photoPicker.InitializePhotoPicker(
        onImageSelect = {file ->
            viewModel.uploadThumbnailToStorage(
                file = file,
                onSuccess = {messageBarState.addSuccess("Uploaded Successfully")}
            )
        }
    )

    AnimatedVisibility(
        visible = showUnsavedDialog
    ){
        AlertDialog(
            onDismissRequest = {showUnsavedDialog = false},
            title = {Text("Delete Product")},
            text = {Text("Are you sure you dont want to save product")},
            confirmButton = {
                TextButton(
                    onClick = {
                        showUnsavedDialog = false
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showUnsavedDialog = false
                        navigateBack()
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    AnimatedVisibility(
        visible = showCategoriesDialog
    ){
        CategoriesDialog(
            onCategorySelect = {selectedCategory ->
                viewModel.updateCategory(selectedCategory)
                showCategoriesDialog = false
            },
            onDismiss = {showCategoriesDialog = false},
            category = screenState.category
        )
    }

    Scaffold(
        containerColor = Surface,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if(id == null)"New Product" else "Edit Product",
                        fontFamily = BebasNeueFont(),
                        fontSize = FontSize.LARGE,
                        color = TextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if(viewModel.isStateChanged){
                            showUnsavedDialog = true
                        }else{
                            navigateBack()
                        }

                    }){
                        Icon(
                            painter = painterResource(Resources.Icon.BackArrow),
                            contentDescription = "Back_icon",
                            tint = IconPrimary
                        )
                    }
                },
                actions = {
                    id.takeIf { it != null }?.let{
                        Box{
                            IconButton(onClick = {
                                showVerticalMenu = true
                            }){
                                Icon(
                                    painter = painterResource(Resources.Icon.VerticalMenu),
                                    contentDescription = "Vertical menu icon",
                                    tint = IconPrimary
                                )
                            }
                            DropdownMenu(
                                containerColor = Surface,
                                expanded = showVerticalMenu,
                                onDismissRequest = {showVerticalMenu = false}
                            ){
                                DropdownMenuItem(
                                    leadingIcon = {
                                        Icon(
                                            modifier = Modifier.size(14.dp),
                                            painter = painterResource(Resources.Icon.Delete),
                                            contentDescription = "Delete Icons",
                                            tint = IconPrimary,
                                        )
                                    },
                                    text = { Text("Delete") },
                                    onClick = {
                                    showVerticalMenu = false
                                    viewModel.deleteProduct(
                                        onError = {message ->
                                            messageBarState.addError(message)
                                        },
                                        onSuccess = {
                                            messageBarState.addSuccess("Product deleted successfully")
                                            navigateBack()
                                        }
                                    )
                                    }
                                )
                            }
                        }
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
            modifier = Modifier
                .padding(
                    top = padding.calculateTopPadding(),
                    bottom = padding.calculateBottomPadding()
                ),
            errorContainerColor = SurfaceError,
            errorContentColor = TextWhite,
            successContainerColor = SurfaceBrand,
            successContentColor = TextPrimary,
            messageBarState = messageBarState,
            errorMaxLines = 2,
            contentBackgroundColor = Surface,

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
                            .clickable(
                                enabled = thumbnailUploaderState.isIdle()
                            ){
                                photoPicker.open()
                            },
                        contentAlignment = Alignment.Center
                    ){
                        thumbnailUploaderState.DisplayResult(
                            onIdle = {
                                Icon(
                                    modifier = Modifier.size(24.dp),
                                    painter = painterResource(Resources.Icon.Plus),
                                    contentDescription = "Camera_icon",
                                    tint = IconPrimary
                                )
                            },
                            onLoading = {
                                LoadingCard(modifier = Modifier.fillMaxSize())
                            },
                            onError = {message ->
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    ErrorCard(
                                        errorText = message
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    TextButton(
                                        onClick = {
                                            viewModel.updateThumbnailUploaderState(RequestState.Idle)
                                        },
                                        colors = ButtonDefaults.textButtonColors(
                                            contentColor = TextSecondary,
                                            containerColor = Color.Transparent
                                        )
                                    ){
                                        Text("Try Again", fontSize = FontSize.SMALL, color = TextPrimary)
                                    }
                                }
                            },
                            onSuccess = {
                                AsyncImage(
                                    modifier = Modifier.fillMaxSize(),
                                    model = ImageRequest.Builder(
                                        LocalPlatformContext.current
                                    ).data(screenState.thumbnail)
                                        .crossfade(enable = true)
                                        .build(),
                                    contentDescription = "Product Thumbnail",
                                    contentScale = ContentScale.Crop
                                )
                            }
                        )

                    }
                    CustomTextField(
                        value = screenState.title,
                        onValueChange = viewModel::updateTitle,
                        placeholder = "Title"
                    )
                    CustomTextField(
                        modifier = Modifier.height(168.dp),
                        value = screenState.description,
                        onValueChange = viewModel::updateDescription,
                        placeholder = "Description",
                        expanded = true
                    )
                    AlertTextField(
                        modifier = Modifier.fillMaxWidth(),
                        text = screenState.category.title,
                        onClick = {
                            showCategoriesDialog = true
                        },
                    )
//                    AnimatedVisibility(
//                        visible = screenState.category != ProductCategory.Accessories
//                    ){
                        CustomTextField(
                            value = "${screenState.weight ?: ""}",
                            onValueChange = { viewModel.updateWeight(it.toIntOrNull() ?: 0) },
                            placeholder = "Weight (Optional)",
                            keyBoardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            )
                        )
                        CustomTextField(
                            value = screenState.flavours ?: "",
                            onValueChange = viewModel::updateFlavours,
                            placeholder = "Flavours (optional)"
                        )
//                    }
                    CustomTextField(
                        value = "${screenState.price}",
                        onValueChange = {value ->
                            if(value.isEmpty() || value.toDoubleOrNull() != null)
                                viewModel.updatePrice(value.toDoubleOrNull() ?: 0.0)
                        },
                        placeholder = "Price",
                        keyBoardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        )
                    )
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row (
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.updateIsNew(!screenState.isNew)
                                },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        )
                        {
                            Text(
                                text = "New",
                                fontSize = FontSize.REGULAR,
                                color = TextPrimary,
                            )
                            Switch(
                                checked = screenState.isNew,
                                onCheckedChange = {checked ->
                                    viewModel.updateIsNew(checked)
                                },
                                colors = SwitchDefaults.colors(
                                    checkedTrackColor = SurfaceSecondary,
                                    uncheckedTrackColor = SurfaceDarker,
                                    checkedThumbColor = Surface,
                                    uncheckedThumbColor = Surface,
                                    checkedBorderColor = SurfaceSecondary,
                                    uncheckedBorderColor = SurfaceDarker,
                                )
                            )
                        }
                        Row (
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.updateIsPopular(!screenState.isPopular)
                                },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        )
                        {
                            Text(
                                text = "Popular",
                                fontSize = FontSize.REGULAR,
                                color = TextPrimary,
                            )
                            Switch(
                                checked = screenState.isPopular,
                                onCheckedChange = {checked ->
                                    viewModel.updateIsPopular(checked)
                                },
                                colors = SwitchDefaults.colors(
                                    checkedTrackColor = SurfaceSecondary,
                                    uncheckedTrackColor = SurfaceDarker,
                                    checkedThumbColor = Surface,
                                    uncheckedThumbColor = Surface,
                                    checkedBorderColor = SurfaceSecondary,
                                    uncheckedBorderColor = SurfaceDarker
                                )
                            )
                        }
                        Row (
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.updateIsDiscounted(!screenState.isDiscounted)
                                },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        )
                        {
                            Text(
                                text = "Discounted",
                                fontSize = FontSize.REGULAR,
                                color = TextPrimary,
                            )
                            Switch(
                                checked = screenState.isDiscounted,
                                onCheckedChange = {checked ->
                                    viewModel.updateIsDiscounted(checked)
                                },
                                colors = SwitchDefaults.colors(
                                    checkedTrackColor = SurfaceSecondary,
                                    uncheckedTrackColor = SurfaceDarker,
                                    checkedThumbColor = Surface,
                                    uncheckedThumbColor = Surface,
                                    checkedBorderColor = SurfaceSecondary,
                                    uncheckedBorderColor = SurfaceDarker
                                )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
                PrimaryButton(
                    text = if(id == null) "Add new Product" else "Update Product",
                    icon = if(id == null)Resources.Icon.Plus
                            else Resources.Icon.Check,
                    isEnabled = isFormValid,
                    onClick = {
                        if(id == null){
                            viewModel.createNewProduct(
                                onSuccess = {messageBarState.addSuccess("Product added successfully!")},
                                onError = {messageBarState.addError(it)}
                            )
                        }else{
                            viewModel.updateProduct(
                                onSuccess = {messageBarState.addSuccess("Product updated successfully!")},
                                onError = {messageBarState.addError(it)}
                            )
                        }
                    },
                )
            }
        }
    }
}