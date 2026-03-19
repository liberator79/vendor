package com.vendor.products_overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vendor.data.domain.ProductsRepository
import com.vendor.shared.util.RequestState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class ProductsOverviewViewModel(
    private val productsRepository: ProductsRepository
): ViewModel() {
    private val diacountedProducts = productsRepository.readDiscountedProducts()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RequestState.Loading
        )

    private val newProducts = productsRepository.readNewProducts()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RequestState.Loading
        )

    val products = combine(
        diacountedProducts,
        newProducts
    ) { discountedProducts, newProducts ->
        when{
            newProducts.isSuccess() && discountedProducts.isSuccess() -> {
                RequestState.Success(
                    newProducts.getSuccessData() + discountedProducts.getSuccessData()
                )
            }
            newProducts.isError() -> newProducts
            discountedProducts.isError() -> discountedProducts
            else -> RequestState.Loading
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = RequestState.Loading
    )
}