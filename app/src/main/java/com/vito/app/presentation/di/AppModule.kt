package com.vito.app.presentation.di

import android.content.Context
import androidx.room.Room
import com.vito.app.data.local.VitoDatabase
import com.vito.app.data.local.dao.*
import com.vito.app.data.mock.MockAuthRepository
import com.vito.app.data.mock.MockChatRepository
import com.vito.app.data.mock.MockMartRepository
import com.vito.app.data.mock.MockRideRepository
import com.vito.app.data.repository.AuthRepository
import com.vito.app.data.repository.ChatRepository
import com.vito.app.data.repository.MartRepository
import com.vito.app.data.repository.RideRepository
import com.vito.app.data.repository.UserRepository
import com.vito.app.data.mock.MockUserRepository
import dagger.Binds
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
    fun provideDatabase(@ApplicationContext context: Context): VitoDatabase {
        return Room.databaseBuilder(
            context,
            VitoDatabase::class.java,
            "vito_database"
        ).build()
    }
    
    @Provides
    fun provideTripDao(database: VitoDatabase): TripDao = database.tripDao()
    
    @Provides
    fun providePackageDao(database: VitoDatabase): PackageDao = database.packageDao()
    
    @Provides
    fun provideMartOrderDao(database: VitoDatabase): MartOrderDao = database.martOrderDao()
    
    @Provides
    fun provideCartDao(database: VitoDatabase): CartDao = database.cartDao()
    
    @Provides
    fun provideUserDao(database: VitoDatabase): UserDao = database.userDao()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: MockAuthRepository): AuthRepository
    
    @Binds
    @Singleton
    abstract fun bindRideRepository(impl: MockRideRepository): RideRepository
    
    @Binds
    @Singleton
    abstract fun bindMartRepository(impl: MockMartRepository): MartRepository
    
    @Binds
    @Singleton
    abstract fun bindChatRepository(impl: MockChatRepository): ChatRepository
    
    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: MockUserRepository): UserRepository
}