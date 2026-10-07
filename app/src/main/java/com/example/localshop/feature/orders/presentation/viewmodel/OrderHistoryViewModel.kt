package com.example.localshop.feature.orders.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.localshop.core.result.ResultState
import com.example.localshop.feature.orders.domain.model.Order
import com.example.localshop.feature.orders.domain.usecase.ConfirmDeliveredUseCase
import com.example.localshop.feature.orders.domain.usecase.GetOrdersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderHistoryViewModel @Inject constructor(
    private val getOrdersUseCase: GetOrdersUseCase,
    private val confirmDeliveredUseCase: ConfirmDeliveredUseCase
) : ViewModel() {

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _isConfirmingDelivery = MutableStateFlow(false)
    val isConfirmingDelivery: StateFlow<Boolean> = _isConfirmingDelivery.asStateFlow()

    init {
        loadOrders()
    }

    fun loadOrders() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            getOrdersUseCase().collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        _isLoading.value = false
                        _orders.value = result.data
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

    fun confirmDelivered(orderId: Int) {
        viewModelScope.launch {
            _isConfirmingDelivery.value = true
            _error.value = null

            confirmDeliveredUseCase(orderId).collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        _isConfirmingDelivery.value = false
                        loadOrders()
                    }
                    is ResultState.Error -> {
                        _isConfirmingDelivery.value = false
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
