package com.example.projectmanagement.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectmanagement.data.model.User
import com.example.projectmanagement.data.repository.SessionManager

class ProfileViewModel : ViewModel() {
    
    private val _currentUser = MutableLiveData<User?>()
    val currentUser: LiveData<User?> = _currentUser
    
    private val _logoutSuccess = MutableLiveData<Boolean>()
    val logoutSuccess: LiveData<Boolean> = _logoutSuccess
    
    init {
        loadCurrentUser()
    }
    
    fun logout() {
        SessionManager.clearSession()
        _logoutSuccess.value = true
    }
    
    private fun loadCurrentUser() {
        _currentUser.value = SessionManager.getCurrentUser()
    }
}
