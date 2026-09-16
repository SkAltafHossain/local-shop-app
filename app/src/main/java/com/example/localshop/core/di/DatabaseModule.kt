package com.example.localshop.core.di

import android.content.Context
import androidx.room.Room
import com.example.localshop.core.database.LocalShopDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): LocalShopDatabase {
        return Room.databaseBuilder(
            context,
            LocalShopDatabase::class.java,
            "localshop_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}