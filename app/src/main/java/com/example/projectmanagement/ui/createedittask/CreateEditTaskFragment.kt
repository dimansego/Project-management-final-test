package com.example.projectmanagement.ui.createedittask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.projectmanagement.ProjectApplication
import com.example.projectmanagement.R
import com.example.projectmanagement.data.model.TaskPriority
import com.example.projectmanagement.data.model.TaskStatus
import com.example.projectmanagement.data.repository.ProjectRepository
import com.example.projectmanagement.databinding.FragmentCreateEditTaskBinding
import com.example.projectmanagement.ui.common.UiState
import com.example.projectmanagement.ui.viewmodel.CreateEditTaskViewModel

class CreateEditTaskFragment : Fragment() {
    private lateinit var binding: FragmentCreateEditTaskBinding
    private val viewModel: CreateEditTaskViewModel by viewModels {
        object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return CreateEditTaskViewModel(
                    ProjectRepository(
                        (activity?.application as ProjectApplication).database.projectDao(),
                        (activity?.application as ProjectApplication).database.taskDao()
                    )
                ) as T
            }
        }
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_edit_task, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val args = CreateEditTaskFragmentArgs.fromBundle(requireArguments())
        val taskId = args.taskId
        val projectId = args.projectId
        
        if (taskId != 0) {
            viewModel.initForEdit(taskId)
        } else {
            viewModel.initForCreate(projectId)
        }
        
        setupStatusDropdown()
        setupPriorityDropdown()
        
        binding.saveButton.setOnClickListener {
            viewModel.saveTask()
        }
        
        viewModel.saveState.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is UiState.Success -> {
                    findNavController().popBackStack()
                }
                is UiState.Error -> {
                    // Handle error
                }
                else -> {}
            }
        })
    }
    
    private fun setupStatusDropdown() {
        val statuses = TaskStatus.values().map { it.name }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, statuses)
        binding.statusAutoComplete.setAdapter(adapter)
        
        viewModel.status.observe(viewLifecycleOwner) { status ->
            if (status != null) {
                binding.statusAutoComplete.setText(status.name, false)
            }
        }
        
        binding.statusAutoComplete.setOnItemClickListener { _, _, position, _ ->
            viewModel.status.value = TaskStatus.values()[position]
        }
    }
    
    private fun setupPriorityDropdown() {
        val priorities = TaskPriority.values().map { it.name }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, priorities)
        binding.priorityAutoComplete.setAdapter(adapter)
        
        viewModel.priority.observe(viewLifecycleOwner) { priority ->
            if (priority != null) {
                binding.priorityAutoComplete.setText(priority.name, false)
            }
        }
        
        binding.priorityAutoComplete.setOnItemClickListener { _, _, position, _ ->
            viewModel.priority.value = TaskPriority.values()[position]
        }
    }
}

