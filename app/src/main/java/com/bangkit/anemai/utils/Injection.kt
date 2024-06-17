package com.bangkit.anemai.utils

import android.app.Application
import com.bangkit.anemai.data.repository.DetectionRepository
import com.bangkit.anemai.data.sevice.ApiConfig

object Injection {
    fun provideDetectionRepository(application: Application): DetectionRepository {

        val apiService = ApiConfig.getMLApiService()
        return DetectionRepository.getInstance(application, apiService)
    }
}