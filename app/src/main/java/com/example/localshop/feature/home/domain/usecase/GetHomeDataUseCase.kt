package com.example.localshop.feature.home.domain.usecase

import com.example.localshop.core.result.ResultState
import com.example.localshop.feature.home.domain.model.HomeData
import com.example.localshop.feature.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHomeDataUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    operator fun invoke(): Flow<ResultState<HomeData>> {
        return repository.getHomeData()
    }
}
