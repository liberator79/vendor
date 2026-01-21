package com.vendor.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vendor.data.domain.CustomerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeGraphViewModel(
    private val CustomerRepository : CustomerRepository

): ViewModel() {
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