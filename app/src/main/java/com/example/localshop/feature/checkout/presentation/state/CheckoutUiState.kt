package com.example.localshop.feature.checkout.presentation.state

import com.example.localshop.feature.cart.domain.model.Cart
import com.example.localshop.feature.checkout.domain.model.CheckoutResponse
import com.example.localshop.feature.address.domain.model.Address

data class CheckoutUiState(
    val isLoading: Boolean = false,
    val cart: Cart? = null,
    val errorMessage: String? = null,
    val isProcessingCheckout: Boolean = false,
    val checkoutResponse: CheckoutResponse? = null,
    val selectedPaymentMethod: String = "cod",
    val isBuyNowMode: Boolean = false,
    val addresses: List<Address> = emptyList(),
    val selectedAddressId: Int? = null,
    val isLoadingAddresses: Boolean = false,
    val showConfirmationModal: Boolean = false,
    val shouldNavigateToOrders: Boolean = false
)
