package com.vendor.auth

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import rememberMessageBarState
import ContentWithMessageBar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mmk.kmpauth.firebase.google.GoogleButtonUiContainerFirebase
import com.vendor.auth.component.GoogleButton
import com.vendor.shared.Alpha
import com.vendor.shared.BebasNeueFont
import com.vendor.shared.FontSize
import com.vendor.shared.Surface
import com.vendor.shared.SurfaceBrand
import com.vendor.shared.SurfaceError
import com.vendor.shared.TextPrimary
import com.vendor.shared.TextSecondary
import com.vendor.shared.TextWhite
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AuthScreen(navigateToHome : () -> Unit){
    val scope = rememberCoroutineScope()
    val viewModel = koinViewModel<AuthViewModel>()
    val messageBarState = rememberMessageBarState()
    var loadingState by remember { mutableStateOf(false) }

    Scaffold { padding ->
        ContentWithMessageBar(
            contentBackgroundColor = Surface,
            messageBarState = messageBarState,
            errorMaxLines = 2,
            modifier = Modifier.padding(
                top = padding.calculateTopPadding(),
                bottom = padding.calculateBottomPadding()),
            errorContentColor = TextWhite,
            errorContainerColor = SurfaceError,
            successContainerColor = SurfaceBrand,
            successContentColor = TextPrimary
        ){
            Column (
                modifier =Modifier.fillMaxSize().padding(all = 24.dp)
            ){
                Column (
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(
                        text = "Vendor",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        fontFamily = BebasNeueFont(),
                        fontSize = FontSize.EXTRA_LARGE,
                        color = TextSecondary
                    )
                    Text(
                        text = "Sign in to continue",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().alpha(Alpha.HALF),
                        fontSize = FontSize.REGULAR,
                        color = TextPrimary
                    )
                }
                GoogleButtonUiContainerFirebase(
                    linkAccount = false,
                    onResult = {result ->
                        result.onSuccess { user ->
                            viewModel.createCustomer(
                                user = user,
                                onSuccess = {
                                    scope.launch {
                                        messageBarState.addSuccess("Authentication Successful")
                                        //delay(2000)
                                        navigateToHome()
                                    }
                                },
                                onError = {error -> messageBarState.addError(error)}
                            )
                            loadingState = false
                        }.onFailure { error ->
                            if(error.message?.contains("A network error") == true){
                                messageBarState.addError("Check your network")
                            }else if(error.message?.contains("Idtoken is null") == true){
                                messageBarState.addError("Sign in canceled")
                            }else{
                                messageBarState.addError(error?.message ?: "Unknown")
                            }
                            loadingState = false;
                        }
                    }
                ){
                    GoogleButton(
                        loading = loadingState ,
                        onClick = {
                            loadingState = true;
                            this@GoogleButtonUiContainerFirebase.onClick()
                        }
                    )
                }
            }
        }
    }
}