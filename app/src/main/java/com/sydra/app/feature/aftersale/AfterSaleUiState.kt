package com.sydra.app.feature.aftersale

import com.sydra.app.domain.model.AfterSaleRequest

sealed interface AfterSaleListUiState {
    data object Loading : AfterSaleListUiState
    data class Content(val requests: List<AfterSaleRequest>) : AfterSaleListUiState
    data object Empty : AfterSaleListUiState
    data class Error(val message: String) : AfterSaleListUiState
}

sealed interface AfterSaleDetailUiState {
    data object Loading : AfterSaleDetailUiState
    data class Content(val request: AfterSaleRequest) : AfterSaleDetailUiState
    data class Error(val message: String) : AfterSaleDetailUiState
}

enum class ReturnStep {
    CONFIRM_ITEM,
    SELECT_REASON,
    EVIDENCE
}

data class ReturnRequestUiState(
    val orderId: String,
    val orderItemId: String,
    val step: ReturnStep = ReturnStep.CONFIRM_ITEM,
    val reason: String? = null,
    val description: String = "",
    val evidenceIds: List<String> = emptyList(),
    val isSubmitting: Boolean = false,
    val validationMessage: String? = null,
    val submitError: String? = null,
    val submittedRequestId: String? = null
)
