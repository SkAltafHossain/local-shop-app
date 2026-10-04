package com.example.localshop.feature.checkout.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.localshop.core.result.ResultState
import com.example.localshop.feature.cart.domain.model.Cart
import com.example.localshop.feature.cart.domain.model.CartItem
import com.example.localshop.feature.cart.data.mapper.BuyNowMapper
import com.example.localshop.feature.cart.domain.usecase.BuyNowUseCase
import com.example.localshop.feature.cart.domain.usecase.GetCartUseCase
import com.example.localshop.feature.address.domain.usecase.GetAddressesUseCase
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
    private val buyNowUseCase: BuyNowUseCase,
    private val getAddressesUseCase: GetAddressesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState: StateFlow<CheckoutUiState> = _uiState.asStateFlow()

    private var currentProductId: Int = 0

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
                        // Load addresses after cart is loaded
                        loadAddresses()
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
        currentProductId = productId
        android.util.Log.d("CheckoutViewModel", "loadBuyNowProduct called with productId: $productId")
        viewModelScope.launch {
            // Clear previous cart data and set Buy Now mode
            _uiState.value = CheckoutUiState(
                isLoading = true,
                isBuyNowMode = true,
                selectedPaymentMethod = _uiState.value.selectedPaymentMethod
            )

            buyNowUseCase(productId, 1).collect { result ->
                android.util.Log.d("CheckoutViewModel", "BuyNow result: $result")
                when (result) {
                    is ResultState.Success -> {
                        val buyNowData = result.data
                        android.util.Log.d("CheckoutViewModel", "BuyNowData received: ${buyNowData.product.name}")
                        // Convert BuyNowData to Cart for checkout
                        val buyNowCart = BuyNowMapper.toCart(buyNowData)
                        android.util.Log.d("CheckoutViewModel", "Cart created with ${buyNowCart.items.size} items")

                        _uiState.value = _uiState.value.copy(
                            cart = buyNowCart,
                            isLoading = false,
                            errorMessage = null
                        )
                        // Load addresses after product is loaded
                        loadAddresses()
                    }
                    is ResultState.Error -> {
                        android.util.Log.e("CheckoutViewModel", "BuyNow error: ${result.message}")
                        _uiState.value = _uiState.value.copy(
                            errorMessage = result.message,
                            isLoading = false
                        )
                    }
                    ResultState.Loading -> {
                        android.util.Log.d("CheckoutViewModel", "BuyNow loading")
                        // Keep loading state
                    }
                }
            }
        }
    }

    fun selectPaymentMethod(method: String) {
        _uiState.value = _uiState.value.copy(selectedPaymentMethod = method)
    }

    fun loadAddresses() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingAddresses = true)

            getAddressesUseCase().collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        val addresses = result.data
                        // Automatically select default address if available
                        val defaultAddress = addresses.find { it.isDefault }
                        _uiState.value = _uiState.value.copy(
                            addresses = addresses,
                            selectedAddressId = defaultAddress?.id,
                            isLoadingAddresses = false
                        )
                    }
                    is ResultState.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoadingAddresses = false
                        )
                    }
                    ResultState.Loading -> {
                        // Keep loading state
                    }
                }
            }
        }
    }

    fun selectAddress(addressId: Int) {
        _uiState.value = _uiState.value.copy(selectedAddressId = addressId)
    }

    fun showConfirmationModal() {
        _uiState.value = _uiState.value.copy(showConfirmationModal = true)
    }

    fun hideConfirmationModal() {
        _uiState.value = _uiState.value.copy(showConfirmationModal = false)
    }

    fun processCheckout() {
        android.util.Log.d("CheckoutViewModel", "processCheckout called")
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isProcessingCheckout = true)

            val cart = _uiState.value.cart
            android.util.Log.d("CheckoutViewModel", "Cart: ${cart?.items?.size} items")
            if (cart == null || cart.items.isEmpty()) {
                android.util.Log.e("CheckoutViewModel", "Cart is empty")
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Cart is empty",
                    isProcessingCheckout = false
                )
                return@launch
            }

            if (_uiState.value.selectedAddressId == null) {
                android.util.Log.e("CheckoutViewModel", "No address selected")
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Please select a delivery address",
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
                addressId = _uiState.value.selectedAddressId,
                paymentMethod = _uiState.value.selectedPaymentMethod,
                isBuyNow = _uiState.value.isBuyNowMode,
                items = items
            )

            android.util.Log.d("CheckoutViewModel", "Request: addressId=${request.addressId}, paymentMethod=${request.paymentMethod}, isBuyNow=${request.isBuyNow}, items=${request.items.size}")

            processCheckoutUseCase(request).collect { result ->
                android.util.Log.d("CheckoutViewModel", "Checkout result: $result")
                when (result) {
                    is ResultState.Success -> {
                        android.util.Log.d("CheckoutViewModel", "Checkout successful, orderId=${result.data.orderId}")
                        _uiState.value = _uiState.value.copy(
                            isProcessingCheckout = false,
                            checkoutResponse = result.data,
                            errorMessage = null,
                            showConfirmationModal = false
                        )
                    }
                    is ResultState.Error -> {
                        android.util.Log.e("CheckoutViewModel", "Checkout error: ${result.message}")
                        _uiState.value = _uiState.value.copy(
                            errorMessage = result.message,
                            isProcessingCheckout = false,
                            showConfirmationModal = false
                        )
                    }
                    ResultState.Loading -> {
                        android.util.Log.d("CheckoutViewModel", "Checkout loading")
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
    }
}
