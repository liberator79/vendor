package com.vendor.manage_product.util



import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.PhotosUI.PHPickerConfiguration
import platform.PhotosUI.PHPickerFilter
import platform.PhotosUI.PHPickerResult
import platform.PhotosUI.PHPickerViewController
import platform.PhotosUI.PHPickerViewControllerDelegateProtocol
import platform.UIKit.UIApplication
import platform.Foundation.NSData
import platform.Foundation.getBytes
import platform.darwin.NSObject

actual class PhotoPicker {
    private val openPhotoPicker = mutableStateOf(false)

    actual fun open() {
        openPhotoPicker.value = true
    }

    @Composable
    actual fun InitializePhotoPicker(onImageSelect: (ByteArray?) -> Unit) {
        val openPicker = remember { openPhotoPicker }

        LaunchedEffect(openPicker.value) {
            if (openPicker.value) {
                val configuration = PHPickerConfiguration()
                configuration.filter = PHPickerFilter.imagesFilter
                configuration.selectionLimit = 1

                val picker = PHPickerViewController(configuration)
                val delegate = object : NSObject(), PHPickerViewControllerDelegateProtocol {
                    override fun picker(picker: PHPickerViewController, didFinishPicking: List<*>) {
                        val result = didFinishPicking.firstOrNull() as? PHPickerResult
                        if (result != null) {
                            result.itemProvider.loadDataRepresentationForTypeIdentifier("public.image") { data, error ->
                                if (data != null) {
                                    onImageSelect(data.toByteArray())
                                } else {
                                    onImageSelect(null)
                                }
                            }
                        } else {
                            onImageSelect(null)
                        }
                        picker.dismissViewControllerAnimated(true, null)
                        openPicker.value = false
                    }
                }

                picker.setDelegate(delegate)

                val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
                rootViewController?.presentViewController(picker, true, null)
            }
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
fun NSData.toByteArray(): ByteArray {
    val size = length.toInt()
    val byteArray = ByteArray(size)
    if (size > 0) {
        byteArray.usePinned { pinned ->
            getBytes(pinned.addressOf(0), length)
        }
    }
    return byteArray
}