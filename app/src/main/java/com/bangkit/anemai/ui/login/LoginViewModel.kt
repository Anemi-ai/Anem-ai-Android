package com.bangkit.anemai.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.anemai.data.model.LoginResult
import com.bangkit.anemai.data.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel (private val userRepository: UserRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<Result<LoginResult>>()
    val loginResult: LiveData<Result<LoginResult>> = _loginResult

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val result = userRepository.login(email, password)
                _loginResult.value = Result.success(result)
            } catch (e: Exception) {
                _loginResult.value = Result.failure(e)
            }
        }
    }
}