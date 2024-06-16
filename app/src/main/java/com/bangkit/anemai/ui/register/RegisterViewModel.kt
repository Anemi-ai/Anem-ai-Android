package com.bangkit.anemai.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.anemai.data.repository.UserRepository
import com.bangkit.anemai.data.sevice.ApiConfig
import kotlinx.coroutines.launch

class RegisterViewModel (private val userRepository: UserRepository) : ViewModel() {

    private val _registrationResult = MutableLiveData<Result<String>>()
    val registrationResult: LiveData<Result<String>> get() = _registrationResult

    fun registerUser(name: String, birthDate: String, gender: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = userRepository.register(name, birthDate, gender, email, password)
                if (response.status == false) {
                    _registrationResult.value = Result.success(response.message ?: "User Created")
                } else {
                    _registrationResult.value = Result.failure(Exception(response.message ?: "User Not Created"))
                }
            } catch (e: Exception) {
                _registrationResult.value = Result.failure(e)
            }
        }
    }
}