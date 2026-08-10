package com.sydra.app.feature.aftersale

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sydra.app.data.api.ApiResult
import com.sydra.app.data.repository.AfterSaleRepository
import java.util.UUID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AfterSaleListViewModel(private val repository: AfterSaleRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<AfterSaleListUiState>(AfterSaleListUiState.Loading)
    val uiState: StateFlow<AfterSaleListUiState> = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = AfterSaleListUiState.Loading
            _uiState.value = when (val result = repository.getRequests()) {
                is ApiResult.Success -> if (result.data.items.isEmpty()) {
                    AfterSaleListUiState.Empty
                } else {
                    AfterSaleListUiState.Content(result.data.items)
                }
                is ApiResult.Failure -> AfterSaleListUiState.Error(result.error.message)
            }
        }
    }

    class Factory(private val repository: AfterSaleRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            check(modelClass.isAssignableFrom(AfterSaleListViewModel::class.java))
            return AfterSaleListViewModel(repository) as T
        }
    }
}

class AfterSaleDetailViewModel(
    private val requestId: String,
    private val repository: AfterSaleRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<AfterSaleDetailUiState>(AfterSaleDetailUiState.Loading)
    val uiState: StateFlow<AfterSaleDetailUiState> = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = AfterSaleDetailUiState.Loading
            _uiState.value = when (val result = repository.getRequest(requestId)) {
                is ApiResult.Success -> AfterSaleDetailUiState.Content(result.data)
                is ApiResult.Failure -> AfterSaleDetailUiState.Error(result.error.message)
            }
        }
    }

    class Factory(
        private val requestId: String,
        private val repository: AfterSaleRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            check(modelClass.isAssignableFrom(AfterSaleDetailViewModel::class.java))
            return AfterSaleDetailViewModel(requestId, repository) as T
        }
    }
}

class ReturnRequestViewModel(
    orderId: String,
    orderItemId: String,
    private val repository: AfterSaleRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ReturnRequestUiState(orderId, orderItemId))
    val uiState: StateFlow<ReturnRequestUiState> = _uiState.asStateFlow()

    private val idempotencyKey = UUID.randomUUID().toString()

    fun nextFromConfirmation() {
        _uiState.value = _uiState.value.copy(
            step = ReturnStep.SELECT_REASON,
            validationMessage = null,
            submitError = null
        )
    }

    fun selectReason(reason: String) {
        _uiState.value = _uiState.value.copy(
            reason = reason,
            validationMessage = null,
            submitError = null
        )
    }

    fun continueFromReason() {
        val reason = _uiState.value.reason
        if (reason == null) {
            _uiState.value = _uiState.value.copy(validationMessage = "请选择退货原因")
            return
        }
        if (reason == OTHER_REASON) {
            _uiState.value = _uiState.value.copy(
                step = ReturnStep.EVIDENCE,
                validationMessage = null
            )
        } else {
            submit()
        }
    }

    fun updateDescription(value: String) {
        _uiState.value = _uiState.value.copy(description = value, validationMessage = null)
    }

    fun addEvidence(uri: String) {
        val current = _uiState.value.evidenceIds
        if (uri in current) return
        if (current.size >= MAX_EVIDENCE) {
            _uiState.value = _uiState.value.copy(validationMessage = "本地原型最多选择 3 个凭证")
            return
        }
        _uiState.value = _uiState.value.copy(
            evidenceIds = current + uri,
            validationMessage = null
        )
    }

    fun removeEvidence(uri: String) {
        _uiState.value = _uiState.value.copy(
            evidenceIds = _uiState.value.evidenceIds - uri,
            validationMessage = null
        )
    }

    fun submit() {
        val state = _uiState.value
        if (state.isSubmitting || state.submittedRequestId != null) return
        val reason = state.reason
        if (reason == null) {
            _uiState.value = state.copy(validationMessage = "请选择退货原因")
            return
        }
        if (reason == OTHER_REASON && state.description.isBlank()) {
            _uiState.value = state.copy(validationMessage = "请描述遇到的问题")
            return
        }
        if (reason == OTHER_REASON && state.evidenceIds.isEmpty()) {
            _uiState.value = state.copy(validationMessage = "本地原型要求至少选择 1 个凭证")
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isSubmitting = true,
                validationMessage = null,
                submitError = null
            )
            _uiState.value = when (val result = repository.submitReturn(
                orderId = state.orderId,
                orderItemId = state.orderItemId,
                reason = reason,
                description = state.description.ifBlank { null },
                evidenceIds = state.evidenceIds,
                idempotencyKey = idempotencyKey
            )) {
                is ApiResult.Success -> _uiState.value.copy(
                    isSubmitting = false,
                    submittedRequestId = result.data.id
                )
                is ApiResult.Failure -> _uiState.value.copy(
                    isSubmitting = false,
                    submitError = result.error.message
                )
            }
        }
    }

    fun previousStep(): Boolean {
        val state = _uiState.value
        if (state.isSubmitting) return true
        return when (state.step) {
            ReturnStep.CONFIRM_ITEM -> false
            ReturnStep.SELECT_REASON -> {
                _uiState.value = state.copy(step = ReturnStep.CONFIRM_ITEM, validationMessage = null)
                true
            }
            ReturnStep.EVIDENCE -> {
                _uiState.value = state.copy(step = ReturnStep.SELECT_REASON, validationMessage = null)
                true
            }
        }
    }

    class Factory(
        private val orderId: String,
        private val orderItemId: String,
        private val repository: AfterSaleRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            check(modelClass.isAssignableFrom(ReturnRequestViewModel::class.java))
            return ReturnRequestViewModel(orderId, orderItemId, repository) as T
        }
    }

    companion object {
        const val OTHER_REASON = "其他"
        private const val MAX_EVIDENCE = 3
    }
}
