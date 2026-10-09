package com.example.localshop.feature.product.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.localshop.core.auth.SessionManager
import com.example.localshop.core.result.ResultState
import com.example.localshop.feature.cart.domain.usecase.AddToCartUseCase
import com.example.localshop.feature.cart.domain.usecase.GetCartUseCase
import com.example.localshop.feature.product.domain.model.Product
import com.example.localshop.feature.product.domain.model.ProductFilters
import com.example.localshop.feature.product.domain.usecase.GetProductsUseCase
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
class ProductViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val getCartUseCase: GetCartUseCase,
    private val getWishlistUseCase: GetWishlistUseCase,
    private val addToWishlistUseCase: AddToWishlistUseCase,
    private val removeFromWishlistUseCase: RemoveFromWishlistUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ProductUiState())
    val uiState: StateFlow<ProductUiState> = _uiState.asStateFlow()
    val isLoggedIn = sessionManager.isLoggedIn
    
    private var currentFilters: ProductFilters = ProductFilters()
    
    init {
        // Don't load by default - let the screen specify what to load
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
    
    fun loadProducts() {
        loadCartIfLoggedIn()
        loadWishlistIfLoggedIn()
        loadProducts(ProductFilters())
    }

    fun loadProducts(filters: ProductFilters, reset: Boolean = true) {
        viewModelScope.launch {
            if (reset) {
                loadCartIfLoggedIn()
                loadWishlistIfLoggedIn()
            }
            currentFilters = if (reset) filters else currentFilters.copy(page = filters.page)
            
            if (reset) {
                _uiState.value = _uiState.value.copy(
                    isLoading = true,
                    products = emptyList(),
                    errorMessage = null,
                    currentPage = 1,
                    lastPage = 1,
                    total = 0
                )
            } else {
                _uiState.value = _uiState.value.copy(isLoadingMore = true, errorMessage = null)
            }
            
            getProductsUseCase(currentFilters).collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        val newProducts = if (reset) {
                            result.data.items
                        } else {
                            _uiState.value.products + result.data.items
                        }
                        
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isLoadingMore = false,
                            products = newProducts,
                            currentPage = result.data.currentPage,
                            lastPage = result.data.lastPage,
                            total = result.data.total,
                            perPage = result.data.perPage
                        )
                    }
                    is ResultState.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isLoadingMore = false,
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
    
    fun loadLatestProducts() {
        loadCartIfLoggedIn()
        loadWishlistIfLoggedIn()
        loadProducts(ProductFilters(isNew = true))
    }

    fun loadFeaturedProducts() {
        loadCartIfLoggedIn()
        loadWishlistIfLoggedIn()
        loadProducts(ProductFilters(isFeatured = true))
    }
    
    fun loadMoreProducts() {
        val currentState = _uiState.value
        if (!currentState.isLoadingMore && currentState.currentPage < currentState.lastPage) {
            loadProducts(currentFilters.copy(page = currentState.currentPage + 1), reset = false)
        }
    }
    
    fun updateFilters(filters: ProductFilters) {
        println("Updating filters: categorySlug=${filters.categorySlug}, minPrice=${filters.minPrice}, maxPrice=${filters.maxPrice}, sort=${filters.sort}, isNew=${filters.isNew}, isFeatured=${filters.isFeatured}")
        loadProducts(filters, reset = true)
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
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

data class ProductUiState(
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val products: List<Product> = emptyList(),
    val errorMessage: String? = null,
    val currentPage: Int = 1,
    val lastPage: Int = 1,
    val total: Int = 0,
    val perPage: Int = 12,
    val isAddingToCart: Boolean = false,
    val addToCartSuccess: Boolean = false,
    val addToCartMessage: String? = null,
    val cartProductIds: Set<Int> = emptySet(),
    val wishlistProductIds: Set<Int> = emptySet(),
    val wishlistItems: Map<Int, Int> = emptyMap() // productId to wishlistItemId
)
