package com.example.localshop.feature.auth.data.mapper

import com.example.localshop.feature.auth.data.remote.dto.AuthResponseDto
import com.example.localshop.feature.auth.data.remote.dto.UserDto
import com.example.localshop.feature.auth.domain.model.AuthResponse
import com.example.localshop.feature.auth.domain.model.User

object AuthMapper {
    fun mapToDomain(dto: UserDto): User {
        return User(
            id = dto.id,
            name = dto.name,
            email = dto.email,
            phone = dto.phone,
            avatar = dto.avatar,
            createdAt = dto.createdAt,
            updatedAt = dto.updatedAt
        )
    }
    
    fun mapToDomain(dto: AuthResponseDto): AuthResponse {
        return AuthResponse(
            token = dto.token,
            user = mapToDomain(dto.user)
        )
    }
}