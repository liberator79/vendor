package com.example.vendor

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.mmk.kmpauth.google.GoogleAuthCredentials
import com.mmk.kmpauth.google.GoogleAuthProvider
import com.vendor.data.domain.CustomerRepository
import com.vendor.shared.navigation.Screen
import com.vendor.navigation.SetUpNavGraph
import com.vendor.shared.Constants
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
@Preview
fun App() {
    MaterialTheme {
        val customerRepository = koinInject<CustomerRepository>()
        var appReady by remember {mutableStateOf(false)};
        var isUserAuthenticated = remember { customerRepository.getCurrentUserId() != null  }
        var startDestination = remember {
            if(isUserAuthenticated) Screen.HomeGraph
            else Screen.Auth
        };
        LaunchedEffect(Unit){
            GoogleAuthProvider.create(credentials = GoogleAuthCredentials(serverId = Constants.WEB_CLIENT_ID))
            appReady = true;
        }
        AnimatedVisibility(
            modifier = Modifier.fillMaxSize(),
            visible = appReady
        ){
            SetUpNavGraph(startDestination = startDestination)
        }
    }
}