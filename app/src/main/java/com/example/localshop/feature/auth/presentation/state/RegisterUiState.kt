package com.example.localshop.feature.auth.presentation.state

data class RegisterUiState(
    val isLoading: Boolean = false,
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val password: String = "",
    val passwordConfirmation: String = "",
    val isPasswordVisible: Boolean = false,
    val isPasswordConfirmationVisible: Boolean = false,
    val errorMessage: String? = null,
    val showErrorModal: Boolean = false,
    val isSuccess: Boolean = false
)