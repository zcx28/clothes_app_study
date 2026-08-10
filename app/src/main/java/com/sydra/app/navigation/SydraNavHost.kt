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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.sydra.app.data.repository.CommerceRepositoryProvider
import com.sydra.app.feature.address.AddressEvent
import com.sydra.app.feature.address.AddressFormScreen
import com.sydra.app.feature.address.AddressListScreen
import com.sydra.app.feature.address.AddressViewModel
import com.sydra.app.feature.aftersale.AfterSaleDetailScreen
import com.sydra.app.feature.aftersale.AfterSaleDetailViewModel
import com.sydra.app.feature.aftersale.AfterSaleListScreen
import com.sydra.app.feature.aftersale.AfterSaleListViewModel
import com.sydra.app.feature.aftersale.ReturnRequestScreen
import com.sydra.app.feature.aftersale.ReturnRequestViewModel
import com.sydra.app.feature.catalog.ConfiguratorScreen
import com.sydra.app.feature.catalog.CategorySelectionScreen
import com.sydra.app.feature.catalog.ProductDetailScreen
import com.sydra.app.feature.catalog.ProductListScreen
import com.sydra.app.feature.catalog.ShopModeSelectionScreen
import com.sydra.app.feature.home.HomeScreen
import com.sydra.app.feature.logistics.LogisticsScreen
import com.sydra.app.feature.logistics.LogisticsViewModel
import com.sydra.app.feature.order.OrderDetailScreen
import com.sydra.app.feature.order.OrderDetailViewModel
import com.sydra.app.feature.order.OrderListScreen
import com.sydra.app.feature.order.OrderViewModel
import com.sydra.app.feature.profile.ProfileScreen
import com.sydra.app.feature.profile.ProfileViewModel
import com.sydra.app.data.api.SydraApiProvider
import com.sydra.app.domain.model.OrderStatus
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

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
    val commerceRepository = remember { CommerceRepositoryProvider.localPrototype }
    val cartViewModel: CartViewModel = viewModel()
    val profileViewModel: ProfileViewModel = viewModel(
        factory = ProfileViewModel.Factory(SydraApiProvider.localPrototype)
    )
    val orderViewModel: OrderViewModel = viewModel(
        factory = OrderViewModel.Factory(commerceRepository)
    )
    val addressViewModel: AddressViewModel = viewModel(
        factory = AddressViewModel.Factory(commerceRepository)
    )
    val cartItems by cartViewModel.items.collectAsState()
    val profileUiState by profileViewModel.uiState.collectAsState()
    val orderUiState by orderViewModel.uiState.collectAsState()
    val addressUiState by addressViewModel.uiState.collectAsState()
    val backStackEntry by mainNavController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination
    val usesDarkHeader = currentDestination?.hasRoute<SydraRoute.Home>() == true ||
        currentDestination?.hasRoute<SydraRoute.Profile>() == true
    val view = LocalView.current
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarScope = rememberCoroutineScope()
    val showBottomBar = TopLevelDestination.entries.any { currentDestination.isTopLevel(it) }

    LaunchedEffect(orderViewModel) {
        orderViewModel.mutations.collect {
            profileViewModel.refresh()
        }
    }

    SideEffect {
        val activity = view.context as? Activity ?: return@SideEffect
        WindowCompat.getInsetsController(activity.window, view).apply {
            isAppearanceLightStatusBars = !usesDarkHeader
            isAppearanceLightNavigationBars = true
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (showBottomBar) {
                MainBottomBar(
                    currentDestination = currentDestination,
                    onDestinationSelected = { destination ->
                        mainNavController.navigateToTopLevel(destination)
                    }
                )
            }
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
                    onToggleSelection = cartViewModel::toggleSelection,
                    onSetAllSelected = cartViewModel::setAllSelected,
                    onIncreaseQuantity = cartViewModel::increaseQuantity,
                    onDecreaseQuantity = cartViewModel::decreaseQuantity,
                    onUpdateSize = cartViewModel::updateSize,
                    onContinueShopping = {
                        mainNavController.navigateToTopLevel(TopLevelDestination.SHOP)
                    }
                )
            }
            composable<SydraRoute.Profile> {
                ProfileScreen(
                    state = profileUiState,
                    onRetry = profileViewModel::refresh,
                    onOpenOrders = { status ->
                        mainNavController.navigate(SydraRoute.OrderList(status?.toRouteStatus()))
                    },
                    onOpenAfterSales = {
                        mainNavController.navigate(SydraRoute.AfterSaleList)
                    },
                    onOpenAddress = {
                        mainNavController.navigate(SydraRoute.AddressList)
                    }
                )
            }
            composable<SydraRoute.OrderList> { backStackEntry ->
                val route = backStackEntry.toRoute<SydraRoute.OrderList>()
                LaunchedEffect(route.status) {
                    orderViewModel.selectStatus(route.status?.toDomainStatus())
                }
                OrderListScreen(
                    state = orderUiState,
                    onBack = { mainNavController.popBackStack() },
                    onStatusSelected = orderViewModel::selectStatus,
                    onRefresh = orderViewModel::refresh,
                    onOrderClick = { mainNavController.navigate(SydraRoute.OrderDetail(it)) },
                    onTrack = { mainNavController.navigate(SydraRoute.Logistics(it)) },
                    onReturn = { orderId, itemId ->
                        mainNavController.navigate(SydraRoute.ReturnRequest(orderId, itemId))
                    },
                    onChangeAddress = { mainNavController.navigate(SydraRoute.AddressList) },
                    onUnavailableAction = { message ->
                        snackbarScope.launch { snackbarHostState.showSnackbar(message) }
                    },
                    onRequestCancel = orderViewModel::requestCancel,
                    onDismissCancel = orderViewModel::dismissCancel,
                    onConfirmCancel = orderViewModel::confirmCancel,
                    onMessageShown = orderViewModel::clearMessage
                )
            }
            composable<SydraRoute.OrderDetail> { backStackEntry ->
                val route = backStackEntry.toRoute<SydraRoute.OrderDetail>()
                val detailViewModel: OrderDetailViewModel = viewModel(
                    factory = OrderDetailViewModel.Factory(route.orderId, commerceRepository)
                )
                val detailState by detailViewModel.uiState.collectAsState()
                OrderDetailScreen(
                    detailState = detailState,
                    orderState = orderUiState,
                    onBack = { mainNavController.popBackStack() },
                    onRefresh = detailViewModel::refresh,
                    onTrack = { mainNavController.navigate(SydraRoute.Logistics(it)) },
                    onReturn = { orderId, itemId ->
                        mainNavController.navigate(SydraRoute.ReturnRequest(orderId, itemId))
                    },
                    onChangeAddress = { mainNavController.navigate(SydraRoute.AddressList) },
                    onUnavailableAction = { message ->
                        snackbarScope.launch { snackbarHostState.showSnackbar(message) }
                    },
                    onRequestCancel = orderViewModel::requestCancel,
                    onDismissCancel = orderViewModel::dismissCancel,
                    onConfirmCancel = orderViewModel::confirmCancel,
                    onCancelled = {
                        orderViewModel.consumeCancelledOrder()
                        mainNavController.popBackStack()
                    }
                )
            }
            composable<SydraRoute.Logistics> { backStackEntry ->
                val route = backStackEntry.toRoute<SydraRoute.Logistics>()
                val logisticsViewModel: LogisticsViewModel = viewModel(
                    factory = LogisticsViewModel.Factory(route.orderId, commerceRepository)
                )
                val logisticsState by logisticsViewModel.uiState.collectAsState()
                LogisticsScreen(
                    state = logisticsState,
                    onBack = { mainNavController.popBackStack() },
                    onRefresh = logisticsViewModel::refresh
                )
            }
            composable<SydraRoute.AfterSaleList> {
                val afterSaleViewModel: AfterSaleListViewModel = viewModel(
                    factory = AfterSaleListViewModel.Factory(commerceRepository)
                )
                val afterSaleState by afterSaleViewModel.uiState.collectAsState()
                AfterSaleListScreen(
                    state = afterSaleState,
                    onBack = { mainNavController.popBackStack() },
                    onRefresh = afterSaleViewModel::refresh,
                    onRequestClick = {
                        mainNavController.navigate(SydraRoute.AfterSaleDetail(it))
                    }
                )
            }
            composable<SydraRoute.AfterSaleDetail> { backStackEntry ->
                val route = backStackEntry.toRoute<SydraRoute.AfterSaleDetail>()
                val afterSaleDetailViewModel: AfterSaleDetailViewModel = viewModel(
                    factory = AfterSaleDetailViewModel.Factory(route.requestId, commerceRepository)
                )
                val afterSaleDetailState by afterSaleDetailViewModel.uiState.collectAsState()
                AfterSaleDetailScreen(
                    state = afterSaleDetailState,
                    onBack = { mainNavController.popBackStack() },
                    onRefresh = afterSaleDetailViewModel::refresh
                )
            }
            composable<SydraRoute.ReturnRequest> { backStackEntry ->
                val route = backStackEntry.toRoute<SydraRoute.ReturnRequest>()
                val returnViewModel: ReturnRequestViewModel = viewModel(
                    factory = ReturnRequestViewModel.Factory(
                        route.orderId,
                        route.itemId,
                        commerceRepository
                    )
                )
                val returnState by returnViewModel.uiState.collectAsState()
                ReturnRequestScreen(
                    state = returnState,
                    onBack = {
                        if (!returnViewModel.previousStep()) mainNavController.popBackStack()
                    },
                    onConfirmItem = returnViewModel::nextFromConfirmation,
                    onSelectReason = returnViewModel::selectReason,
                    onContinueReason = returnViewModel::continueFromReason,
                    onDescriptionChange = returnViewModel::updateDescription,
                    onAddEvidence = returnViewModel::addEvidence,
                    onRemoveEvidence = returnViewModel::removeEvidence,
                    onSubmit = returnViewModel::submit,
                    onSubmitted = { requestId ->
                        profileViewModel.refresh()
                        orderViewModel.refresh()
                        mainNavController.navigate(SydraRoute.AfterSaleDetail(requestId)) {
                            popUpTo<SydraRoute.ReturnRequest> { inclusive = true }
                        }
                    }
                )
            }
            composable<SydraRoute.AddressList> {
                AddressListScreen(
                    state = addressUiState,
                    onBack = { mainNavController.popBackStack() },
                    onRefresh = addressViewModel::refresh,
                    onAdd = { mainNavController.navigate(SydraRoute.AddressForm()) },
                    onEdit = { mainNavController.navigate(SydraRoute.AddressForm(it)) },
                    onSetDefault = addressViewModel::setDefault,
                    onRequestDelete = addressViewModel::requestDelete,
                    onDismissDelete = addressViewModel::dismissDelete,
                    onConfirmDelete = addressViewModel::confirmDelete,
                    onMessageShown = addressViewModel::clearMessage
                )
            }
            composable<SydraRoute.AddressForm> { backStackEntry ->
                val route = backStackEntry.toRoute<SydraRoute.AddressForm>()
                LaunchedEffect(route.addressId) {
                    addressViewModel.prepareForm()
                }
                LaunchedEffect(addressViewModel) {
                    addressViewModel.events.collect { event ->
                        if (event is AddressEvent.Saved) mainNavController.popBackStack()
                    }
                }
                AddressFormScreen(
                    address = addressViewModel.addressById(route.addressId),
                    state = addressUiState,
                    onBack = { mainNavController.popBackStack() },
                    onSave = addressViewModel::saveAddress
                )
            }
        }
    }
}

