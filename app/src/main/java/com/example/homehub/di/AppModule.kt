package com.example.homehub.di

import android.content.Context
import com.example.homehub.MainGateController
import com.example.homehub.repositories.MainGateRepository
import com.example.homehub.utils.MQTTManager
import com.example.homehub.utils.Preferences
import com.example.homehub.utils.StartupConfigurator
import com.example.homehub.utils.Toasts
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideStartupConfigurator(
        preferences: Preferences
    ): StartupConfigurator = StartupConfigurator(preferences)

    @Provides
    @Singleton
    fun providePreferences(@ApplicationContext context: Context): Preferences = Preferences(context)

    @Provides
    @Singleton
    fun provideMQTTManager(preferences: Preferences): MQTTManager = MQTTManager(preferences)

    @Provides
    @Singleton
    fun provideMainGateRepository(
        mqttManager: MQTTManager,
        preferences: Preferences
    ): MainGateRepository = MainGateRepository(mqttManager, preferences)

    @Provides
    @Singleton
    fun provideMainGateController(
        repository: MainGateRepository
    ): MainGateController = MainGateController(repository)

    @Provides
    @Singleton
    fun provideToasts(@ApplicationContext context: Context): Toasts = Toasts(context)
}
