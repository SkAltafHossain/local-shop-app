package com.example.localshop.feature.address.data.mapper

import com.example.localshop.feature.address.data.remote.dto.AddressDto
import com.example.localshop.feature.address.data.remote.dto.AddressRequestDto
import com.example.localshop.feature.address.domain.model.Address

object AddressMapper {
    fun toDomain(dto: AddressDto): Address {
        return Address(
            id = dto.id,
            userId = dto.userId,
            fullName = dto.name ?: "",
            phone = dto.phone ?: "",
            addressLine1 = dto.addressLine1,
            addressLine2 = dto.addressLine2,
            city = dto.city,
            state = dto.state,
            postalCode = dto.postalCode,
            country = dto.country,
            latitude = null,
            longitude = null,
            type = dto.type,
            isDefault = dto.isDefault,
            createdAt = dto.createdAt,
            updatedAt = dto.updatedAt
        )
    }

    fun toDto(address: Address): AddressDto {
        return AddressDto(
            id = address.id,
            userId = address.userId,
            name = address.fullName,
            phone = address.phone,
            type = address.type,
            addressLine1 = address.addressLine1,
            addressLine2 = address.addressLine2,
            city = address.city,
            state = address.state,
            postalCode = address.postalCode,
            country = address.country,
            isDefault = address.isDefault,
            createdAt = address.createdAt,
            updatedAt = address.updatedAt
        )
    }

    fun toRequestDto(address: Address): AddressRequestDto {
        return AddressRequestDto(
            name = address.fullName ?: "",
            phone = address.phone ?: "",
            type = address.type,
            addressLine1 = address.addressLine1,
            addressLine2 = address.addressLine2,
            city = address.city,
            state = address.state,
            postalCode = address.postalCode,
            country = address.country,
            latitude = address.latitude,
            longitude = address.longitude
        )
    }
}
