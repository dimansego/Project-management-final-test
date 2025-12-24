package com.example.projectmanagement.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.projectmanagement.data.model.Project
import com.example.projectmanagement.data.model.Task
import com.example.projectmanagement.data.model.TaskStatus
import com.example.projectmanagement.data.repository.ProjectRepository
import com.example.projectmanagement.ui.common.UiState

class ProjectDetailViewModel(
    private val projectRepository: ProjectRepository
) : ViewModel() {
    
    private val _projectId = MutableLiveData<Int>()
    val project: LiveData<Project?> = Transformations.switchMap(_projectId) { id ->
        projectRepository.getProjectById(id)
    }
    
    private val _selectedStatus = MutableLiveData<TaskStatus?>()
    val selectedStatus: LiveData<TaskStatus?> = _selectedStatus
    
    val tasksState: LiveData<UiState<List<Task>>> = Transformations.switchMap(_projectId) { projectId ->
        Transformations.map(projectRepository.getTasksByProjectId(projectId)) { allTasks ->
            val filteredTasks = if (_selectedStatus.value != null) {
                allTasks.filter { it.status == _selectedStatus.value }
            } else {
                allTasks
            }
            UiState.Success(filteredTasks)
        }
    }
    
    fun loadProject(projectId: Int) {
        _projectId.value = projectId
    }
    
    fun filterByStatus(status: TaskStatus?) {
        _selectedStatus.value = status
        // Trigger reload by updating projectId
        val currentId = _projectId.value
        if (currentId != null) {
            _projectId.value = currentId
        }
    }
}
