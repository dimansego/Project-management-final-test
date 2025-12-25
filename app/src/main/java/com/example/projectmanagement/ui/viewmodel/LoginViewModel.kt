package com.example.projectmanagement.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.projectmanagement.data.model.User
import com.example.projectmanagement.data.repository.AuthRepository
import com.example.projectmanagement.ui.common.UiState
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    
    private val _loginState = MutableLiveData<UiState<User>>()
    val loginState: LiveData<UiState<User>> = _loginState
    
    private val _emailError = MutableLiveData<String?>()
    val emailError: LiveData<String?> = _emailError
    
    private val _passwordError = MutableLiveData<String?>()
    val passwordError: LiveData<String?> = _passwordError
    
    fun login() {
        val emailValue = email.value?.trim() ?: ""
        val passwordValue = password.value ?: ""
        
        if (!isEntryValid(emailValue, passwordValue)) {
            return
        }
        
        _loginState.value = UiState.Loading
        
        viewModelScope.launch {
            try {
                val user = authRepository.login(emailValue, passwordValue)
                if (user != null) {
                    _loginState.value = UiState.Success(user)
                } else {
                    _loginState.value = UiState.Error("Invalid email or password")
                }
            } catch (e: Exception) {
                _loginState.value = UiState.Error("Error: ${e.message}")
            }
        }
    }
    
    fun clearErrors() {
        _emailError.value = null
        _passwordError.value = null
    }
    
    fun setEmail(emailValue: String) {
        email.value = emailValue
    }
    
    fun setPassword(passwordValue: String) {
        password.value = passwordValue
    }
    
    private fun isEntryValid(emailValue: String, passwordValue: String): Boolean {
        val isValidEmail = when {
            emailValue.isEmpty() -> {
                _emailError.value = "Email is required"
                false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(emailValue).matches() -> {
                _emailError.value = "Invalid email format"
                false
            }
            else -> {
                _emailError.value = null
                true
            }
        }
        
        val isValidPassword = if (passwordValue.isEmpty()) {
            _passwordError.value = "Password is required"
            false
        } else {
            _passwordError.value = null
            true
        }
        
        return isValidEmail && isValidPassword
    }
}

class LoginViewModelFactory(private val authRepository: AuthRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
