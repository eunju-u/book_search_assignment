package com.example.database.di

import android.content.Context
import androidx.room.Room
import com.example.database.FavoriteDao
import com.example.database.FavoriteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// Room DB, DAO를 Hilt에 연결하는 모듈
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    // 즐겨찾기 DB
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): FavoriteDatabase =
        Room.databaseBuilder(context, FavoriteDatabase::class.java, "favorite_database").build()

    // 즐겨찾기 DAO
    @Provides
    @Singleton
    fun provideFavoriteDao(db: FavoriteDatabase): FavoriteDao = db.favoriteDao()
}