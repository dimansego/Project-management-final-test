package com.example.projectmanagement.data.model

data class Project(
    val id: Int,
    val title: String,
    val description: String,
    val dueDate: String = ""
)

