package com.example.data.di

import com.example.data.repository.BookRepositoryImpl
import com.example.data.repository.FavoriteRepositoryImpl
import com.example.domain.repository.BookRepository
import com.example.domain.repository.FavoriteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// Repository 인터페이스와 구현체를 Hilt에 연결하는 모듈
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindBookRepository(impl: BookRepositoryImpl): BookRepository

    @Binds
    @Singleton
    abstract fun bindFavoriteRepository(impl: FavoriteRepositoryImpl): FavoriteRepository
}