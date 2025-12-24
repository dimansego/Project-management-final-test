package com.example.projectmanagement.data.repository

import com.example.projectmanagement.data.model.User

class FakeAuthRepository {
    private val users = mutableListOf(
        User(1, "Test User", "test@gmail.com", "123456"),
        User(2, "Admin", "admin@gmail.com", "admin123")
    )
    
    private var currentUser: User? = null
    
    fun login(email: String, password: String): User? {
        val user = users.find { it.email == email && it.password == password }
        currentUser = user
        return user
    }
    
    fun register(name: String, email: String, password: String): User {
        val newId = users.maxOfOrNull { it.id }?.plus(1) ?: 1
        val user = User(newId, name, email, password)
        users.add(user)
        currentUser = user
        return user
    }
    
    fun getCurrentUser(): User? = currentUser
    
    fun logout() {
        currentUser = null
    }
    
    fun isEmailExists(email: String): Boolean {
        return users.any { it.email == email }
    }
}

