package com.sydra.app.navigation

import kotlinx.serialization.Serializable

/**
 * Routes used by the SYDRA navigation graph.
 *
 * Route arguments are deliberately domain identifiers (String) rather than
 * display text or serialized UI state. The receiving screen can then load the
 * current resource through its ViewModel and Repository.
 */
object SydraRoute {
    @Serializable
    data object Splash

    @Serializable
    data object Login

    @Serializable
    data object Main

    @Serializable
    data object Home

    @Serializable
    data object ShopGraph

    @Serializable
    data object Cart

    @Serializable
    data object Profile

    @Serializable
    data object ShopModeSelection

    @Serializable
    data class CategorySelection(val mode: ShopMode)

    @Serializable
    data class ProductList(val categoryId: String)

    @Serializable
    data class ProductDetail(val productId: String)

    @Serializable
    data class Configurator(
        val categoryId: String,
        val size: String? = null
    )

    @Serializable
    data class OrderList(val status: OrderStatus? = null)

    @Serializable
    data class OrderDetail(val orderId: String)

    @Serializable
    data class Logistics(val orderId: String)

    @Serializable
    data object AfterSaleList

    @Serializable
    data class AfterSaleDetail(val requestId: String)

    @Serializable
    data class ReturnRequest(
        val orderId: String,
        val itemId: String
    )

    @Serializable
    data object AddressList

    /** One screen supports both adding and editing, as required by the analysis. */
    @Serializable
    data class AddressForm(val addressId: String? = null)

    @Serializable
    enum class ShopMode {
        PRODUCTS_LIST,
        CONFIGURATOR
    }

    @Serializable
    enum class OrderStatus {
        PENDING_PAYMENT,
        PENDING_SHIPMENT,
        PENDING_RECEIPT,
        COMPLETED,
        AFTER_SALE
    }
}
