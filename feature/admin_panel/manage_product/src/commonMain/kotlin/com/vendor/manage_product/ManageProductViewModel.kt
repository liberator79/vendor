package com.vendor.manage_product

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vendor.data.domain.AdminRepository
import com.vendor.shared.components.ProductCard
import com.vendor.shared.domain.Product
import com.vendor.shared.domain.ProductCategory
import com.vendor.shared.util.RequestState
import dev.gitlive.firebase.storage.File
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class ManageProductScreenState (
    val id : String = Uuid.random().toHexString(),
    val title : String = "",
    val description : String = "",
    val thumbnail : String = "",
    val category : ProductCategory = ProductCategory.Protein,
    val flavours : String? = null,
    val weight : Int? = null,
    val price: Double = 0.0,
    val createdAt : Long = Clock.System.now().toEpochMilliseconds(),
    val isNew : Boolean = false,
    val isDiscounted : Boolean = false,
    val isPopular : Boolean = false
)

class ManageProductViewModel(
    private val adminRepository: AdminRepository,
    private val savedStateHandle : SavedStateHandle
) : ViewModel() {

    private val productId = savedStateHandle.get<String>("productId") ?: ""



    var screenState by mutableStateOf(ManageProductScreenState())
        private set

    var thumbnailUploaderState : RequestState<Unit> by mutableStateOf(RequestState.Idle)
        private set

    init {
        productId.takeIf { it.isNotEmpty() }?.let {id ->
                viewModelScope.launch {
                    val selectedProduct = adminRepository.getProductById(id)
                    if(selectedProduct.isSuccess()){
                        val product = selectedProduct.getSuccessData()
                        updateId(product.id)
                        updateTitle(product.title)
                        updateDescription(product.description)
                        updateThumbnail(product.thumbnail)
                        updateCategory(ProductCategory.valueOf(product.category))
                        updateFlavours(product.flavours?.joinToString(",") ?: "")
                        updateWeight(product.weight ?: 0)
                        updatePrice(product.price)
                        updateThumbnailUploaderState(RequestState.Success(Unit))
                        updateCreatedAt(product.createdAt)
                        updateIsNew(product.isNew)
                        updateIsDiscounted(product.isDiscounted)
                        updateIsPopular(product.isPopular)
                    }
                }
        }
    }

    val isFormValid: Boolean
        get() = screenState.title.isNotEmpty() &&
                screenState.description.isNotEmpty() &&
                screenState.thumbnail.isNotEmpty() &&
                screenState.price != 0.0

    val isStateChanged : Boolean
        get() = screenState.thumbnail.isNotEmpty()

    fun updateId(value : String){
        screenState = screenState.copy(id = value)
    }
    fun updateTitle(title : String){
        screenState = screenState.copy(title = title)
    }

    fun updateIsNew(isNew: Boolean){
        screenState = screenState.copy(isNew = isNew)
    }

    fun updateIsPopular(isPopular: Boolean){
        screenState = screenState.copy(isPopular = isPopular)
    }

    fun updateIsDiscounted(isDiscounted: Boolean){
        screenState = screenState.copy(isDiscounted = isDiscounted)
    }

    fun updateDescription(description : String){
        screenState = screenState.copy(description = description)
    }

    fun updateThumbnailUploaderState(thumbnail : RequestState<Unit>){
        thumbnailUploaderState = thumbnail
    }

    fun updateCategory(category : ProductCategory){
        screenState = screenState.copy(category = category)
    }

    fun updateCreatedAt(createdAt: Long){
        screenState = screenState.copy(createdAt = createdAt)
    }

    fun updateFlavours(flavours : String){
        screenState = screenState.copy(flavours = flavours)
    }

    fun updateWeight(weight : Int){
        screenState = screenState.copy(weight = weight)
    }

    fun updatePrice(price : Double){
        screenState = screenState.copy(price = price)
    }

    fun createNewProduct(
        onSuccess : () -> Unit,
        onError : (String) -> Unit
    ){
        viewModelScope.launch {
            adminRepository.createNewProduct(
                product = Product(
                    id = screenState.id,
                    title = screenState.title,
                    description = screenState.description,
                    thumbnail = screenState.thumbnail,
                    category = screenState.category.title ,
                    flavours = screenState.flavours?.split(","),
                    weight = screenState.weight,
                    price = screenState.price,
                    isNew = screenState.isNew,
                    isDiscounted = screenState.isDiscounted,
                    isPopular = screenState.isPopular
                ),
                onSuccess = onSuccess,
                onError = onError
            )
        }
    }

    fun updateThumbnail(value: String) {
        screenState = screenState.copy(thumbnail = value)
    }
    fun uploadThumbnailToStorage(
        file : ByteArray?,
        onSuccess: () -> Unit
    ){
        if(file == null || file.size == 0){
            thumbnailUploaderState = RequestState.Error("No file selected")
            return
        }
        updateThumbnailUploaderState(RequestState.Loading)
        viewModelScope.launch {
            try{
                val downloadUrl = adminRepository.uploadImageToStorage(file)
                if(downloadUrl.isNullOrEmpty()){
                    throw Exception("Failed to Retrive Image")
                }

                productId.takeIf { it.isNotEmpty() }?.let {id ->
                    adminRepository.updateThumbnail(
                        productId = id,
                        downloadUrl = downloadUrl,
                        onSuccess = {
                            onSuccess()
                            updateThumbnailUploaderState(RequestState.Success(Unit))
                            updateThumbnail(downloadUrl)
                        },
                        onError = {message ->
                            updateThumbnailUploaderState(RequestState.Error(message))
                        }
                    )
                } ?: run {
                    onSuccess()
                    updateThumbnailUploaderState(RequestState.Success(Unit))
                    updateThumbnail(downloadUrl)
                }

            }catch(e : Exception){
                updateThumbnailUploaderState(RequestState.Error("Error while uploading : ${e.message}"))
            }
        }
    }

    fun updateProduct(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ){
        if(isFormValid){
            viewModelScope.launch {
                adminRepository.updateProduct(
                    product = Product(
                        id = screenState.id,
                        title = screenState.title,
                        description = screenState.description,
                        thumbnail = screenState.thumbnail,
                        category = screenState.category.title ,
                        flavours = screenState.flavours?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() },
                        weight = screenState.weight,
                        price = screenState.price,
                        createdAt = screenState.createdAt,
                        isNew = screenState.isNew,
                        isDiscounted = screenState.isDiscounted,
                        isPopular = screenState.isPopular
                    ),
                    onSuccess = onSuccess,
                    onError = onError,

                )
            }
        }else{
            onError("Please fill all the fields")
        }
    }

    fun deleteProduct(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ){
        productId.takeIf { it.isNotEmpty() }?.let { id ->
            viewModelScope.launch {
                adminRepository.deleteProduct(
                    id,
                    onSuccess = {
                        //delete thubnail from storage
                        onSuccess()
                    },
                    onError = {message ->
                        onError(message)
                    }
                )
            }
        }
    }

}