package com.sydra.app.feature.order

import com.sydra.app.domain.model.Order
import com.sydra.app.domain.model.OrderStatus

sealed interface OrderLoadState {
    data object Loading : OrderLoadState
    data class Content(val orders: List<Order>) : OrderLoadState
    data object Empty : OrderLoadState
    data class Error(val message: String) : OrderLoadState
}

data class OrderListUiState(
    val selectedStatus: OrderStatus? = null,
    val loadState: OrderLoadState = OrderLoadState.Loading,
    val cancelTargetOrderId: String? = null,
    val isCancelling: Boolean = false,
    val message: String? = null,
    val cancelledOrderId: String? = null
)

sealed interface OrderDetailUiState {
    data object Loading : OrderDetailUiState
    data class Content(val order: Order) : OrderDetailUiState
    data class Error(val message: String) : OrderDetailUiState
}
