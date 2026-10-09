package com.example.localshop.feature.wishlist.domain.usecase

import com.example.localshop.core.result.ResultState
import com.example.localshop.feature.wishlist.domain.repository.WishlistRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CheckInWishlistUseCase @Inject constructor(
    private val wishlistRepository: WishlistRepository
) {
    operator fun invoke(productId: Int): Flow<ResultState<Boolean>> {
        return wishlistRepository.checkInWishlist(productId)
    }
}
