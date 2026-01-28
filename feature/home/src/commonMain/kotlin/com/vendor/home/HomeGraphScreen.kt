package com.vendor.home

import ContentWithMessageBar
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.vendor.home.components.BottomBar
import com.vendor.home.components.CustomDrawer
import com.vendor.home.domain.BottomBarDestination
import com.vendor.home.domain.CustomDrawerState
import com.vendor.home.domain.isOpened
import com.vendor.home.domain.opposite
import com.vendor.shared.Alpha
import com.vendor.shared.BebasNeueFont
import com.vendor.shared.FontSize
import com.vendor.shared.IconPrimary
import com.vendor.shared.Resources
import com.vendor.shared.Surface
import com.vendor.shared.SurfaceLighter
import com.vendor.shared.TextPrimary
import com.vendor.shared.navigation.Screen
import com.vendor.shared.util.getScreenWidth
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import rememberMessageBarState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeGraphScreen(
    navigateToAuth : () -> Unit,
    navigateToProfile : () -> Unit
){
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState()
    val selectedDestination by remember {
        derivedStateOf {
            val route = currentRoute.value?.destination?.route.toString()
            when{
                route.contains(BottomBarDestination.CustomerFeed.screen.toString()) -> BottomBarDestination.CustomerFeed
                route.contains(BottomBarDestination.Cart.screen.toString()) -> BottomBarDestination.Cart
                route.contains(BottomBarDestination.Categories.screen.toString()) -> BottomBarDestination.Categories
                else -> BottomBarDestination.CustomerFeed
            }
        }
    }
    val screenWidth = remember { getScreenWidth() }
    var drawerState by remember { mutableStateOf(CustomDrawerState.Closed) }

    val offsetValue by remember { derivedStateOf { (screenWidth/1.5).dp } }
    val animatedOffSet by animateDpAsState(
        targetValue = if(drawerState.isOpened())offsetValue else 0.dp,
        animationSpec = tween(
            durationMillis = 600,
            //easing =
        )
    )

    val animateScale by animateFloatAsState(
        targetValue = if(drawerState.isOpened()) 0.9f else 1f
    )

    val animatedRadius by animateDpAsState(
        targetValue = if(drawerState.isOpened()) 20.dp else 0.dp
    )

    val animatedBackground by animateColorAsState(
        targetValue = if(drawerState.isOpened()) SurfaceLighter else Surface
    )

    val messageBarState = rememberMessageBarState()
    val viewModel = koinViewModel<HomeGraphViewModel>()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(animatedBackground)
            .systemBarsPadding()
    ){
        CustomDrawer(
            onLogoutClick = {
                viewModel.signOut(
                    onSuccess = {
                        navigateToAuth();
                    },
                    onError = {error ->
                        messageBarState.addError(error)
                    }
                )
            },
            onProfileClick = {navigateToProfile()},
            onAdminPannelClick = {},
            onContactUsClick = {},
            isCustomDrawerOpen = drawerState.isOpened()
        )
       Box(
           modifier = Modifier
               .fillMaxSize()
               .clip(RoundedCornerShape(animatedRadius))
               .offset(x = animatedOffSet)
               .scale(scale =animateScale)
               .shadow(
                   elevation = animatedRadius,
                   shape = RoundedCornerShape(animatedRadius),
                   ambientColor = Color.Black.copy(alpha = Alpha.DISABLED),
                   spotColor = Color.Black.copy(alpha = Alpha.DISABLED)
               )
       ){
           Scaffold(
               containerColor = Surface,
               topBar = {
                   CenterAlignedTopAppBar(
                       title = {
                           AnimatedContent(
                               targetState = selectedDestination
                           ){destination ->
                               Text(text = destination.title, fontFamily = BebasNeueFont(), fontSize = FontSize.LARGE, color = TextPrimary)
                           }
                       },
                       navigationIcon = {
                           AnimatedContent(
                               targetState = drawerState
                           ){drawer ->
                               if(drawer.isOpened()){
                                   IconButton(onClick = {
                                       drawerState = drawerState.opposite()
                                   }){
                                       Icon(
                                           painter = painterResource(Resources.Icon.Close),
                                           contentDescription = "Menu",
                                           tint = IconPrimary
                                       )
                                   }
                               }else{
                                   IconButton(onClick = {
                                       drawerState = drawerState.opposite()
                                   }){
                                       Icon(
                                           painter = painterResource(Resources.Icon.Menu),
                                           contentDescription = "Menu",
                                           tint = IconPrimary
                                       )
                                   }

                               }
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
           ) { paddding ->
               ContentWithMessageBar(
                   modifier = Modifier
                       .padding(
                           top = paddding.calculateTopPadding(),
                           bottom = paddding.calculateBottomPadding()
                        )
                       .fillMaxSize(),
                   messageBarState = messageBarState,
                   errorMaxLines = 2,
                   contentBackgroundColor = Surface
               ){
                   Column(
                       modifier = Modifier.fillMaxSize()

                   ) {
                       NavHost(
                           modifier = Modifier.weight(1f),
                           startDestination = Screen.CustomerFeed,
                           navController = navController

                       ){
                           composable<Screen.CustomerFeed> {  }
                           composable<Screen.Cart> {  }
                           composable<Screen.Categories> {  }
                       }
                       Spacer(modifier = Modifier.height(12.dp))
                       Box(
                           modifier = Modifier.padding(12.dp)
                       ){
                           BottomBar(
                               isActive = selectedDestination,
                               onSelect = {destination ->
                                   navController.navigate(destination.screen){
                                       launchSingleTop = true
                                       popUpTo<Screen.CustomerFeed>{
                                           saveState = true
                                           inclusive  = false
                                       }
                                       restoreState = true
                                   }
                               }
                           )
                       }
                   }
               }
           }
       }
    }
}