package com.example.localshop.feature.wishlist.data.repository

import com.example.localshop.core.result.ResultState
import com.example.localshop.feature.wishlist.data.mapper.WishlistMapper
import com.example.localshop.feature.wishlist.data.remote.WishlistApi
import com.example.localshop.feature.wishlist.data.remote.dto.WishlistRequestDto
import com.example.localshop.feature.wishlist.domain.model.Wishlist
import com.example.localshop.feature.wishlist.domain.model.WishlistItem
import com.example.localshop.feature.wishlist.domain.repository.WishlistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WishlistRepositoryImpl @Inject constructor(
    private val wishlistApi: WishlistApi
) : WishlistRepository {

    override fun getWishlist(): Flow<ResultState<Wishlist>> = flow {
        emit(ResultState.Loading)
        try {
            val response = wishlistApi.getWishlist()
            if (response.success && response.data != null) {
                val wishlist = WishlistMapper.mapToDomain(response.data)
                emit(ResultState.Success(wishlist))
            } else {
                emit(ResultState.Error(response.message ?: "Failed to load wishlist"))
            }
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "An error occurred"))
        }
    }

    override fun addToWishlist(productId: Int): Flow<ResultState<WishlistItem>> = flow {
        emit(ResultState.Loading)
        try {
            println("Adding to wishlist: productId=$productId")
            val response = wishlistApi.addToWishlist(WishlistRequestDto(productId))
            println("Add to wishlist response: success=${response.success}, message=${response.message}")
            if (response.success && response.data != null) {
                val wishlistItem = WishlistMapper.mapItemToDomain(response.data)
                emit(ResultState.Success(wishlistItem))
            } else {
                emit(ResultState.Error(response.message ?: "Failed to add to wishlist"))
            }
        } catch (e: Exception) {
            println("Exception adding to wishlist: ${e.message}")
            e.printStackTrace()
            emit(ResultState.Error(e.message ?: "An error occurred"))
        }
    }

    override fun removeFromWishlist(wishlistItemId: Int): Flow<ResultState<Unit>> = flow {
        emit(ResultState.Loading)
        try {
            val response = wishlistApi.removeFromWishlist(wishlistItemId)
            if (response.success) {
                emit(ResultState.Success(Unit))
            } else {
                emit(ResultState.Error(response.message ?: "Failed to remove from wishlist"))
            }
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "An error occurred"))
        }
    }

    override fun checkInWishlist(productId: Int): Flow<ResultState<Boolean>> = flow {
        emit(ResultState.Loading)
        try {
            val response = wishlistApi.checkInWishlist(WishlistRequestDto(productId))
            if (response.success) {
                val isInWishlist = response.data?.get("in_wishlist") ?: false
                emit(ResultState.Success(isInWishlist))
            } else {
                emit(ResultState.Error(response.message ?: "Failed to check wishlist"))
            }
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "An error occurred"))
        }
    }
}
