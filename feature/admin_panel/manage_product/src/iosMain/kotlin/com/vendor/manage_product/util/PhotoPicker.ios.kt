package com.vendor.manage_product.util

import androidx.compose.runtime.Composable
import dev.gitlive.firebase.storage.File

actual class PhotoPicker {
    @Composable
    actual fun InitializePhotoPicket(onImageSelect: (File?) -> Unit) {
    }

    actual fun open() {
    }
}