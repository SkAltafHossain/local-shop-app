package com.example.localshop.feature.checkout.domain.usecase

import com.example.localshop.core.result.ResultState
import com.example.localshop.feature.checkout.domain.model.CheckoutRequest
import com.example.localshop.feature.checkout.domain.model.CheckoutResponse
import com.example.localshop.feature.checkout.domain.repository.CheckoutRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProcessCheckoutUseCase @Inject constructor(
    private val checkoutRepository: CheckoutRepository
) {
    operator fun invoke(request: CheckoutRequest): Flow<ResultState<CheckoutResponse>> {
        return checkoutRepository.processCheckout(request)
    }
}
