package com.example.localshop.feature.product.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.localshop.core.result.ResultState
import com.example.localshop.feature.product.domain.model.Product
import com.example.localshop.feature.product.domain.model.ProductFilters
import com.example.localshop.feature.product.domain.usecase.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ProductUiState())
    val uiState: StateFlow<ProductUiState> = _uiState.asStateFlow()
    
    private var currentFilters: ProductFilters = ProductFilters()
    
    init {
        // Don't load by default - let the screen specify what to load
    }
    
    fun loadProducts() {
        loadProducts(ProductFilters())
    }
    
    fun loadProducts(filters: ProductFilters, reset: Boolean = true) {
        viewModelScope.launch {
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
        loadProducts(ProductFilters(isNew = true))
    }
    
    fun loadFeaturedProducts() {
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
}

data class ProductUiState(
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val products: List<Product> = emptyList(),
    val errorMessage: String? = null,
    val currentPage: Int = 1,
    val lastPage: Int = 1,
    val total: Int = 0,
    val perPage: Int = 12
)
