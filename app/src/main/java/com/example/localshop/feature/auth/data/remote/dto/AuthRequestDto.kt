package com.example.localshop.feature.auth.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequestDto(
    @SerialName("name")
    val name: String,
    
    @SerialName("email")
    val email: String,
    
    @SerialName("password")
    val password: String,
    
    @SerialName("password_confirmation")
    val passwordConfirmation: String
)

@Serializable
data class LoginRequestDto(
    @SerialName("email")
    val email: String,
    
    @SerialName("password")
    val password: String
)

@Serializable
data class ForgotPasswordRequestDto(
    @SerialName("email")
    val email: String
)

@Serializable
data class ResetPasswordRequestDto(
    @SerialName("email")
    val email: String,
    
    @SerialName("token")
    val token: String,
    
    @SerialName("password")
    val password: String,
    
    @SerialName("password_confirmation")
    val passwordConfirmation: String
)