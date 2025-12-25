package com.example.projectmanagement.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.switchMap
import com.example.projectmanagement.data.model.Project
import com.example.projectmanagement.data.model.Task
import com.example.projectmanagement.data.model.TaskStatus
import com.example.projectmanagement.data.repository.ProjectRepository
import com.example.projectmanagement.ui.common.UiState

class ProjectDetailViewModel(
    private val projectRepository: ProjectRepository
) : ViewModel() {

    private val _projectId = MutableLiveData<Int?>()

    val project: LiveData<Project?> = _projectId.switchMap { id ->
        if (id == null) MutableLiveData(null)
        else projectRepository.getProjectById(id)
    }

    private val _selectedStatus = MutableLiveData<TaskStatus?>()
    val selectedStatus: LiveData<TaskStatus?> = _selectedStatus

    private val tasksLiveData: LiveData<List<Task>> = _projectId.switchMap { projectId ->
        if (projectId == null) MutableLiveData(emptyList())
        else projectRepository.getTasksByProjectId(projectId)
    }

    val tasksState: LiveData<UiState<List<Task>>> = MediatorLiveData<UiState<List<Task>>>().apply {
        value = UiState.Loading

        fun updateTasks() {
            val allTasks = tasksLiveData.value ?: emptyList()
            val status = _selectedStatus.value

            val filteredTasks = if (status != null) {
                allTasks.filter { it.status == status }
            } else {
                allTasks
            }

            value = UiState.Success(filteredTasks)
        }

        addSource(tasksLiveData) { updateTasks() }
        addSource(_selectedStatus) { updateTasks() }
    }

    fun loadProject(projectId: Int) {
        _projectId.value = projectId
        // optional: reset filter when opening a new project
        // _selectedStatus.value = null
    }

    fun filterByStatus(status: TaskStatus?) {
        _selectedStatus.value = status
    }
}

class ProjectDetailViewModelFactory(private val projectRepository: ProjectRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProjectDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProjectDetailViewModel(projectRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
