package com.bangkit.anemai.utils

import android.content.Context
import com.bangkit.anemai.data.repository.DetectionRepository
import com.bangkit.anemai.data.sevice.ApiConfig

object Injection {
    fun provideDetectionRepository(): DetectionRepository {
        val apiService = ApiConfig.getMLApiService()
        return DetectionRepository.getInstance(apiService)
    }
}