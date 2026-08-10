package com.sydra.app.feature.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sydra.app.data.api.ApiResult
import com.sydra.app.data.repository.OrderRepository
import com.sydra.app.domain.model.OrderStatus
import java.util.UUID
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OrderViewModel(private val repository: OrderRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(OrderListUiState())
    val uiState: StateFlow<OrderListUiState> = _uiState.asStateFlow()

    private val _mutations = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val mutations: SharedFlow<Unit> = _mutations.asSharedFlow()

    private var loadJob: Job? = null

    init {
        refresh()
    }

    fun selectStatus(status: OrderStatus?) {
        if (_uiState.value.selectedStatus == status && _uiState.value.loadState !is OrderLoadState.Error) {
            return
        }
        _uiState.value = _uiState.value.copy(selectedStatus = status)
        refresh()
    }

    fun refresh() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            val status = _uiState.value.selectedStatus
            _uiState.value = _uiState.value.copy(loadState = OrderLoadState.Loading)
            _uiState.value = when (val result = repository.getOrders(status)) {
                is ApiResult.Success -> _uiState.value.copy(
                    loadState = if (result.data.items.isEmpty()) {
                        OrderLoadState.Empty
                    } else {
                        OrderLoadState.Content(result.data.items)
                    }
                )
                is ApiResult.Failure -> _uiState.value.copy(
                    loadState = OrderLoadState.Error(result.error.message)
                )
            }
        }
    }

    fun requestCancel(orderId: String) {
        _uiState.value = _uiState.value.copy(cancelTargetOrderId = orderId, message = null)
    }

    fun dismissCancel() {
        if (_uiState.value.isCancelling) return
        _uiState.value = _uiState.value.copy(cancelTargetOrderId = null)
    }

    fun confirmCancel() {
        val orderId = _uiState.value.cancelTargetOrderId ?: return
        if (_uiState.value.isCancelling) return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isCancelling = true, message = null)
            when (val result = repository.cancelOrder(orderId, UUID.randomUUID().toString())) {
                is ApiResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        cancelTargetOrderId = null,
                        isCancelling = false,
                        message = result.data.message,
                        cancelledOrderId = orderId
                    )
                    _mutations.tryEmit(Unit)
                    refresh()
                }
                is ApiResult.Failure -> {
                    _uiState.value = _uiState.value.copy(
                        isCancelling = false,
                        message = result.error.message
                    )
                }
            }
        }
    }

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null)
    }

    fun consumeCancelledOrder() {
        _uiState.value = _uiState.value.copy(cancelledOrderId = null)
    }

    class Factory(private val repository: OrderRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            check(modelClass.isAssignableFrom(OrderViewModel::class.java))
            return OrderViewModel(repository) as T
        }
    }
}

class OrderDetailViewModel(
    private val orderId: String,
    private val repository: OrderRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<OrderDetailUiState>(OrderDetailUiState.Loading)
    val uiState: StateFlow<OrderDetailUiState> = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = OrderDetailUiState.Loading
            _uiState.value = when (val result = repository.getOrder(orderId)) {
                is ApiResult.Success -> OrderDetailUiState.Content(result.data)
                is ApiResult.Failure -> OrderDetailUiState.Error(result.error.message)
            }
        }
    }

    class Factory(
        private val orderId: String,
        private val repository: OrderRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            check(modelClass.isAssignableFrom(OrderDetailViewModel::class.java))
            return OrderDetailViewModel(orderId, repository) as T
        }
    }
}
