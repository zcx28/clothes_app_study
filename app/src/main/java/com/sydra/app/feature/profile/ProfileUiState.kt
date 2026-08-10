package com.sydra.app.feature.profile

import com.sydra.app.domain.model.ProfileModel

sealed interface ProfileUiState {
    data object Loading : ProfileUiState

    data class Content(val profile: ProfileModel) : ProfileUiState

    data class Error(val message: String) : ProfileUiState
}
