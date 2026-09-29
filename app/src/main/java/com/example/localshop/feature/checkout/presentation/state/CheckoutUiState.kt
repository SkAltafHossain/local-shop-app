package com.example.localshop.feature.checkout.presentation.state

import com.example.localshop.feature.cart.domain.model.Cart
import com.example.localshop.feature.checkout.domain.model.CheckoutResponse

data class CheckoutUiState(
    val isLoading: Boolean = false,
    val cart: Cart? = null,
    val errorMessage: String? = null,
    val isProcessingCheckout: Boolean = false,
    val checkoutResponse: CheckoutResponse? = null,
    val selectedPaymentMethod: String = "cod",
    val isBuyNowMode: Boolean = false
)
