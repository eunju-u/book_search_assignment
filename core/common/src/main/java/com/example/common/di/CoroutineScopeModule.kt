package com.example.common.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

//앱에서 전체에서 사용할 어노테이션
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ApplicationScope

// 앱 단위 CoroutineScope를 Hilt에 연결하는 모듈
@Module
@InstallIn(SingletonComponent::class)
object CoroutineScopeModule {

    // 싱글톤 패턴 : 앱 동작하는 동안 사용하는 하나의 CoroutineScope
    // 앱 단위라 자식 코루틴 오류나도 다른 자식 코루틴이 취소되지 않도록 SupervisorJob 사용
    @Provides
    @Singleton
    @ApplicationScope
    fun provideApplicationScope(): CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
}