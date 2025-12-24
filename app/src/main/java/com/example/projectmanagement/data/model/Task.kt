package com.example.projectmanagement.data.model

data class Task(
    val id: Int,
    val projectId: Int,
    val title: String,
    val description: String,
    val status: TaskStatus,
    val priority: TaskPriority,
    val deadline: String,
    val assigneeName: String
)

