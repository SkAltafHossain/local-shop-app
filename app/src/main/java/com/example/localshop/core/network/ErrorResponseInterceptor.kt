package com.example.localshop.core.network

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import javax.inject.Inject

class ErrorResponseInterceptor @Inject constructor(
    private val json: Json
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        // If response is successful, return as-is
        if (response.isSuccessful) {
            return response
        }

        // Try to parse error response body for API error messages
        val errorBody = response.body
        val contentType = errorBody?.contentType()

        if (contentType?.subtype == "json" && errorBody != null) {
            try {
                val errorJson = errorBody.string()
                val jsonObject = json.parseToJsonElement(errorJson).jsonObject

                // The API response contains the actual error message
                // Override status code to 200 so Retrofit can parse the response body
                // The repository will check response.success and use response.message
                val newBody = errorJson.toResponseBody(contentType)
                return response.newBuilder()
                    .body(newBody)
                    .code(200)
                    .protocol(Protocol.HTTP_1_1)
                    .message("OK")
                    .build()
            } catch (e: Exception) {
                // If parsing fails, return original response
                return response
            }
        }

        return response
    }
}
