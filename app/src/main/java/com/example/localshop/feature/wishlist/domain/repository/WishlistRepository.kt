package com.example.localshop.feature.wishlist.domain.repository

import com.example.localshop.core.result.ResultState
import com.example.localshop.feature.wishlist.domain.model.Wishlist
import com.example.localshop.feature.wishlist.domain.model.WishlistItem
import kotlinx.coroutines.flow.Flow

interface WishlistRepository {
    fun getWishlist(): Flow<ResultState<Wishlist>>
    fun addToWishlist(productId: Int): Flow<ResultState<WishlistItem>>
    fun removeFromWishlist(wishlistItemId: Int): Flow<ResultState<Unit>>
    fun checkInWishlist(productId: Int): Flow<ResultState<Boolean>>
}
