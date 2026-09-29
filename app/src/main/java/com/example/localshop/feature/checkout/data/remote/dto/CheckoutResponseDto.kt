package com.example.localshop.feature.checkout.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CheckoutResponseDto(
    @SerialName("order_id")
    val orderId: Int,
    
    @SerialName("order_number")
    val orderNumber: String,
    
    @SerialName("total_amount")
    val totalAmount: Double,
    
    @SerialName("payment_status")
    val paymentStatus: String,
    
    @SerialName("message")
    val message: String
)
