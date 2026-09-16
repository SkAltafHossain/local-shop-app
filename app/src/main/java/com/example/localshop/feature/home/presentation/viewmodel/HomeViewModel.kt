package com.example.localshop.feature.home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.localshop.core.result.ResultState
import com.example.localshop.feature.home.domain.usecase.GetShopInfoUseCase
import com.example.localshop.feature.home.domain.usecase.GetShopSettingsUseCase
import com.example.localshop.feature.home.presentation.state.HomeUiState
import com.example.localshop.feature.product.domain.usecase.GetFeaturedProductsUseCase
import com.example.localshop.feature.product.domain.usecase.GetLatestProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getShopSettingsUseCase: GetShopSettingsUseCase,
    private val getShopInfoUseCase: GetShopInfoUseCase,
    private val getFeaturedProductsUseCase: GetFeaturedProductsUseCase,
    private val getLatestProductsUseCase: GetLatestProductsUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    init {
        loadHomeData()
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
            
            // Load featured products
            getFeaturedProductsUseCase(perPage = 8).collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        _uiState.value = _uiState.value.copy(
                            featuredProducts = result.data.items
                        )
                    }
                    is ResultState.Error -> {
                        _uiState.value = _uiState.value.copy(errorMessage = result.message)
                    }
                    ResultState.Loading -> {
                        // Keep loading state
                    }
                }
            }
            
            // Load latest products
            getLatestProductsUseCase(perPage = 8).collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        _uiState.value = _uiState.value.copy(
                            latestProducts = result.data.items
                        )
                    }
                    is ResultState.Error -> {
                        _uiState.value = _uiState.value.copy(errorMessage = result.message)
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
    }
}