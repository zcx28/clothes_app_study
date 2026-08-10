package com.sydra.app.navigation

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountTree
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sydra.app.feature.cart.CartScreen
import com.sydra.app.feature.cart.CartViewModel
import com.sydra.app.feature.catalog.ConfiguratorScreen
import com.sydra.app.feature.catalog.CategorySelectionScreen
import com.sydra.app.feature.catalog.ProductDetailScreen
import com.sydra.app.feature.catalog.ProductListScreen
import com.sydra.app.feature.catalog.ShopModeSelectionScreen
import com.sydra.app.feature.home.HomeScreen
import com.sydra.app.feature.profile.ProfileScreen

@Composable
fun SydraNavHost() {
    val rootNavController = rememberNavController()

    NavHost(
        navController = rootNavController,
        startDestination = SydraRoute.Main
    ) {
        composable<SydraRoute.Main> {
            MainScaffold()
        }
    }
}

@Composable
private fun MainScaffold() {
    val mainNavController = rememberNavController()
    val cartViewModel: CartViewModel = viewModel()
    val cartItems by cartViewModel.items.collectAsState()
    val backStackEntry by mainNavController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination
    val usesDarkHeader = currentDestination?.hasRoute<SydraRoute.Home>() == true ||
        currentDestination?.hasRoute<SydraRoute.Profile>() == true
    val view = LocalView.current

    SideEffect {
        val activity = view.context as? Activity ?: return@SideEffect
        WindowCompat.getInsetsController(activity.window, view).apply {
            isAppearanceLightStatusBars = !usesDarkHeader
            isAppearanceLightNavigationBars = true
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            MainBottomBar(
                currentDestination = currentDestination,
                onDestinationSelected = { destination ->
                    mainNavController.navigateToTopLevel(destination)
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = mainNavController,
            startDestination = SydraRoute.Home,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<SydraRoute.Home> {
                HomeScreen(
                    onExploreClick = {
                        mainNavController.navigateToTopLevel(
                            TopLevelDestination.SHOP
                        )
                    }
                )
            }
            navigation<SydraRoute.ShopGraph>(
                startDestination = SydraRoute.ShopModeSelection
            ) {
                composable<SydraRoute.ShopModeSelection> {
                    ShopModeSelectionScreen(
                        onConfiguratorClick = {
                            mainNavController.navigate(
                                SydraRoute.CategorySelection(SydraRoute.ShopMode.CONFIGURATOR)
                            ) {
                                popUpTo<SydraRoute.ShopModeSelection> {
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }
                        },
                        onProductsListClick = {
                            mainNavController.navigate(
                                SydraRoute.CategorySelection(SydraRoute.ShopMode.PRODUCTS_LIST)
                            ) {
                                popUpTo<SydraRoute.ShopModeSelection> {
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }
                        }
                    )
                }
                composable<SydraRoute.CategorySelection> { backStackEntry ->
                    val route = backStackEntry.toRoute<SydraRoute.CategorySelection>()
                    CategorySelectionScreen(
                        mode = route.mode,
                        onCategoryClick = { categoryId ->
                            if (route.mode == SydraRoute.ShopMode.PRODUCTS_LIST) {
                                mainNavController.navigate(SydraRoute.ProductList(categoryId))
                            } else {
                                mainNavController.navigate(SydraRoute.Configurator(categoryId))
                            }
                        },
                        onBackClick = { mainNavController.popBackStack() }
                    )
                }
                composable<SydraRoute.ProductList> { backStackEntry ->
                    val route = backStackEntry.toRoute<SydraRoute.ProductList>()
                    ProductListScreen(
                        categoryId = route.categoryId,
                        onCategoryClick = { categoryId ->
                            mainNavController.navigate(SydraRoute.ProductList(categoryId)) {
                                popUpTo<SydraRoute.ProductList> {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        },
                        onProductClick = { productId ->
                            mainNavController.navigate(SydraRoute.ProductDetail(productId))
                        },
                        onBackClick = { mainNavController.popBackStack() }
                    )
                }
                composable<SydraRoute.ProductDetail> { backStackEntry ->
                    val route = backStackEntry.toRoute<SydraRoute.ProductDetail>()
                    ProductDetailScreen(
                        productId = route.productId,
                        onAddToCart = { product, size ->
                            cartViewModel.addProduct(product, size)
                        },
                        onBuyNow = { product, size ->
                            cartViewModel.addProduct(product, size)
                            mainNavController.navigateToTopLevel(TopLevelDestination.CART)
                        },
                        onBackClick = { mainNavController.popBackStack() }
                    )
                }
                composable<SydraRoute.Configurator> { backStackEntry ->
                    val route = backStackEntry.toRoute<SydraRoute.Configurator>()
                    ConfiguratorScreen(
                        categoryId = route.categoryId,
                        onAddToCart = { categoryId, size ->
                            cartViewModel.addConfiguration(categoryId, size)
                            mainNavController.navigateToTopLevel(TopLevelDestination.CART)
                        },
                        onBackClick = { mainNavController.popBackStack() }
                    )
                }
            }
            composable<SydraRoute.Cart> {
                CartScreen(
                    items = cartItems,
                    onRemove = cartViewModel::remove,
                    onContinueShopping = {
                        mainNavController.navigateToTopLevel(TopLevelDestination.SHOP)
                    }
                )
            }
            composable<SydraRoute.Profile> {
                ProfileScreen()
            }
        }
    }
}

private enum class TopLevelDestination(val label: String) {
    HOME("首页"),
    SHOP("选购"),
    CART("购物车"),
    PROFILE("我的")
}

@Composable
private fun MainBottomBar(
    currentDestination: NavDestination?,
    onDestinationSelected: (TopLevelDestination) -> Unit
) {
    NavigationBar(
        modifier = Modifier.height(112.dp),
        containerColor = Color.White,
        tonalElevation = 0.dp
    ) {
        TopLevelDestination.entries.forEach { destination ->
            NavigationBarItem(
                selected = currentDestination.isTopLevel(destination),
                onClick = { onDestinationSelected(destination) },
                icon = {
                    Icon(
                        imageVector = when (destination) {
                            TopLevelDestination.HOME -> Icons.Outlined.Home
                            TopLevelDestination.SHOP -> Icons.Outlined.AccountTree
                            TopLevelDestination.CART -> Icons.Outlined.ShoppingCart
                            TopLevelDestination.PROFILE -> Icons.Outlined.Person
                        },
                        modifier = Modifier.size(36.dp),
                        contentDescription = destination.label
                    )
                },
                label = null,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Black,
                    unselectedIconColor = Color(0xFFA6A6A6),
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

private fun NavDestination?.isTopLevel(destination: TopLevelDestination): Boolean {
    return when (destination) {
        TopLevelDestination.HOME -> this?.hasRoute<SydraRoute.Home>() == true
        TopLevelDestination.SHOP ->
            this?.hierarchy?.any { it.hasRoute<SydraRoute.ShopGraph>() } == true
        TopLevelDestination.CART -> this?.hasRoute<SydraRoute.Cart>() == true
        TopLevelDestination.PROFILE -> this?.hasRoute<SydraRoute.Profile>() == true
    }
}

private fun NavHostController.navigateToTopLevel(destination: TopLevelDestination) {
    when (destination) {
        TopLevelDestination.HOME -> navigateTopLevelRoute(
            route = SydraRoute.Home,
            saveState = false,
            restoreState = false
        )
        TopLevelDestination.SHOP -> navigateTopLevelRoute(
            route = SydraRoute.ShopGraph,
            saveState = false,
            restoreState = false,
            launchSingleTop = false
        )
        TopLevelDestination.CART -> navigateTopLevelRoute(SydraRoute.Cart)
        TopLevelDestination.PROFILE -> navigateTopLevelRoute(SydraRoute.Profile)
    }
}

private inline fun <reified T : Any> NavHostController.navigateTopLevelRoute(
    route: T,
    saveState: Boolean = true,
    restoreState: Boolean = true,
    launchSingleTop: Boolean = true
) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) {
            this.saveState = saveState
        }
        this.launchSingleTop = launchSingleTop
        this.restoreState = restoreState
    }
}

@Composable
private fun TopLevelPlaceholder(
    title: String,
    description: String,
    contentPadding: PaddingValues = PaddingValues(24.dp)
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(contentPadding),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = description,
            modifier = Modifier.padding(top = 12.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
