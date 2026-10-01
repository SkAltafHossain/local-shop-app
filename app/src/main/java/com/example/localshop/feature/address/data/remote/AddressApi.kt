package com.example.localshop.feature.address.data.remote

import com.example.localshop.core.network.ApiResponse
import com.example.localshop.feature.address.data.remote.dto.AddressDto
import com.example.localshop.feature.address.data.remote.dto.AddressRequestDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AddressApi {
    @GET("user/addresses")
    suspend fun getAddresses(): ApiResponse<List<AddressDto>>

    @POST("user/addresses")
    suspend fun addAddress(@Body request: AddressRequestDto): ApiResponse<AddressDto>

    @PUT("user/addresses/{id}")
    suspend fun updateAddress(
        @Path("id") addressId: Int,
        @Body request: AddressRequestDto
    ): ApiResponse<AddressDto>

    @DELETE("user/addresses/{id}")
    suspend fun deleteAddress(@Path("id") addressId: Int): ApiResponse<Unit>

    @PUT("user/addresses/{id}/default")
    suspend fun setDefaultAddress(@Path("id") addressId: Int): ApiResponse<Unit>
}
