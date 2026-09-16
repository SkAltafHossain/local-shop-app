package com.example.localshop.feature.auth.presentation.state

data class RegisterUiState(
    val isLoading: Boolean = false,
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val passwordConfirmation: String = "",
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)