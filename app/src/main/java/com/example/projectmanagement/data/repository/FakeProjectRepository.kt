package com.example.projectmanagement.data.repository

import com.example.projectmanagement.data.model.Project
import com.example.projectmanagement.data.model.Task
import com.example.projectmanagement.data.model.TaskPriority
import com.example.projectmanagement.data.model.TaskStatus

object FakeProjectRepository {
    private val projects = mutableListOf(
        Project(1, "Mobile App Development", "Building a cross-platform mobile application"),
        Project(2, "Website Redesign", "Modernizing the company website with new UI/UX"),
        Project(3, "Database Migration", "Migrating legacy database to cloud infrastructure")
    )
    
    private val tasks = mutableListOf(
        // Project 1 tasks
        Task(1, 1, "Design UI Mockups", "Create wireframes and mockups for main screens", TaskStatus.DONE, TaskPriority.HIGH, "2024-01-15", "Test User"),
        Task(2, 1, "Implement Login Screen", "Build authentication UI and logic", TaskStatus.DOING, TaskPriority.HIGH, "2024-02-01", "Admin"),
        Task(3, 1, "Setup API Integration", "Connect app to backend services", TaskStatus.TODO, TaskPriority.MEDIUM, "2024-02-10", "Test User"),
        Task(4, 1, "Write Unit Tests", "Add test coverage for core features", TaskStatus.TODO, TaskPriority.LOW, "2024-02-20", "Admin"),
        
        // Project 2 tasks
        Task(5, 2, "Research Design Trends", "Study current web design patterns", TaskStatus.DONE, TaskPriority.MEDIUM, "2024-01-10", "Test User"),
        Task(6, 2, "Create Color Palette", "Define brand colors and theme", TaskStatus.DOING, TaskPriority.MEDIUM, "2024-01-25", "Admin"),
        Task(7, 2, "Build Homepage", "Implement new homepage layout", TaskStatus.TODO, TaskPriority.HIGH, "2024-02-05", "Test User"),
        
        // Project 3 tasks
        Task(8, 3, "Backup Current Database", "Create full backup before migration", TaskStatus.DONE, TaskPriority.HIGH, "2024-01-05", "Admin"),
        Task(9, 3, "Setup Cloud Environment", "Configure cloud database instance", TaskStatus.DOING, TaskPriority.HIGH, "2024-01-20", "Test User"),
        Task(10, 3, "Migrate Data", "Transfer data to new database", TaskStatus.TODO, TaskPriority.HIGH, "2024-02-15", "Admin"),
        Task(11, 3, "Test Migration", "Verify data integrity after migration", TaskStatus.TODO, TaskPriority.MEDIUM, "2024-02-25", "Test User")
    )
    
    fun getAllProjects(): List<Project> = projects.toList()
    
    fun getProjectById(id: Int): Project? = projects.find { it.id == id }
    
    fun getTasksByProjectId(projectId: Int): List<Task> = tasks.filter { it.projectId == projectId }
    
    fun getTaskById(id: Int): Task? = tasks.find { it.id == id }
    
    fun getAllTasks(): List<Task> = tasks.toList()
    
    fun addTask(task: Task) {
        val newId = tasks.maxOfOrNull { it.id }?.plus(1) ?: 1
        tasks.add(task.copy(id = newId))
    }
    
    fun updateTask(task: Task) {
        val index = tasks.indexOfFirst { it.id == task.id }
        if (index != -1) {
            tasks[index] = task
        }
    }
    
    fun deleteTask(taskId: Int) {
        tasks.removeAll { it.id == taskId }
    }
    
    fun addProject(project: Project) {
        val newId = projects.maxOfOrNull { it.id }?.plus(1) ?: 1
        projects.add(project.copy(id = newId))
    }
}
