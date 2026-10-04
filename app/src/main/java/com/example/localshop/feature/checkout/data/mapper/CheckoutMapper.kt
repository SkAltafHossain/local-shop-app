package com.example.localshop.feature.checkout.data.mapper

import com.example.localshop.feature.checkout.data.remote.dto.CheckoutItemDto
import com.example.localshop.feature.checkout.data.remote.dto.CheckoutRequestDto
import com.example.localshop.feature.checkout.data.remote.dto.CheckoutResponseDto
import com.example.localshop.feature.checkout.domain.model.CheckoutItem
import com.example.localshop.feature.checkout.domain.model.CheckoutRequest
import com.example.localshop.feature.checkout.domain.model.CheckoutResponse
import com.example.localshop.feature.checkout.domain.model.OrderItem

object CheckoutMapper {
    fun toDto(request: CheckoutRequest): CheckoutRequestDto {
        return CheckoutRequestDto(
            addressId = request.addressId,
            paymentMethod = request.paymentMethod,
            isBuyNow = request.isBuyNow,
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
            totalAmount = dto.totalAmount.toDoubleOrNull() ?: 0.0,
            paymentMethod = dto.paymentMethod,
            orderStatus = dto.orderStatus,
            customerName = dto.customerName,
            customerEmail = dto.customerEmail,
            customerPhone = dto.customerPhone,
            customerAddress = dto.customerAddress,
            addressId = dto.addressId,
            id = dto.id,
            items = dto.items.map { item ->
                OrderItem(
                    id = item.id,
                    orderId = item.orderId,
                    productId = item.productId,
                    quantity = item.quantity,
                    price = item.price.toDoubleOrNull() ?: 0.0,
                    total = item.total.toDoubleOrNull() ?: 0.0,
                    productName = item.product.name,
                    productPrice = item.product.price.toDoubleOrNull() ?: 0.0,
                    productDiscountPrice = item.product.discountPrice.toDoubleOrNull() ?: 0.0,
                    productImage = item.product.image
                )
            }
        )
    }
}
