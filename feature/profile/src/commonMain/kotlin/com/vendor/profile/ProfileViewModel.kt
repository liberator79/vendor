package com.vendor.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vendor.data.domain.CustomerRepository
import com.vendor.shared.domain.Country
import com.vendor.shared.domain.Customer
import com.vendor.shared.domain.PhoneNumber
import com.vendor.shared.util.RequestState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ProfileScreenState(
    val id: String = "",
    val firstName : String = "",
    val lastName : String = "",
    val email : String = "",
    val city : String? = "",
    val postalCode : Int? = null,
    val address : String? = null,
    val phoneNumber: PhoneNumber? =null,
    val country: Country = Country.India,
)

class ProfileViewModel(
    private val customerRepository: CustomerRepository
) : ViewModel() {
    private val customer = customerRepository.readCustomerFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RequestState.Loading
        )
    var screenReady: RequestState<Unit> by mutableStateOf(RequestState.Loading)
    var screenState: ProfileScreenState by mutableStateOf(ProfileScreenState())
        private set

    val isFormValid : Boolean
        get() = with(screenState){
            firstName.length in 3..50 &&
                    lastName.length in 3..50  &&
                    city?.length in 3..50 &&
                    address?.length in 3..50 &&
                    postalCode?.toString()?.length in 3..8 &&
                    phoneNumber?.phoneNumber?.length in 5..30

        }

    init {
        viewModelScope.launch {
            customer.collectLatest {data ->
                if(data.isSuccess()){
                    val fetchedCustomer = data.getSuccessData()
                    screenState =  ProfileScreenState(
                        id = fetchedCustomer.id,
                        firstName = fetchedCustomer.firstName,
                        lastName = fetchedCustomer.lastName,
                        email = fetchedCustomer.email,
                        address = fetchedCustomer.address,
                        phoneNumber = fetchedCustomer.phoneNumber,
                        country = Country.entries.firstOrNull{it.dialCode == fetchedCustomer.phoneNumber?.countryCode} ?: Country.India
                    )
                    screenReady = RequestState.Success(Unit)
                }else if(data.isError()){
                    screenReady = RequestState.Error(data.getErrorMessage())
                }
            }
        }
    }

    fun updateFirstName(value: String) {
        screenState = screenState.copy(firstName = value)
    }


    fun updateLastName(value: String) {
        screenState = screenState.copy(lastName = value)
    }

    fun updateCity(value: String) {
        screenState = screenState.copy(city = value)
    }

    fun updatePostalCode(value: Int?) {
        screenState = screenState.copy(postalCode = value)
    }

    fun updateAddress(value: String) {
        screenState = screenState.copy(address = value)
    }

    fun updateCountry(value: Country) {
        screenState = screenState.copy(
            country = value,
            phoneNumber = screenState.phoneNumber?.copy(
                countryCode = value.dialCode
            )
        )
    }

    fun updatePhoneNumber(value: String) {
        screenState = screenState.copy(
            phoneNumber = PhoneNumber(
                countryCode = screenState.country.dialCode,
                phoneNumber = value
            )
        )
    }

    fun updateCustomer(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ){
        viewModelScope.launch {
            customerRepository.updateCustomer(
                customer = Customer(
                    id = screenState.id,
                    firstName = screenState.firstName,
                    lastName = screenState.lastName,
                    email = screenState.email,
                    city = screenState.city,
                    postalCode = screenState.postalCode,
                    address = screenState.address,
                    phoneNumber = screenState.phoneNumber
                ),
                onError = onError,
                onSuccess = onSuccess,
            )
        }
    }
}