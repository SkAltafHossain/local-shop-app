package com.example.localshop.feature.orders.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.localshop.core.result.ResultState
import com.example.localshop.feature.orders.domain.model.Order
import com.example.localshop.feature.orders.domain.usecase.DownloadBillUseCase
import com.example.localshop.feature.orders.domain.usecase.GetOrderDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import javax.inject.Inject

@HiltViewModel
class OrderDetailsViewModel @Inject constructor(
    private val getOrderDetailsUseCase: GetOrderDetailsUseCase,
    private val downloadBillUseCase: DownloadBillUseCase
) : ViewModel() {

    private val _order = MutableStateFlow<Order?>(null)
    val order: StateFlow<Order?> = _order.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _isDownloadingBill = MutableStateFlow(false)
    val isDownloadingBill: StateFlow<Boolean> = _isDownloadingBill.asStateFlow()

    private val _billData = MutableStateFlow<Pair<okhttp3.ResponseBody, Int>?>(null)
    val billData: StateFlow<Pair<okhttp3.ResponseBody, Int>?> = _billData.asStateFlow()

    private val _billFilePath = MutableStateFlow<String?>(null)
    val billFilePath: StateFlow<String?> = _billFilePath.asStateFlow()

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

    fun downloadBill(orderId: Int) {
        viewModelScope.launch {
            _isDownloadingBill.value = true
            _error.value = null

            downloadBillUseCase(orderId).collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        _isDownloadingBill.value = false
                        _billData.value = Pair(result.data, orderId)
                    }
                    is ResultState.Error -> {
                        _isDownloadingBill.value = false
                        _error.value = result.message
                    }
                    ResultState.Loading -> {
                        // Keep loading state
                    }
                }
            }
        }
    }

    fun setBillFilePath(path: String) {
        _billFilePath.value = path
    }

    fun clearError() {
        _error.value = null
    }

    fun clearBillData() {
        Log.d("OrderDetailsViewModel", "clearBillData called")
        _billData.value = null
        _billFilePath.value = null
    }

    fun clearBillDataOnly() {
        Log.d("OrderDetailsViewModel", "clearBillDataOnly called")
        _billData.value = null
    }
}
