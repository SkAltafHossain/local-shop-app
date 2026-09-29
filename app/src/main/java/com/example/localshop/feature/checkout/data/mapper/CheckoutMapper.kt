package com.example.localshop.feature.checkout.data.mapper

import com.example.localshop.feature.checkout.data.remote.dto.CheckoutItemDto
import com.example.localshop.feature.checkout.data.remote.dto.CheckoutRequestDto
import com.example.localshop.feature.checkout.data.remote.dto.CheckoutResponseDto
import com.example.localshop.feature.checkout.domain.model.CheckoutItem
import com.example.localshop.feature.checkout.domain.model.CheckoutRequest
import com.example.localshop.feature.checkout.domain.model.CheckoutResponse

object CheckoutMapper {
    fun toDto(request: CheckoutRequest): CheckoutRequestDto {
        return CheckoutRequestDto(
            addressId = request.addressId,
            paymentMethod = request.paymentMethod,
            items = request.items.map { item ->
                CheckoutItemDto(
                    productId = item.productId,
                    quantity = item.quantity
                )
            }
        )
    }
    
    fun toDomain(dto: CheckoutResponseDto): CheckoutResponse {
        return CheckoutResponse(
            orderId = dto.orderId,
            orderNumber = dto.orderNumber,
            totalAmount = dto.totalAmount,
            paymentStatus = dto.paymentStatus,
            message = dto.message
        )
    }
}
