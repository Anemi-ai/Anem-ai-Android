package com.bangkit.anemai.utils

import android.content.Context
import com.bangkit.anemai.data.pref.UserPreference
import com.bangkit.anemai.data.pref.dataStore
import com.bangkit.anemai.data.repository.DetectionRepository
import com.bangkit.anemai.data.repository.UserRepository
import com.bangkit.anemai.data.sevice.ApiConfig
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideDetectionRepository(): DetectionRepository {
        val apiService = ApiConfig.getMLApiService()
        return DetectionRepository.getInstance(apiService)
    }

    fun provideRepository(context: Context): UserRepository {
        val userPreference = UserPreference.getInstance(context.dataStore)
        return runBlocking {
            UserRepository.getInstance(userPreference)
        }
    }
}