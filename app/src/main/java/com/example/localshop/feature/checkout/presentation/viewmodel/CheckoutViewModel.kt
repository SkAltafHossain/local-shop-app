package com.example.localshop.feature.checkout.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.localshop.core.result.ResultState
import com.example.localshop.feature.cart.domain.model.Cart
import com.example.localshop.feature.cart.domain.model.CartItem
import com.example.localshop.feature.cart.data.mapper.BuyNowMapper
import com.example.localshop.feature.cart.domain.usecase.BuyNowUseCase
import com.example.localshop.feature.cart.domain.usecase.GetCartUseCase
import com.example.localshop.feature.checkout.domain.model.CheckoutItem
import com.example.localshop.feature.checkout.domain.model.CheckoutRequest
import com.example.localshop.feature.checkout.domain.usecase.ProcessCheckoutUseCase
import com.example.localshop.feature.checkout.presentation.state.CheckoutUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val getCartUseCase: GetCartUseCase,
    private val processCheckoutUseCase: ProcessCheckoutUseCase,
    private val buyNowUseCase: BuyNowUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState: StateFlow<CheckoutUiState> = _uiState.asStateFlow()

    fun loadCart() {
        viewModelScope.launch {
            // Clear previous Buy Now data and set Cart mode
            _uiState.value = CheckoutUiState(
                isLoading = true,
                isBuyNowMode = false,
                selectedPaymentMethod = _uiState.value.selectedPaymentMethod
            )

            getCartUseCase().collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        _uiState.value = _uiState.value.copy(
                            cart = result.data,
                            isLoading = false,
                            errorMessage = null
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

    fun loadBuyNowProduct(productId: Int) {
        viewModelScope.launch {
            // Clear previous cart data and set Buy Now mode
            _uiState.value = CheckoutUiState(
                isLoading = true,
                isBuyNowMode = true,
                selectedPaymentMethod = _uiState.value.selectedPaymentMethod
            )

            buyNowUseCase(productId, 1).collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        val buyNowData = result.data
                        // Convert BuyNowData to Cart for checkout
                        val buyNowCart = BuyNowMapper.toCart(buyNowData)

                        _uiState.value = _uiState.value.copy(
                            cart = buyNowCart,
                            isLoading = false,
                            errorMessage = null
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

    fun selectPaymentMethod(method: String) {
        _uiState.value = _uiState.value.copy(selectedPaymentMethod = method)
    }

    fun processCheckout() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isProcessingCheckout = true)

            val cart = _uiState.value.cart
            if (cart == null || cart.items.isEmpty()) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Cart is empty",
                    isProcessingCheckout = false
                )
                return@launch
            }

            val items = cart.items.map { cartItem ->
                CheckoutItem(
                    productId = cartItem.productId,
                    quantity = cartItem.quantity
                )
            }

            val request = CheckoutRequest(
                addressId = null, // TODO: Get from address selection
                paymentMethod = _uiState.value.selectedPaymentMethod,
                items = items
            )

            processCheckoutUseCase(request).collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isProcessingCheckout = false,
                            checkoutResponse = result.data,
                            errorMessage = null
                        )
                    }
                    is ResultState.Error -> {
                        _uiState.value = _uiState.value.copy(
                            errorMessage = result.message,
                            isProcessingCheckout = false
                        )
                    }
                    ResultState.Loading -> {
                        // Keep loading state
                    }
                }
            }
        }
    }

    fun clearErrorMessage() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    fun resetCheckout() {
        _uiState.value = CheckoutUiState()
        loadCart()
    }
}
