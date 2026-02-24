package com.vendor.manage_product.util

import androidx.compose.runtime.Composable
import dev.gitlive.firebase.storage.File

expect class PhotoPicker {

    fun open()
    @Composable
    fun InitializePhotoPicker(onImageSelect : (ByteArray?) -> Unit)
}