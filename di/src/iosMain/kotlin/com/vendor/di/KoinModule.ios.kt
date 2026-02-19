package com.vendor.di

import com.vendor.manage_product.util.PhotoPicker
import org.koin.core.module.Module
import org.koin.dsl.module

actual val targetModule = module {
    single<PhotoPicker> { PhotoPicker() }
}