package com.example.localshop.feature.home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.localshop.core.auth.SessionManager
import com.example.localshop.core.result.ResultState
import com.example.localshop.feature.cart.domain.usecase.AddToCartUseCase
import com.example.localshop.feature.cart.domain.usecase.GetCartUseCase
import com.example.localshop.feature.home.domain.usecase.GetHomeDataUseCase
import com.example.localshop.feature.home.domain.usecase.GetShopInfoUseCase
import com.example.localshop.feature.home.domain.usecase.GetShopSettingsUseCase
import com.example.localshop.feature.home.presentation.state.HomeUiState
import com.example.localshop.feature.wishlist.domain.usecase.AddToWishlistUseCase
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
class HomeViewModel @Inject constructor(
    private val getShopSettingsUseCase: GetShopSettingsUseCase,
    private val getShopInfoUseCase: GetShopInfoUseCase,
    private val getHomeDataUseCase: GetHomeDataUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val getCartUseCase: GetCartUseCase,
    private val getWishlistUseCase: GetWishlistUseCase,
    private val addToWishlistUseCase: AddToWishlistUseCase,
    private val removeFromWishlistUseCase: RemoveFromWishlistUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    val isLoggedIn = sessionManager.isLoggedIn
    
    init {
        loadHomeData()
        loadCartIfLoggedIn()
        loadWishlistIfLoggedIn()
    }

    private fun loadCartIfLoggedIn() {
        viewModelScope.launch {
            sessionManager.isLoggedIn.collect { isLoggedIn ->
                if (isLoggedIn) {
                    getCartUseCase().collect { result ->
                        when (result) {
                            is ResultState.Success -> {
                                val cartProductIds = result.data.items.map { it.productId }.toSet()
                                _uiState.value = _uiState.value.copy(cartProductIds = cartProductIds)
                            }
                            else -> {
                                // Ignore errors for cart
                            }
                        }
                    }
                } else {
                    // Clear cart product IDs if not logged in
                    _uiState.value = _uiState.value.copy(cartProductIds = emptySet())
                }
            }
        }
    }

    private fun loadWishlistIfLoggedIn() {
        viewModelScope.launch {
            sessionManager.isLoggedIn.collect { isLoggedIn ->
                if (isLoggedIn) {
                    getWishlistUseCase().collect { result ->
                        when (result) {
                            is ResultState.Success -> {
                                val wishlistProductIds = result.data.items.map { it.productId }.toSet()
                                val wishlistItems = result.data.items.associate { it.productId to it.id }
                                _uiState.value = _uiState.value.copy(
                                    wishlistProductIds = wishlistProductIds,
                                    wishlistItems = wishlistItems
                                )
                            }
                            is ResultState.Error -> {
                                println("Error loading wishlist: ${result.message}")
                            }
                            ResultState.Loading -> {
                                // Loading state
                            }
                        }
                    }
                } else {
                    // Clear wishlist product IDs if not logged in
                    _uiState.value = _uiState.value.copy(
                        wishlistProductIds = emptySet(),
                        wishlistItems = emptyMap()
                    )
                }
            }
        }
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            // Load shop settings
            getShopSettingsUseCase().collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        _uiState.value = _uiState.value.copy(
                            shopSettings = result.data,
                            isLoading = false
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
            
            // Load shop info
            getShopInfoUseCase().collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        _uiState.value = _uiState.value.copy(shopInfo = result.data)
                    }
                    is ResultState.Error -> {
                        _uiState.value = _uiState.value.copy(errorMessage = result.message)
                    }
                    ResultState.Loading -> {
                        // Keep loading state
                    }
                }
            }
            
            // Load home data (products, categories, latest, featured)
            getHomeDataUseCase().collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        _uiState.value = _uiState.value.copy(
                            products = result.data.products,
                            categories = result.data.categories,
                            latestProducts = result.data.latestProducts,
                            featuredProducts = result.data.featuredProducts,
                            isLoading = false
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
    
    fun refresh() {
        loadHomeData()
        loadCartIfLoggedIn()
        loadWishlistIfLoggedIn()
    }
    
    fun addToCart(productId: Int, quantity: Int = 1) {
        viewModelScope.launch {
            val loggedIn = sessionManager.isLoggedIn.first()
            if (!loggedIn) {
                _uiState.value = _uiState.value.copy(
                    isAddingToCart = false,
                    addToCartSuccess = false,
                    addToCartMessage = "Please login to add items to cart"
                )
                return@launch
            }

            _uiState.value = _uiState.value.copy(isAddingToCart = true)

            addToCartUseCase(productId, quantity).collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isAddingToCart = false,
                            addToCartSuccess = true,
                            addToCartMessage = "Item added to cart",
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
                        // Reload wishlist to get updated data
                        loadWishlistIfLoggedIn()
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

    fun removeFromWishlist(productId: Int) {
        viewModelScope.launch {
            val loggedIn = sessionManager.isLoggedIn.first()
            if (!loggedIn) {
                return@launch
            }

            val wishlistItemId = _uiState.value.wishlistItems[productId]
            if (wishlistItemId != null) {
                removeFromWishlistUseCase(wishlistItemId).collect { result ->
                    when (result) {
                        is ResultState.Success -> {
                            // Reload wishlist to get updated data
                            loadWishlistIfLoggedIn()
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