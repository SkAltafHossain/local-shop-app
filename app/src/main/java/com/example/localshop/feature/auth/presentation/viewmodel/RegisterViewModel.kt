package com.example.localshop.feature.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.localshop.core.auth.SessionManager
import com.example.localshop.core.result.ResultState
import com.example.localshop.feature.auth.domain.usecase.RegisterUserUseCase
import com.example.localshop.feature.auth.presentation.state.RegisterUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()
    
    fun onNameChanged(name: String) {
        _uiState.value = _uiState.value.copy(name = name)
    }
    
    fun onEmailChanged(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
    }
    
    fun onPasswordChanged(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }
    
    fun onPasswordConfirmationChanged(passwordConfirmation: String) {
        _uiState.value = _uiState.value.copy(passwordConfirmation = passwordConfirmation)
    }
    
    fun togglePasswordVisibility() {
        _uiState.value = _uiState.value.copy(isPasswordVisible = !_uiState.value.isPasswordVisible)
    }
    
    fun togglePasswordConfirmationVisibility() {
        _uiState.value = _uiState.value.copy(isPasswordConfirmationVisible = !_uiState.value.isPasswordConfirmationVisible)
    }
    
    fun register() {
        val name = _uiState.value.name.trim()
        val email = _uiState.value.email.trim()
        val password = _uiState.value.password
        val passwordConfirmation = _uiState.value.passwordConfirmation
        
        if (name.isBlank() || email.isBlank() || password.isBlank() || passwordConfirmation.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Please fill in all fields")
            return
        }
        
        if (password != passwordConfirmation) {
            _uiState.value = _uiState.value.copy(errorMessage = "Passwords do not match")
            return
        }
        
        if (password.length < 6) {
            _uiState.value = _uiState.value.copy(errorMessage = "Password must be at least 6 characters")
            return
        }
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            
            registerUserUseCase(name, email, password, passwordConfirmation).collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        sessionManager.setCurrentUser(result.data.user)
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isSuccess = true
                        )
                    }
                    is ResultState.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                    ResultState.Loading -> {
                        // Keep loading state
                    }
                }
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}