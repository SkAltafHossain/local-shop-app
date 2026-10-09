package com.example.localshop.feature.wishlist.domain.usecase

import com.example.localshop.core.result.ResultState
import com.example.localshop.feature.wishlist.domain.repository.WishlistRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RemoveFromWishlistUseCase @Inject constructor(
    private val wishlistRepository: WishlistRepository
) {
    operator fun invoke(wishlistItemId: Int): Flow<ResultState<Unit>> {
        return wishlistRepository.removeFromWishlist(wishlistItemId)
    }
}
