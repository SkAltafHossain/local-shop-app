package com.example.localshop.core.network

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenProvider: TokenProvider
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        
        // Skip auth for public endpoints
        val path = originalRequest.url.encodedPath
        if (isPublicEndpoint(path)) {
            return chain.proceed(originalRequest)
        }

        // Add auth token for protected endpoints
        val token = runBlocking { tokenProvider.getToken() }
        
        if (token != null) {
            val authenticatedRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
            return chain.proceed(authenticatedRequest)
        }

        return chain.proceed(originalRequest)
    }

    private fun isPublicEndpoint(path: String): Boolean {
        val publicEndpoints = listOf(
            "/register",
            "/login",
            "/forgot-password",
            "/reset-password",
            "/shop/settings",
            "/shop/info",
            "/products",
            "/categories"
        )
        
        return publicEndpoints.any { path.contains(it) }
    }
}