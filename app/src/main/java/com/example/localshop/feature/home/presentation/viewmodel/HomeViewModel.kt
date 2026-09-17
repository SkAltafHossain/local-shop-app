package com.example.localshop.feature.home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.localshop.core.result.ResultState
import com.example.localshop.feature.home.domain.usecase.GetHomeDataUseCase
import com.example.localshop.feature.home.domain.usecase.GetShopInfoUseCase
import com.example.localshop.feature.home.domain.usecase.GetShopSettingsUseCase
import com.example.localshop.feature.home.presentation.state.HomeUiState
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
    private val getHomeDataUseCase: GetHomeDataUseCase
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
    }
}