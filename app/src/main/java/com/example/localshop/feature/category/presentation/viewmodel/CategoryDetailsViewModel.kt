package com.example.localshop.feature.category.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.localshop.core.result.ResultState
import com.example.localshop.feature.category.domain.usecase.GetCategoryWithProductsUseCase
import com.example.localshop.feature.category.presentation.state.CategoryDetailsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryDetailsViewModel @Inject constructor(
    private val getCategoryWithProductsUseCase: GetCategoryWithProductsUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(CategoryDetailsUiState())
    val uiState: StateFlow<CategoryDetailsUiState> = _uiState.asStateFlow()
    
    fun loadCategoryDetails(categoryId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            getCategoryWithProductsUseCase(categoryId.toIntOrNull() ?: 0).collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        _uiState.value = _uiState.value.copy(
                            categoryWithProducts = result.data,
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
        _uiState.value.categoryWithProducts?.category?.id?.let { categoryId ->
            loadCategoryDetails(categoryId.toString())
        }
    }
}