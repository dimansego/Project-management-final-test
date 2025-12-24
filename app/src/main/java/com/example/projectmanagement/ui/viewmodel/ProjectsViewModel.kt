package com.example.projectmanagement.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.projectmanagement.data.model.Project
import com.example.projectmanagement.data.repository.ProjectRepository
import com.example.projectmanagement.ui.common.UiState

class ProjectsViewModel(
    private val projectRepository: ProjectRepository
) : ViewModel() {
    
    val projectsState: LiveData<UiState<List<Project>>> = projectRepository.getAllProjects().map { projects ->
        UiState.Success(projects)
    }
}

