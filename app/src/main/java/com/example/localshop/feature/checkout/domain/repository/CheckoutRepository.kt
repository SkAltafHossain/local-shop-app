package com.example.localshop.feature.checkout.domain.repository

import com.example.localshop.core.result.ResultState
import com.example.localshop.feature.checkout.domain.model.CheckoutRequest
import com.example.localshop.feature.checkout.domain.model.CheckoutResponse
import kotlinx.coroutines.flow.Flow

interface CheckoutRepository {
    fun processCheckout(request: CheckoutRequest): Flow<ResultState<CheckoutResponse>>
}