private fun OrderStatus.toRouteStatus(): SydraRoute.OrderStatus = when (this) {
    OrderStatus.PENDING_PAYMENT -> SydraRoute.OrderStatus.PENDING_PAYMENT
    OrderStatus.PENDING_SHIPMENT -> SydraRoute.OrderStatus.PENDING_SHIPMENT
    OrderStatus.PENDING_RECEIPT -> SydraRoute.OrderStatus.PENDING_RECEIPT
    OrderStatus.COMPLETED -> SydraRoute.OrderStatus.COMPLETED
    OrderStatus.AFTER_SALE -> SydraRoute.OrderStatus.AFTER_SALE
}

private fun SydraRoute.OrderStatus.toDomainStatus(): OrderStatus = when (this) {
    SydraRoute.OrderStatus.PENDING_PAYMENT -> OrderStatus.PENDING_PAYMENT
    SydraRoute.OrderStatus.PENDING_SHIPMENT -> OrderStatus.PENDING_SHIPMENT
    SydraRoute.OrderStatus.PENDING_RECEIPT -> OrderStatus.PENDING_RECEIPT
    SydraRoute.OrderStatus.COMPLETED -> OrderStatus.COMPLETED
    SydraRoute.OrderStatus.AFTER_SALE -> OrderStatus.AFTER_SALE
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
