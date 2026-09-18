package com.example.localshop.feature.auth.presentation.state

data class LoginUiState(
    val isLoading: Boolean = false,
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)