package com.example.localshop.feature.auth.data.remote

import com.example.localshop.core.network.ApiResponse
import com.example.localshop.feature.auth.data.remote.dto.RegisterRequestDto
import com.example.localshop.feature.auth.data.remote.dto.LoginRequestDto
import com.example.localshop.feature.auth.data.remote.dto.AuthResponseDto
import com.example.localshop.feature.auth.data.remote.dto.ForgotPasswordRequestDto
import com.example.localshop.feature.auth.data.remote.dto.ResetPasswordRequestDto
import com.example.localshop.feature.auth.data.remote.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface AuthApi {
    @POST("register")
    suspend fun register(@Body request: RegisterRequestDto): ApiResponse<AuthResponseDto>
    
    @POST("login")
    suspend fun login(@Body request: LoginRequestDto): ApiResponse<AuthResponseDto>
    
    @POST("forgot-password")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequestDto): ApiResponse<Unit>
    
    @POST("reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequestDto): ApiResponse<Unit>
    
    @GET("user")
    suspend fun getCurrentUser(): ApiResponse<UserDto>
    
    @PUT("user")
    suspend fun updateUser(@Body request: Map<String, String>): ApiResponse<UserDto>
    
    @PUT("user/password")
    suspend fun updatePassword(@Body request: Map<String, String>): ApiResponse<Unit>
    
    @POST("logout")
    suspend fun logout(): ApiResponse<Unit>
}