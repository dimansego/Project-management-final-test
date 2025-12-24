package com.example.projectmanagement.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.projectmanagement.data.model.Task
import com.example.projectmanagement.data.repository.ProjectRepository
import com.example.projectmanagement.ui.common.UiState

class TaskDetailViewModel(
    private val projectRepository: ProjectRepository
) : ViewModel() {
    
    private val _taskId = MutableLiveData<Int>()
    
    val taskState: LiveData<UiState<Task>> = Transformations.switchMap(_taskId) { id ->
        Transformations.map(projectRepository.getTaskById(id)) { task ->
            if (task != null) {
                UiState.Success(task)
            } else {
                UiState.Error("Task not found")
            }
        }
    }
    
    val task: LiveData<Task?> = Transformations.switchMap(_taskId) { id ->
        projectRepository.getTaskById(id)
    }
    
    fun loadTask(taskId: Int) {
        _taskId.value = taskId
    }
}
