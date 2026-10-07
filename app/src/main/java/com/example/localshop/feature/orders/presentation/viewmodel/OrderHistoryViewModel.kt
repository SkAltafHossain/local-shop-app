package com.example.localshop.feature.orders.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.localshop.core.result.ResultState
import com.example.localshop.feature.orders.domain.model.Order
import com.example.localshop.feature.orders.domain.usecase.ConfirmDeliveredUseCase
import com.example.localshop.feature.orders.domain.usecase.DownloadBillUseCase
import com.example.localshop.feature.orders.domain.usecase.GetOrdersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import javax.inject.Inject

@HiltViewModel
class OrderHistoryViewModel @Inject constructor(
    private val getOrdersUseCase: GetOrdersUseCase,
    private val confirmDeliveredUseCase: ConfirmDeliveredUseCase,
    private val downloadBillUseCase: DownloadBillUseCase
) : ViewModel() {

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _isConfirmingDelivery = MutableStateFlow(false)
    val isConfirmingDelivery: StateFlow<Boolean> = _isConfirmingDelivery.asStateFlow()

    private val _isDownloadingBill = MutableStateFlow(false)
    val isDownloadingBill: StateFlow<Boolean> = _isDownloadingBill.asStateFlow()

    private val _billData = MutableStateFlow<Pair<okhttp3.ResponseBody, Int>?>(null)
    val billData: StateFlow<Pair<okhttp3.ResponseBody, Int>?> = _billData.asStateFlow()

    private val _billFilePath = MutableStateFlow<String?>(null)
    val billFilePath: StateFlow<String?> = _billFilePath.asStateFlow()

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

    fun downloadBill(orderId: Int) {
        Log.d("OrderHistoryViewModel", "downloadBill called for order ID: $orderId")
        viewModelScope.launch {
            _isDownloadingBill.value = true
            _error.value = null

            downloadBillUseCase(orderId).collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        Log.d("OrderHistoryViewModel", "DownloadBillUseCase success for order ID: $orderId")
                        _isDownloadingBill.value = false
                        _billData.value = Pair(result.data, orderId)
                    }
                    is ResultState.Error -> {
                        Log.e("OrderHistoryViewModel", "DownloadBillUseCase error: ${result.message}")
                        _isDownloadingBill.value = false
                        _error.value = result.message
                    }
                    ResultState.Loading -> {
                        Log.d("OrderHistoryViewModel", "DownloadBillUseCase loading")
                        // Keep loading state
                    }
                }
            }
        }
    }

    fun setBillFilePath(path: String) {
        Log.d("OrderHistoryViewModel", "setBillFilePath called with path: $path")
        _billFilePath.value = path
    }

    fun clearError() {
        _error.value = null
    }

    fun clearBillData() {
        Log.d("OrderHistoryViewModel", "clearBillData called")
        _billData.value = null
        _billFilePath.value = null
    }

    fun clearBillDataOnly() {
        Log.d("OrderHistoryViewModel", "clearBillDataOnly called")
        _billData.value = null
    }
}
