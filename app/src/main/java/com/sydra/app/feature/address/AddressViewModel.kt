package com.sydra.app.feature.address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sydra.app.data.api.ApiResult
import com.sydra.app.data.repository.AddressRepository
import com.sydra.app.domain.model.Address
import com.sydra.app.domain.model.AddressDraft
import java.util.UUID
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddressViewModel(private val repository: AddressRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(AddressUiState())
    val uiState: StateFlow<AddressUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<AddressEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<AddressEvent> = _events.asSharedFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, loadError = null)
            _uiState.value = when (val result = repository.getAddresses()) {
                is ApiResult.Success -> _uiState.value.copy(
                    isLoading = false,
                    addresses = result.data,
                    loadError = null
                )
                is ApiResult.Failure -> _uiState.value.copy(
                    isLoading = false,
                    loadError = result.error.message
                )
            }
        }
    }

    fun addressById(addressId: String?): Address? =
        addressId?.let { id -> _uiState.value.addresses.firstOrNull { it.id == id } }

    fun prepareForm() {
        _uiState.value = _uiState.value.copy(fieldErrors = emptyMap(), message = null)
    }

    fun saveAddress(address: AddressDraft) {
        if (_uiState.value.isSaving) return
        val errors = validateAddressDraft(address)
        if (errors.isNotEmpty()) {
            _uiState.value = _uiState.value.copy(fieldErrors = errors, message = "请检查地址信息")
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isSaving = true,
                fieldErrors = emptyMap(),
                message = null
            )
            when (val result = repository.saveAddress(address, UUID.randomUUID().toString())) {
                is ApiResult.Success -> {
                    val updated = (_uiState.value.addresses.filterNot { it.id == result.data.id } + result.data)
                        .sortedByDescending { it.isDefault }
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        addresses = updated,
                        message = "地址已保存"
                    )
                    _events.tryEmit(AddressEvent.Saved(result.data.id))
                }
                is ApiResult.Failure -> {
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        fieldErrors = result.error.fieldErrors,
                        message = result.error.message
                    )
                }
            }
        }
    }

    fun setDefault(addressId: String) {
        if (_uiState.value.isMutating) return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isMutating = true, message = null)
            when (val result = repository.setDefault(addressId)) {
                is ApiResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isMutating = false,
                        addresses = _uiState.value.addresses.map {
                            it.copy(isDefault = it.id == result.data.id)
                        }.sortedByDescending { it.isDefault },
                        message = "已设为默认地址"
                    )
                }
                is ApiResult.Failure -> _uiState.value = _uiState.value.copy(
                    isMutating = false,
                    message = result.error.message
                )
            }
        }
    }

    fun requestDelete(addressId: String) {
        _uiState.value = _uiState.value.copy(deleteTargetId = addressId, message = null)
    }

    fun dismissDelete() {
        if (_uiState.value.isMutating) return
        _uiState.value = _uiState.value.copy(deleteTargetId = null)
    }

    fun confirmDelete() {
        val addressId = _uiState.value.deleteTargetId ?: return
        if (_uiState.value.isMutating) return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isMutating = true, message = null)
            when (val result = repository.deleteAddress(addressId)) {
                is ApiResult.Success -> _uiState.value = _uiState.value.copy(
                    isMutating = false,
                    deleteTargetId = null,
                    addresses = _uiState.value.addresses.filterNot { it.id == addressId },
                    message = "地址已删除"
                )
                is ApiResult.Failure -> _uiState.value = _uiState.value.copy(
                    isMutating = false,
                    message = result.error.message
                )
            }
        }
    }

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null)
    }

    class Factory(private val repository: AddressRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            check(modelClass.isAssignableFrom(AddressViewModel::class.java))
            return AddressViewModel(repository) as T
        }
    }
}
