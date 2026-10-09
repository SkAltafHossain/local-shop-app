package com.example.localshop.feature.product.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.localshop.core.auth.SessionManager
import com.example.localshop.core.result.ResultState
import com.example.localshop.feature.cart.domain.usecase.AddToCartUseCase
import com.example.localshop.feature.cart.domain.usecase.GetCartUseCase
import com.example.localshop.feature.product.domain.usecase.GetProductWithRelatedUseCase
import com.example.localshop.feature.product.presentation.state.ProductDetailsUiState
import com.example.localshop.feature.wishlist.domain.usecase.AddToWishlistUseCase
import com.example.localshop.feature.wishlist.domain.usecase.CheckInWishlistUseCase
import com.example.localshop.feature.wishlist.domain.usecase.GetWishlistUseCase
import com.example.localshop.feature.wishlist.domain.usecase.RemoveFromWishlistUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val getProductWithRelatedUseCase: GetProductWithRelatedUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val getCartUseCase: GetCartUseCase,
    private val getWishlistUseCase: GetWishlistUseCase,
    private val checkInWishlistUseCase: CheckInWishlistUseCase,
    private val addToWishlistUseCase: AddToWishlistUseCase,
    private val removeFromWishlistUseCase: RemoveFromWishlistUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ProductDetailsUiState())
    val uiState: StateFlow<ProductDetailsUiState> = _uiState.asStateFlow()
    val isLoggedIn = sessionManager.isLoggedIn
    
    fun loadProductDetails(productId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            getProductWithRelatedUseCase(productId).collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        _uiState.value = _uiState.value.copy(
                            productDetails = result.data,
                            isLoading = false
                        )
                        // Check if product is in cart
                        checkIfProductInCart(productId)
                        // Check if product is in wishlist
                        checkIfProductInWishlist(productId)
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

    private fun checkIfProductInCart(productId: Int) {
        viewModelScope.launch {
            sessionManager.isLoggedIn.collect { isLoggedIn ->
                if (isLoggedIn) {
                    getCartUseCase().collect { result ->
                        when (result) {
                            is ResultState.Success -> {
                                val cartProductIds = result.data.items.map { it.productId }.toSet()
                                val isInCart = cartProductIds.contains(productId)
                                _uiState.value = _uiState.value.copy(
                                    isInCart = isInCart,
                                    cartProductIds = cartProductIds
                                )
                            }
                            else -> {
                                // Ignore errors for cart check
                            }
                        }
                    }
                } else {
                    // Clear cart data if not logged in
                    _uiState.value = _uiState.value.copy(
                        isInCart = false,
                        cartProductIds = emptySet()
                    )
                }
            }
        }
    }

    private fun checkIfProductInWishlist(productId: Int) {
        viewModelScope.launch {
            sessionManager.isLoggedIn.collect { isLoggedIn ->
                if (isLoggedIn) {
                    checkInWishlistUseCase(productId).collect { result ->
                        when (result) {
                            is ResultState.Success -> {
                                val isInWishlist = result.data
                                _uiState.value = _uiState.value.copy(isInWishlist = isInWishlist)
                            }
                            else -> {
                                // Ignore errors for wishlist check
                            }
                        }
                    }
                } else {
                    // Clear wishlist data if not logged in
                    _uiState.value = _uiState.value.copy(
                        isInWishlist = false,
                        wishlistItemId = null
                    )
                }
            }
        }
    }
    
    fun refresh() {
        _uiState.value.productDetails?.product?.id?.let { productId ->
            loadProductDetails(productId)
        }
    }
    
    fun addToCart(productId: Int, quantity: Int = 1) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isAddingToCart = true)

            addToCartUseCase(productId, quantity).collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isAddingToCart = false,
                            addToCartSuccess = true,
                            addToCartMessage = "Item added to cart",
                            isInCart = true,
                            cartProductIds = _uiState.value.cartProductIds + productId
                        )
                    }
                    is ResultState.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isAddingToCart = false,
                            addToCartSuccess = false,
                            addToCartMessage = result.message
                        )
                    }
                    ResultState.Loading -> {
                        // Keep loading state
                    }
                }
            }
        }
    }
    
    fun clearAddToCartMessage() {
        _uiState.value = _uiState.value.copy(
            addToCartMessage = null,
            addToCartSuccess = false
        )
    }

    fun addToWishlist(productId: Int) {
        viewModelScope.launch {
            val loggedIn = sessionManager.isLoggedIn.first()
            if (!loggedIn) {
                return@launch
            }

            addToWishlistUseCase(productId).collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isInWishlist = true,
                            wishlistItemId = result.data.id
                        )
                    }
                    is ResultState.Error -> {
                        println("Error adding to wishlist: ${result.message}")
                    }
                    ResultState.Loading -> {
                        // Loading state
                    }
                }
            }
        }
    }

    fun removeFromWishlist() {
        viewModelScope.launch {
            val loggedIn = sessionManager.isLoggedIn.first()
            if (!loggedIn) {
                return@launch
            }

            val wishlistItemId = _uiState.value.wishlistItemId
            if (wishlistItemId != null) {
                removeFromWishlistUseCase(wishlistItemId).collect { result ->
                    when (result) {
                        is ResultState.Success -> {
                            _uiState.value = _uiState.value.copy(
                                isInWishlist = false,
                                wishlistItemId = null
                            )
                        }
                        is ResultState.Error -> {
                            println("Error removing from wishlist: ${result.message}")
                        }
                        ResultState.Loading -> {
                            // Loading state
                        }
                    }
                }
            }
        }
    }
}