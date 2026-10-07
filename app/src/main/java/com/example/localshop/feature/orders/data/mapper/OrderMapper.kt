package com.example.localshop.feature.orders.data.mapper

import com.example.localshop.core.common.Constants
import com.example.localshop.feature.orders.data.remote.dto.OrderDto
import com.example.localshop.feature.orders.data.remote.dto.OrderItemDto
import com.example.localshop.feature.orders.domain.model.Order
import com.example.localshop.feature.orders.domain.model.OrderItem
import com.example.localshop.feature.orders.domain.model.OrderStatus

object OrderMapper {
    fun toDomain(dto: OrderDto): Order {
        return Order(
            id = dto.id,
            orderId = dto.orderId,
            totalAmount = dto.totalAmount.toDoubleOrNull() ?: 0.0,
            paymentMethod = dto.paymentMethod,
            orderStatus = OrderStatus.fromString(dto.orderStatus),
            customerName = dto.customerName,
            customerEmail = dto.customerEmail,
            customerPhone = dto.customerPhone,
            customerAddress = dto.customerAddress,
            addressId = dto.addressId,
            items = dto.items.map { toDomain(it) },
            createdAt = dto.createdAt,
            updatedAt = dto.updatedAt
        )
    }

    fun toDomain(dto: OrderItemDto): OrderItem {
        return OrderItem(
            id = dto.id,
            orderId = dto.orderId,
            productId = dto.productId,
            quantity = dto.quantity,
            price = dto.price.toDoubleOrNull() ?: 0.0,
            total = dto.total.toDoubleOrNull() ?: 0.0,
            productName = dto.product.name,
            productPrice = dto.product.price.toDoubleOrNull() ?: 0.0,
            productDiscountPrice = dto.product.discountPrice.toDoubleOrNull() ?: 0.0,
            productImage = "${Constants.IMAGE_BASE_URL}${dto.product.image}"
        )
    }

    fun toDomainList(dtos: List<OrderDto>): List<Order> {
        return dtos.map { toDomain(it) }
    }
}
