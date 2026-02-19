package com.vendor.manage_product

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vendor.data.domain.AdminRepository
import com.vendor.shared.domain.Product
import com.vendor.shared.domain.ProductCategory
import com.vendor.shared.util.RequestState
import dev.gitlive.firebase.storage.File
import kotlinx.coroutines.launch
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
    val price: Double = 0.0
)

class ManageProductViewModel(
    private val adminRepository: AdminRepository
) : ViewModel() {
    var screenState by mutableStateOf(ManageProductScreenState())
        private set

    var thumbnailUploaderState : RequestState<Unit> by mutableStateOf(RequestState.Idle)
        private set

    val isFormValid : Boolean
        get() = screenState.title.isNotEmpty() &&
                screenState.description.isNotEmpty() &&
                screenState.category != ProductCategory.Protein &&
                screenState.price != 0.0

    fun updateTitle(title : String){
        screenState = screenState.copy(title = title)
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
                    thumbnail = "https://www.ibef.org/discoverindia/organic-products/organic_images/io-bottom-img.jpg",//screenState.thumbnail ?:
                    category = screenState.category.title ,
                    flavours = screenState.flavours?.split(","),
                    weight = screenState.weight,
                    price = screenState.price,
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
        file : File?,
        onSuccess: () -> Unit
    ){
        if(file == null){
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
                onSuccess()
                updateThumbnailUploaderState(RequestState.Success(Unit))
                updateThumbnail(downloadUrl)
            }catch(e : Exception){
                updateThumbnailUploaderState(RequestState.Error("Error while uploading : ${e.message}"))
            }
        }
    }

}