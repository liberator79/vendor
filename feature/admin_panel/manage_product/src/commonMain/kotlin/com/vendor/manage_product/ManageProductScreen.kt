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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
import com.vendor.shared.SurfaceLighter
import com.vendor.shared.TextPrimary
import com.vendor.shared.TextSecondary
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
    var screenState = viewModel.screenState
    var showCategoriesDialog by remember{mutableStateOf(false)}
    val isFormValid = viewModel.isFormValid
    val thumbnailUploaderState = viewModel.thumbnailUploaderState

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
                    Spacer(modifier = Modifier.height(24.dp))
                }
                PrimaryButton(
                    text = if(id == null) "Add new Product" else "Update Product",
                    icon = if(id == null)Resources.Icon.Plus
                            else Resources.Icon.Check,
                    isEnabled = isFormValid,
                    onClick = {
                        viewModel.createNewProduct(
                            onSuccess = {messageBarState.addSuccess("Product added successfully!")},
                            onError = {messageBarState.addError(it)}
                        )
                    },
                )
            }
        }
    }
}