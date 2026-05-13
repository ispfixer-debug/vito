package com.vito.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vito.app.data.local.dao.*
import com.vito.app.data.local.entity.*

@Database(
    entities = [
        TripEntity::class,
        PackageEntity::class,
        MartOrderEntity::class,
        MessageEntity::class,
        CartItemEntity::class,
        UserEntity::class,
        ProductEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class VitoDatabase : RoomDatabase() {
    abstract fun tripDao(): TripDao
    abstract fun packageDao(): PackageDao
    abstract fun martOrderDao(): MartOrderDao
    abstract fun cartDao(): CartDao
    abstract fun userDao(): UserDao
}