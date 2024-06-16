package com.bangkit.anemai.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.anemai.data.repository.UserRepository
import com.bangkit.anemai.ui.login.LoginViewModel
import com.bangkit.anemai.ui.main.MainViewModel
import com.bangkit.anemai.ui.register.RegisterViewModel
import com.bangkit.anemai.utils.UserInjection

class UserViewModelFactory(private val userRepository: UserRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(userRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): UserViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserViewModelFactory(UserInjection.provideRepository(context)).also { INSTANCE = it }
            }
        }
    }
}