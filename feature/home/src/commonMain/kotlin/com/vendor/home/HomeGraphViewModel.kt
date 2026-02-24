package com.vendor.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vendor.data.domain.CustomerRepository
import com.vendor.shared.util.RequestState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeGraphViewModel(
    private val CustomerRepository : CustomerRepository

): ViewModel() {

    val customer = CustomerRepository.readCustomerFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RequestState.Loading
        )

    fun signOut(
        onSuccess : () -> Unit,
        onError: (String) -> Unit
    ){
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO){
                CustomerRepository.signout()
            }
            if(result.isSuccess()){
                onSuccess()
            }else{
                onError(result.getErrorMessage())
            }
        }
    }
}