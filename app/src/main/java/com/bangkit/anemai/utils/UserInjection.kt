package com.bangkit.anemai.utils

import android.content.Context
import com.bangkit.anemai.data.pref.UserPreference
import com.bangkit.anemai.data.pref.dataStore
import com.bangkit.anemai.data.repository.UserRepository
import kotlinx.coroutines.runBlocking

object UserInjection {
    fun provideRepository(context: Context): UserRepository {
        val userPreference = UserPreference.getInstance(context.dataStore)
        return runBlocking {
            UserRepository.getInstance(userPreference)
        }
    }
}