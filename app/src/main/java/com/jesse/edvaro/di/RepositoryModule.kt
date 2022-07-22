package com.jesse.edvaro.di

import com.jesse.edvaro.data.repository.RideRepo
import com.jesse.edvaro.data.repository.RideRepository
import com.jesse.edvaro.data.repository.UserRepo
import com.jesse.edvaro.data.repository.UserRepository
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
    abstract fun provideUserRepository(repo: UserRepository): UserRepo

    @Binds
    @Singleton
    abstract fun provideRideRepository(repo: RideRepository): RideRepo
}