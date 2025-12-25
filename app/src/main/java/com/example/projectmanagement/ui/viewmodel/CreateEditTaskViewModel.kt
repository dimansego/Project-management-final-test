package com.example.projectmanagement.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.switchMap
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
    val taskData: LiveData<Task?> = _taskId.switchMap { id ->
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
        
        if (!isEntryValid(titleValue)) {
            return
        }
        
        val newTask = getNewTaskEntry(titleValue)
        _saveState.value = UiState.Loading
        
        viewModelScope.launch {
            try {
                if (_taskId.value != null && _taskId.value != 0) {
                    projectRepository.updateTask(newTask)
                } else {
                    projectRepository.insertTask(newTask)
                }
                _saveState.postValue(UiState.Success(newTask))
            } catch (e: Exception) {
                _saveState.postValue(UiState.Error("Error: ${e.message}"))
            }
        }
    }
    
    fun clearErrors() {
        _titleError.value = null
    }
    
    fun setTitle(titleValue: String) {
        title.value = titleValue
    }
    
    fun setDescription(descriptionValue: String) {
        description.value = descriptionValue
    }
    
    fun setDeadline(deadlineValue: String) {
        deadline.value = deadlineValue
    }
    
    fun setAssigneeName(assigneeNameValue: String) {
        assigneeName.value = assigneeNameValue
    }
    
    private fun isEntryValid(titleValue: String): Boolean {
        return if (titleValue.isEmpty()) {
            _titleError.value = "Title is required"
            false
        } else {
            _titleError.value = null
            true
        }
    }
    
    private fun getNewTaskEntry(titleValue: String): Task {
        return Task(
            id = _taskId.value ?: 0,
            projectId = _projectId.value ?: 1,
            title = titleValue,
            description = description.value ?: "",
            status = status.value ?: TaskStatus.TODO,
            priority = priority.value ?: TaskPriority.MEDIUM,
            deadline = deadline.value ?: "",
            assigneeName = assigneeName.value ?: ""
        )
    }
}

class CreateEditTaskViewModelFactory(private val projectRepository: ProjectRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateEditTaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CreateEditTaskViewModel(projectRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
