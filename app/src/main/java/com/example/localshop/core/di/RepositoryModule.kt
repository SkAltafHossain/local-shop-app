package com.example.localshop.core.di

import com.example.localshop.feature.auth.data.repository.AuthRepositoryImpl
import com.example.localshop.feature.auth.domain.repository.AuthRepository
import com.example.localshop.feature.category.data.repository.CategoryRepositoryImpl
import com.example.localshop.feature.category.domain.repository.CategoryRepository
import com.example.localshop.feature.home.data.repository.HomeRepositoryImpl
import com.example.localshop.feature.home.data.repository.ShopRepositoryImpl
import com.example.localshop.feature.home.domain.repository.HomeRepository
import com.example.localshop.feature.home.domain.repository.ShopRepository
import com.example.localshop.feature.product.data.repository.ProductRepositoryImpl
import com.example.localshop.feature.product.domain.repository.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Binds
    @Singleton
    abstract fun bindShopRepository(
        shopRepositoryImpl: ShopRepositoryImpl
    ): ShopRepository
    
    @Binds
    @Singleton
    abstract fun bindHomeRepository(
        homeRepositoryImpl: HomeRepositoryImpl
    ): HomeRepository
    
    @Binds
    @Singleton
    abstract fun bindProductRepository(
        productRepositoryImpl: ProductRepositoryImpl
    ): ProductRepository
    
    @Binds
    @Singleton
    abstract fun bindCategoryRepository(
        categoryRepositoryImpl: CategoryRepositoryImpl
    ): CategoryRepository
    
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository
}