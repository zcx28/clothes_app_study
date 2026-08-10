package com.sydra.app.feature.address

import com.sydra.app.domain.model.Address

data class AddressUiState(
    val isLoading: Boolean = true,
    val addresses: List<Address> = emptyList(),
    val loadError: String? = null,
    val isSaving: Boolean = false,
    val isMutating: Boolean = false,
    val fieldErrors: Map<String, String> = emptyMap(),
    val message: String? = null,
    val deleteTargetId: String? = null
)

sealed interface AddressEvent {
    data class Saved(val addressId: String) : AddressEvent
}
