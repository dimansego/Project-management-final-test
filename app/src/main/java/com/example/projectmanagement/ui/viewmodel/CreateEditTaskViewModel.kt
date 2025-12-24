package com.example.projectmanagement.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectmanagement.data.model.Task
import com.example.projectmanagement.data.model.TaskPriority
import com.example.projectmanagement.data.model.TaskStatus
import com.example.projectmanagement.data.repository.ProjectRepository
import com.example.projectmanagement.ui.common.UiState
import kotlinx.coroutines.launch

class CreateEditTaskViewModel(
    private val projectRepository: ProjectRepository
) : ViewModel() {
    
    val title = MutableLiveData<String>()
    val description = MutableLiveData<String>()
    val status = MutableLiveData<TaskStatus>()
    val priority = MutableLiveData<TaskPriority>()
    val deadline = MutableLiveData<String>()
    val assigneeName = MutableLiveData<String>()
    
    private val _taskId = MutableLiveData<Int?>()
    private val _projectId = MutableLiveData<Int>()
    
    private val _saveState = MutableLiveData<UiState<Task>>()
    val saveState: LiveData<UiState<Task>> = _saveState
    
    private val _titleError = MutableLiveData<String?>()
    val titleError: LiveData<String?> = _titleError
    
    // Load task data when editing
    val taskData: LiveData<Task?> = Transformations.switchMap(_taskId) { id ->
        if (id != null && id != 0) {
            projectRepository.getTaskById(id)
        } else {
            MutableLiveData(null)
        }
    }
    
    init {
        // Observe taskData to populate fields when editing
        taskData.observeForever { task ->
            if (task != null && _taskId.value != null) {
                _projectId.value = task.projectId
                title.value = task.title
                description.value = task.description
                status.value = task.status
                priority.value = task.priority
                deadline.value = task.deadline
                assigneeName.value = task.assigneeName
            }
        }
    }
    
    fun initForCreate(projectId: Int) {
        _projectId.value = projectId
        _taskId.value = null
        title.value = ""
        description.value = ""
        status.value = TaskStatus.TODO
        priority.value = TaskPriority.MEDIUM
        deadline.value = ""
        assigneeName.value = ""
    }
    
    fun initForEdit(taskId: Int) {
        _taskId.value = taskId
    }
    
    fun saveTask() {
        val titleValue = title.value?.trim() ?: ""
        
        if (titleValue.isEmpty()) {
            _titleError.value = "Title is required"
            return
        } else {
            _titleError.value = null
        }
        
        val task = Task(
            id = _taskId.value ?: 0,
            projectId = _projectId.value ?: 1,
            title = titleValue,
            description = description.value ?: "",
            status = status.value ?: TaskStatus.TODO,
            priority = priority.value ?: TaskPriority.MEDIUM,
            deadline = deadline.value ?: "",
            assigneeName = assigneeName.value ?: ""
        )
        
        _saveState.value = UiState.Loading
        
        viewModelScope.launch {
            try {
                if (_taskId.value != null && _taskId.value != 0) {
                    projectRepository.updateTask(task)
                } else {
                    projectRepository.insertTask(task)
                }
                _saveState.postValue(UiState.Success(task))
            } catch (e: Exception) {
                _saveState.postValue(UiState.Error("Error: ${e.message}"))
            }
        }
    }
    
    fun clearErrors() {
        _titleError.value = null
    }
}
