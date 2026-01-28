package com.vendor.profile

import ContentWithMessageBar
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vendor.shared.BebasNeueFont
import com.vendor.shared.FontSize
import com.vendor.shared.IconPrimary
import com.vendor.shared.Resources
import com.vendor.shared.Surface
import com.vendor.shared.TextPrimary
import com.vendor.shared.components.ErrorCard
import com.vendor.shared.components.LoadingCard
import com.vendor.shared.components.PrimaryButton
import com.vendor.shared.components.ProfileForm
import com.vendor.shared.domain.Country
import com.vendor.shared.util.DisplayResult
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import rememberMessageBarState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navigateToHome : () -> Unit
){
    val viewModel = koinViewModel<ProfileViewModel>()
    val screenState = viewModel.screenState
    val screenReady = viewModel.screenReady
    val isFormValid = viewModel.isFormValid
    val messageBarState = rememberMessageBarState()
    Scaffold(
        containerColor = Surface,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Profile", fontFamily = BebasNeueFont(), fontSize = FontSize.LARGE, color = TextPrimary)
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navigateToHome()
                    }){
                        Icon(
                            painter = painterResource(Resources.Icon.BackArrow),
                            contentDescription = "Back_icon",
                            tint = IconPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Surface,
                    scrolledContainerColor = Surface,
                    navigationIconContentColor = IconPrimary,
                    titleContentColor = TextPrimary,
                    actionIconContentColor = IconPrimary
                )
            )
        }
    ) {padding ->
        ContentWithMessageBar(
            contentBackgroundColor = Surface,
            modifier = Modifier
                .padding(
                    top = padding.calculateTopPadding() + 12.dp,
                    bottom = padding.calculateBottomPadding() + 24.dp
                ),
            messageBarState = messageBarState,
            errorMaxLines = 2
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
            ) {
                screenReady.DisplayResult(
                    onLoading = { LoadingCard(modifier = Modifier.fillMaxSize()) },
                    onSuccess = {state ->
                        Column(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            ProfileForm(
                                modifier = Modifier.weight(1f),
                                firstName = screenState.firstName,
                                onFirstNameChange = viewModel::updateFirstName,
                                lastName = screenState.lastName,
                                onLastNameChange = viewModel::updateLastName,
                                email = screenState.email,
                                city = screenState.city,
                                onCityChange = viewModel::updateCity,
                                address = screenState.address ?: "",
                                onAddressChange = viewModel::updateAddress,
                                postalCode = screenState.postalCode ,
                                onPostalCodeChange = viewModel::updatePostalCode,
                                phoneNumber = screenState.phoneNumber?.phoneNumber,
                                onPhoneNumberChange = viewModel::updatePhoneNumber,
                                country = screenState.country,
                                onCountrySelect = viewModel::updateCountry
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            PrimaryButton(
                                text = "update",
                                icon = Resources.Icon.Check,
                                isEnabled = isFormValid ,
                                onClick = {
                                    viewModel.updateCustomer(
                                        onSuccess = {
                                            messageBarState.addSuccess("Updated User")
                                        },
                                        onError = {message ->
                                            messageBarState.addError(message)
                                        },
                                    )
                                }
                            )
                        }
                    },
                    onError = {message ->
                        ErrorCard(
                            errorText = message,
                            fontSize = FontSize.REGULAR,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                )
            }
        }
    }
}