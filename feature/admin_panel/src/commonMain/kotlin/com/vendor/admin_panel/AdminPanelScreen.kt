package com.vendor.admin_panel

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarColors
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vendor.shared.BebasNeueFont
import com.vendor.shared.BorderIdle
import com.vendor.shared.ButtonPrimary
import com.vendor.shared.FontSize
import com.vendor.shared.IconPrimary
import com.vendor.shared.Resources
import com.vendor.shared.Surface
import com.vendor.shared.SurfaceLighter
import com.vendor.shared.TextPrimary
import com.vendor.shared.components.InfoCard
import com.vendor.shared.components.LoadingCard
import com.vendor.shared.components.ProductCard
import com.vendor.shared.domain.Product
import com.vendor.shared.util.DisplayResult
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPanelScreen(
    navigateToHome : () -> Unit,
    navigateToManageProduct : (String?) -> Unit
){
    val viewModel = koinViewModel<AdminPanelViewModel>()
    val products = viewModel.filteredProducts.collectAsState()

    val searchQuery by viewModel.searchQuery.collectAsState()

    var searchBarVisible by mutableStateOf(false)

    Scaffold(
        containerColor = Surface,
        topBar = {
            AnimatedContent(targetState = searchBarVisible){visible ->
                if(visible){
                    SearchBar(
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .fillMaxWidth(),
                        inputField = {
                            SearchBarDefaults.InputField(
                                modifier = Modifier.fillMaxWidth(),
                                query = searchQuery,
                                onQueryChange = viewModel::updateSearchQuery,
                                expanded = false,
                                onExpandedChange = {},
                                onSearch = {},
                                placeholder = {
                                    Text(
                                        text = "Search here",
                                        fontSize = FontSize.REGULAR,
                                        color = TextPrimary
                                    )
                                },
                                trailingIcon = {
                                    IconButton(
                                        modifier = Modifier.size(14.dp),
                                        onClick = {
                                            if (searchQuery.isNotEmpty()) viewModel.updateSearchQuery("")
                                            else searchBarVisible = false
                                        }
                                    ) {
                                        Icon(
                                            painter = painterResource(Resources.Icon.Close),
                                            contentDescription = "Close icon"
                                        )
                                    }
                                }
                            )
                        },
                        colors = SearchBarColors(
                            containerColor = SurfaceLighter,
                            dividerColor = BorderIdle
                        ),
                        expanded = false,
                        onExpandedChange = {},
                        content = {}
                    )
                }else{
                    TopAppBar(
                        title = {
                            Text(text = "Admin Panel", fontFamily = BebasNeueFont(), fontSize = FontSize.LARGE, color = TextPrimary)
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
                        actions = {
                            IconButton(onClick = {searchBarVisible = true}){
                                Icon(
                                    painter = painterResource(Resources.Icon.Search),
                                    contentDescription = "Search_icon",
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
            }

        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {navigateToManageProduct(null)},
                containerColor = ButtonPrimary,
                contentColor = IconPrimary,
                content = {
                    Icon(
                        painter = painterResource(Resources.Icon.Plus),
                        contentDescription = "Add_icon"
                    )
                }
            )
        }
    ) { padding ->
        products.value.DisplayResult(
            modifier = Modifier
                .padding(
                    top = padding.calculateTopPadding(),
                    bottom = padding.calculateBottomPadding()
                ),
            onIdle = {},
            onLoading = {
                LoadingCard(
                    modifier = Modifier.fillMaxSize()
                )
            },
            onError = {message ->
                InfoCard(
                    icon = Resources.Image.Cat,
                    title = "Oops!",
                    subTitle = message
                )
            },
            onSuccess = { latestProducts ->
                AnimatedContent(
                    targetState = latestProducts
                ){products ->
                    if(products.isNotEmpty()){
                        LazyColumn (
                            modifier = Modifier.fillMaxSize().padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            items(
                                items = latestProducts,
                                key = {it.id}
                            ){product ->
                                ProductCard(
                                    modifier = Modifier.fillMaxSize(),
                                    product = product,
                                    onClick = {navigateToManageProduct(product.id)}
                                )
                            }
                        }
                    }else{
                        InfoCard(
                            icon = Resources.Image.Cat,
                            title = "Oops!",
                            subTitle = "No products found"
                        )
                    }
                }
            }
        )
    }
}
