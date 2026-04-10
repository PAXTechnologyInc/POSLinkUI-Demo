package com.paxus.pay.poslinkui.demo.di

import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * Qualifier for IO-bound work (file, network); inject instead of hardcoding [Dispatchers.IO].
 */
@Qualifier
annotation class IoDispatcher

/** Qualifier for main-thread immediate callbacks. */
@Qualifier
annotation class MainImmediateDispatcher

@dagger.Module
@InstallIn(SingletonComponent::class)
object CoroutineDispatchersModule {
    @Provides
    @Singleton
    @IoDispatcher
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    @MainImmediateDispatcher
    fun provideMainImmediateDispatcher(): CoroutineDispatcher = Dispatchers.Main.immediate
}
