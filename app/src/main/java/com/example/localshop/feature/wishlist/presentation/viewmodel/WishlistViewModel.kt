package com.example.localshop.feature.wishlist.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.localshop.core.auth.SessionManager
import com.example.localshop.core.result.ResultState
import com.example.localshop.feature.wishlist.domain.usecase.AddToWishlistUseCase
import com.example.localshop.feature.wishlist.domain.usecase.CheckInWishlistUseCase
import com.example.localshop.feature.wishlist.domain.usecase.GetWishlistUseCase
import com.example.localshop.feature.wishlist.domain.usecase.RemoveFromWishlistUseCase
import com.example.localshop.feature.wishlist.presentation.state.WishlistUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WishlistViewModel @Inject constructor(
    private val getWishlistUseCase: GetWishlistUseCase,
    private val addToWishlistUseCase: AddToWishlistUseCase,
    private val removeFromWishlistUseCase: RemoveFromWishlistUseCase,
    private val checkInWishlistUseCase: CheckInWishlistUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(WishlistUiState())
    val uiState: StateFlow<WishlistUiState> = _uiState.asStateFlow()
    val isLoggedIn = sessionManager.isLoggedIn

    init {
        loadWishlistIfLoggedIn()
    }

    private fun loadWishlistIfLoggedIn() {
        viewModelScope.launch {
            sessionManager.isLoggedIn.collect { isLoggedIn ->
                if (isLoggedIn) {
                    getWishlistUseCase().collect { result ->
                        when (result) {
                            is ResultState.Success -> {
                                _uiState.value = _uiState.value.copy(
                                    wishlist = result.data,
                                    isLoading = false,
                                    errorMessage = null
                                )
                            }
                            is ResultState.Error -> {
                                _uiState.value = _uiState.value.copy(
                                    errorMessage = result.message,
                                    isLoading = false
                                )
                            }
                            ResultState.Loading -> {
                                _uiState.value = _uiState.value.copy(isLoading = true)
                            }
                        }
                    }
                } else {
                    // Clear wishlist if not logged in
                    _uiState.value = _uiState.value.copy(
                        wishlist = null,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun loadWishlist() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            getWishlistUseCase().collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        _uiState.value = _uiState.value.copy(
                            wishlist = result.data,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                    is ResultState.Error -> {
                        _uiState.value = _uiState.value.copy(
                            errorMessage = result.message,
                            isLoading = false
                        )
                    }
                    ResultState.Loading -> {
                        // Keep loading state
                    }
                }
            }
        }
    }

    fun addToWishlist(productId: Int) {
        viewModelScope.launch {
            val loggedIn = sessionManager.isLoggedIn.first()
            if (!loggedIn) {
                _uiState.value = _uiState.value.copy(
                    isAddingToWishlist = false,
                    addToWishlistSuccess = false,
                    addToWishlistMessage = "Please login to add items to wishlist"
                )
                return@launch
            }

            _uiState.value = _uiState.value.copy(isAddingToWishlist = true)

            addToWishlistUseCase(productId).collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isAddingToWishlist = false,
                            addToWishlistSuccess = true,
                            addToWishlistMessage = "Item added to wishlist"
                        )
                        // Reload wishlist to get updated data
                        loadWishlist()
                    }
                    is ResultState.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isAddingToWishlist = false,
                            addToWishlistSuccess = false,
                            addToWishlistMessage = result.message
                        )
                    }
                    ResultState.Loading -> {
                        // Keep loading state
                    }
                }
            }
        }
    }

    fun removeFromWishlist(wishlistItemId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRemovingFromWishlist = true)

            removeFromWishlistUseCase(wishlistItemId).collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isRemovingFromWishlist = false,
                            removeFromWishlistMessage = "Item removed from wishlist"
                        )
                        // Reload wishlist to get updated data
                        loadWishlist()
                    }
                    is ResultState.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isRemovingFromWishlist = false,
                            removeFromWishlistMessage = result.message
                        )
                    }
                    ResultState.Loading -> {
                        // Keep loading state
                    }
                }
            }
        }
    }

    fun checkInWishlist(productId: Int, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val loggedIn = sessionManager.isLoggedIn.first()
            if (!loggedIn) {
                onResult(false)
                return@launch
            }

            checkInWishlistUseCase(productId).collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        onResult(result.data)
                    }
                    else -> {
                        onResult(false)
                    }
                }
            }
        }
    }

    fun clearAddToWishlistMessage() {
        _uiState.value = _uiState.value.copy(
            addToWishlistMessage = null,
            addToWishlistSuccess = false
        )
    }

    fun clearRemoveFromWishlistMessage() {
        _uiState.value = _uiState.value.copy(removeFromWishlistMessage = null)
    }

    fun clearErrorMessage() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}
