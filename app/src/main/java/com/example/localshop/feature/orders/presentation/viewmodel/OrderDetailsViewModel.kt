package com.example.localshop.feature.orders.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.localshop.core.result.ResultState
import com.example.localshop.feature.orders.domain.model.Order
import com.example.localshop.feature.orders.domain.usecase.GetOrderDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderDetailsViewModel @Inject constructor(
    private val getOrderDetailsUseCase: GetOrderDetailsUseCase
) : ViewModel() {

    private val _order = MutableStateFlow<Order?>(null)
    val order: StateFlow<Order?> = _order.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadOrderDetails(orderId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            getOrderDetailsUseCase(orderId).collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        _isLoading.value = false
                        _order.value = result.data
                    }
                    is ResultState.Error -> {
                        _isLoading.value = false
                        _error.value = result.message
                    }
                    ResultState.Loading -> {
                        // Keep loading state
                    }
                }
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}
