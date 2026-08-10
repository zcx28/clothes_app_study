package com.sydra.app.feature.logistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sydra.app.data.api.ApiResult
import com.sydra.app.data.repository.LogisticsRepository
import com.sydra.app.domain.model.Logistics
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface LogisticsUiState {
    data object Loading : LogisticsUiState
    data class Content(val logistics: Logistics) : LogisticsUiState
    data class Error(val message: String) : LogisticsUiState
}

class LogisticsViewModel(
    private val orderId: String,
    private val repository: LogisticsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<LogisticsUiState>(LogisticsUiState.Loading)
    val uiState: StateFlow<LogisticsUiState> = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = LogisticsUiState.Loading
            _uiState.value = when (val result = repository.getLogistics(orderId)) {
                is ApiResult.Success -> LogisticsUiState.Content(result.data)
                is ApiResult.Failure -> LogisticsUiState.Error(result.error.message)
            }
        }
    }

    class Factory(
        private val orderId: String,
        private val repository: LogisticsRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            check(modelClass.isAssignableFrom(LogisticsViewModel::class.java))
            return LogisticsViewModel(orderId, repository) as T
        }
    }
}
