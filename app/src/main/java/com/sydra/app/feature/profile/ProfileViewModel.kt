package com.sydra.app.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sydra.app.data.api.ApiResult
import com.sydra.app.data.api.SydraApi
import com.sydra.app.data.mapper.toModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(private val api: SydraApi) : ViewModel() {
    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading
            _uiState.value = when (val result = api.fetchProfile()) {
                is ApiResult.Success -> ProfileUiState.Content(result.data.toModel())
                is ApiResult.Failure -> ProfileUiState.Error(result.error.message)
            }
        }
    }

    class Factory(private val api: SydraApi) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            check(modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
                "Unsupported ViewModel: ${modelClass.name}"
            }
            return ProfileViewModel(api) as T
        }
    }
}
