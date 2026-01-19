package com.vendor.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vendor.home.components.BottomBar
import com.vendor.home.domain.BottomBarDestination
import com.vendor.shared.Surface

@Composable
fun HomeGraphScreen(){
    Scaffold(
        containerColor = Surface
    ) { paddding ->
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(top = paddding.calculateTopPadding(),
                    bottom = paddding.calculateBottomPadding()
                )
        ) {
            Box(
                modifier = Modifier.padding(12.dp)
            ){
                BottomBar(
                    isActive = false,
                    onSelect = {destination ->

                    }
                )
            }
        }
    }
}