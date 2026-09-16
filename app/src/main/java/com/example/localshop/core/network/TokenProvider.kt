package com.example.localshop.core.network

import kotlinx.coroutines.flow.Flow

interface TokenProvider {
    fun getToken(): Flow<String?>
    suspend fun saveToken(token: String)
    suspend fun clearToken()
}