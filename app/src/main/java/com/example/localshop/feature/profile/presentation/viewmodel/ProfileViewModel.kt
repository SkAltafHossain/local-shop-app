package com.example.localshop.feature.profile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.localshop.core.auth.SessionManager
import com.example.localshop.core.result.ResultState
import com.example.localshop.feature.auth.domain.usecase.DeleteAccountUseCase
import com.example.localshop.feature.auth.domain.usecase.LogoutUseCase
import com.example.localshop.feature.auth.domain.usecase.UpdateEmailUseCase
import com.example.localshop.feature.auth.domain.usecase.UpdatePasswordUseCase
import com.example.localshop.feature.auth.domain.usecase.UpdatePhoneUseCase
import com.example.localshop.feature.auth.domain.usecase.UpdateUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val logoutUseCase: LogoutUseCase,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase,
    private val updateEmailUseCase: UpdateEmailUseCase,
    private val updatePhoneUseCase: UpdatePhoneUseCase,
    private val updatePasswordUseCase: UpdatePasswordUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase
) : ViewModel() {

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _currentUser = MutableStateFlow<com.example.localshop.feature.auth.domain.model.User?>(null)
    val currentUser: StateFlow<com.example.localshop.feature.auth.domain.model.User?> = _currentUser.asStateFlow()

    private val _isLoggingOut = MutableStateFlow(false)
    val isLoggingOut: StateFlow<Boolean> = _isLoggingOut.asStateFlow()

    private val _logoutError = MutableStateFlow<String?>(null)
    val logoutError: StateFlow<String?> = _logoutError.asStateFlow()

    // Modal states
    private val _showNameModal = MutableStateFlow(false)
    val showNameModal: StateFlow<Boolean> = _showNameModal.asStateFlow()

    private val _showEmailModal = MutableStateFlow(false)
    val showEmailModal: StateFlow<Boolean> = _showEmailModal.asStateFlow()

    private val _showPhoneModal = MutableStateFlow(false)
    val showPhoneModal: StateFlow<Boolean> = _showPhoneModal.asStateFlow()

    private val _showPasswordModal = MutableStateFlow(false)
    val showPasswordModal: StateFlow<Boolean> = _showPasswordModal.asStateFlow()

    private val _showDeleteAccountModal = MutableStateFlow(false)
    val showDeleteAccountModal: StateFlow<Boolean> = _showDeleteAccountModal.asStateFlow()

    // Loading states
    private val _isUpdatingName = MutableStateFlow(false)
    val isUpdatingName: StateFlow<Boolean> = _isUpdatingName.asStateFlow()

    private val _isUpdatingEmail = MutableStateFlow(false)
    val isUpdatingEmail: StateFlow<Boolean> = _isUpdatingEmail.asStateFlow()

    private val _isUpdatingPhone = MutableStateFlow(false)
    val isUpdatingPhone: StateFlow<Boolean> = _isUpdatingPhone.asStateFlow()

    private val _isUpdatingPassword = MutableStateFlow(false)
    val isUpdatingPassword: StateFlow<Boolean> = _isUpdatingPassword.asStateFlow()

    private val _isDeletingAccount = MutableStateFlow(false)
    val isDeletingAccount: StateFlow<Boolean> = _isDeletingAccount.asStateFlow()

    // Error states
    private val _updateError = MutableStateFlow<String?>(null)
    val updateError: StateFlow<String?> = _updateError.asStateFlow()

    private val _updateSuccess = MutableStateFlow<String?>(null)
    val updateSuccess: StateFlow<String?> = _updateSuccess.asStateFlow()

    init {
        viewModelScope.launch {
            sessionManager.isLoggedIn.collect { loggedIn ->
                _isLoggedIn.value = loggedIn
            }
        }

        viewModelScope.launch {
            sessionManager.currentUser.collect { user ->
                _currentUser.value = user
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _isLoggingOut.value = true
            _logoutError.value = null

            logoutUseCase().collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        _isLoggingOut.value = false
                        sessionManager.clearSession()
                    }
                    is ResultState.Error -> {
                        _isLoggingOut.value = false
                        _logoutError.value = result.message
                    }
                    ResultState.Loading -> {
                        // Keep loading state
                    }
                }
            }
        }
    }

    fun clearLogoutError() {
        _logoutError.value = null
    }

    // Modal controls
    fun showNameModal() { _showNameModal.value = true }
    fun hideNameModal() { _showNameModal.value = false }

    fun showEmailModal() { _showEmailModal.value = true }
    fun hideEmailModal() { _showEmailModal.value = false }

    fun showPhoneModal() { _showPhoneModal.value = true }
    fun hidePhoneModal() { _showPhoneModal.value = false }

    fun showPasswordModal() { _showPasswordModal.value = true }
    fun hidePasswordModal() { _showPasswordModal.value = false }

    fun showDeleteAccountModal() { _showDeleteAccountModal.value = true }
    fun hideDeleteAccountModal() { _showDeleteAccountModal.value = false }

    // Update functions
    fun updateName(name: String) {
        viewModelScope.launch {
            _isUpdatingName.value = true
            _updateError.value = null

            updateUserProfileUseCase(name).collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        _isUpdatingName.value = false
                        _currentUser.value = result.data
                        _updateSuccess.value = "Name updated successfully"
                        _showNameModal.value = false
                    }
                    is ResultState.Error -> {
                        _isUpdatingName.value = false
                        _updateError.value = result.message
                    }
                    ResultState.Loading -> {
                        // Keep loading state
                    }
                }
            }
        }
    }

    fun updateEmail(email: String, password: String) {
        viewModelScope.launch {
            _isUpdatingEmail.value = true
            _updateError.value = null

            updateEmailUseCase(email, password).collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        _isUpdatingEmail.value = false
                        _currentUser.value = result.data
                        _updateSuccess.value = "Email updated successfully"
                        _showEmailModal.value = false
                    }
                    is ResultState.Error -> {
                        _isUpdatingEmail.value = false
                        _updateError.value = result.message
                    }
                    ResultState.Loading -> {
                        // Keep loading state
                    }
                }
            }
        }
    }

    fun updatePhone(phone: String, password: String) {
        viewModelScope.launch {
            _isUpdatingPhone.value = true
            _updateError.value = null

            updatePhoneUseCase(phone, password).collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        _isUpdatingPhone.value = false
                        _currentUser.value = result.data
                        _updateSuccess.value = "Phone updated successfully"
                        _showPhoneModal.value = false
                    }
                    is ResultState.Error -> {
                        _isUpdatingPhone.value = false
                        _updateError.value = result.message
                    }
                    ResultState.Loading -> {
                        // Keep loading state
                    }
                }
            }
        }
    }

    fun updatePassword(currentPassword: String, newPassword: String, newPasswordConfirmation: String) {
        viewModelScope.launch {
            _isUpdatingPassword.value = true
            _updateError.value = null

            updatePasswordUseCase(currentPassword, newPassword, newPasswordConfirmation).collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        _isUpdatingPassword.value = false
                        _updateSuccess.value = "Password updated successfully"
                        _showPasswordModal.value = false
                    }
                    is ResultState.Error -> {
                        _isUpdatingPassword.value = false
                        _updateError.value = result.message
                    }
                    ResultState.Loading -> {
                        // Keep loading state
                    }
                }
            }
        }
    }

    fun deleteAccount(password: String) {
        viewModelScope.launch {
            _isDeletingAccount.value = true
            _updateError.value = null

            deleteAccountUseCase(password).collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        _isDeletingAccount.value = false
                        sessionManager.clearSession()
                        _showDeleteAccountModal.value = false
                    }
                    is ResultState.Error -> {
                        _isDeletingAccount.value = false
                        _updateError.value = result.message
                    }
                    ResultState.Loading -> {
                        // Keep loading state
                    }
                }
            }
        }
    }

    fun clearUpdateError() {
        _updateError.value = null
    }

    fun clearUpdateSuccess() {
        _updateSuccess.value = null
    }
}
