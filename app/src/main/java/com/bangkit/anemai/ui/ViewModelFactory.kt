package com.bangkit.anemai.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.anemai.data.repository.ArticleRepository
import com.bangkit.anemai.data.repository.DetectionRepository
import com.bangkit.anemai.data.repository.UserRepository
import com.bangkit.anemai.ui.login.LoginViewModel
import com.bangkit.anemai.ui.main.MainViewModel
import com.bangkit.anemai.ui.register.RegisterViewModel
import com.bangkit.anemai.utils.Injection

class ViewModelFactory(private val detectionRepository: DetectionRepository, private val userRepository: UserRepository, private val articleRepository: ArticleRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> MainViewModel(detectionRepository, userRepository, articleRepository) as T
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(userRepository) as T
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> RegisterViewModel(userRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context, application: Application): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideDetectionRepository(application), Injection.provideRepository(context), Injection.provideArticleRepository(context, application))
            }.also { instance = it }
    }
}