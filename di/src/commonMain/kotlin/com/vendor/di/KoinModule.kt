package com.vendor.di

import androidx.lifecycle.viewmodel.compose.viewModel
import com.vendor.auth.AuthViewModel
import com.vendor. data.domain.CustomerRepository
import com.vendor.data.CutomerRepositoryImpl
import com.vendor.home.HomeGraphViewModel
import com.vendor.profile.ProfileViewModel
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module


val sharedModule = module {
    single<CustomerRepository> { CutomerRepositoryImpl() }
    viewModelOf(::AuthViewModel)
    viewModelOf(::HomeGraphViewModel)
    viewModelOf(::ProfileViewModel)
}
fun initKoin(
    config : (KoinApplication.() -> Unit)? = null,
){
    startKoin {
        config?.invoke(this)
        modules(sharedModule)
    }
}