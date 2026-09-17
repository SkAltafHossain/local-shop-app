package com.example.localshop.feature.home.domain.repository

import com.example.localshop.core.result.ResultState
import com.example.localshop.feature.home.domain.model.HomeData
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getHomeData(): Flow<ResultState<HomeData>>
}
