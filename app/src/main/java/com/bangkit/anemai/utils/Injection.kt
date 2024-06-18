package com.bangkit.anemai.utils

import android.app.Application
import android.content.Context
import com.bangkit.anemai.data.pref.UserPreference
import com.bangkit.anemai.data.pref.dataStore
import com.bangkit.anemai.data.repository.ArticleRepository
import com.bangkit.anemai.data.repository.DetectionRepository
import com.bangkit.anemai.data.repository.UserRepository
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideDetectionRepository(context: Context, application: Application): DetectionRepository {
        val userPreference = UserPreference.getInstance(context.dataStore)
        return runBlocking {
            DetectionRepository.getInstance(application, userPreference)
        }
    }

    fun provideRepository(context: Context): UserRepository {
        val userPreference = UserPreference.getInstance(context.dataStore)
        return runBlocking {
            UserRepository.getInstance(userPreference)
        }
    }

    fun provideArticleRepository(context: Context, application: Application): ArticleRepository {
        val userPreference = UserPreference.getInstance(context.dataStore)
        return runBlocking {
            ArticleRepository.getInstance(userPreference, application)
        }
    }
}