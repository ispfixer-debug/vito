package com.vito.core.data.di

import com.vito.core.data.repository.AuthRepository
import com.vito.core.data.repository.RideRepository
import com.vito.core.data.repository.PackageRepository
import com.vito.core.data.repository.MartRepository
import com.vito.core.data.repository.ChatRepository
import com.vito.core.data.repository.UserRepository
import com.vito.core.data.repository.QrRepository
import com.vito.core.data.repository.impl.AuthRepositoryImpl
import com.vito.core.data.repository.impl.RideRepositoryImpl
import com.vito.core.data.repository.impl.PackageRepositoryImpl
import com.vito.core.data.repository.impl.MartRepositoryImpl
import com.vito.core.data.repository.impl.ChatRepositoryImpl
import com.vito.core.data.repository.impl.UserRepositoryImpl
import com.vito.core.data.repository.impl.QrRepositoryImpl
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
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindRideRepository(impl: RideRepositoryImpl): RideRepository

    @Binds
    @Singleton
    abstract fun bindPackageRepository(impl: PackageRepositoryImpl): PackageRepository

    @Binds
    @Singleton
    abstract fun bindMartRepository(impl: MartRepositoryImpl): MartRepository

    @Binds
    @Singleton
    abstract fun bindChatRepository(impl: ChatRepositoryImpl): ChatRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindQrRepository(impl: QrRepositoryImpl): QrRepository
}