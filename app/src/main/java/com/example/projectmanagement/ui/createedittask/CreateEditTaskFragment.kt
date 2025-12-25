package com.example.projectmanagement.ui.createedittask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
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
import com.example.projectmanagement.ui.viewmodel.CreateEditTaskViewModelFactory

class CreateEditTaskFragment : Fragment() {
    private var _binding: FragmentCreateEditTaskBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CreateEditTaskViewModel by viewModels {
        CreateEditTaskViewModelFactory(
            ProjectRepository(
                (activity?.application as ProjectApplication).database.projectDao(),
                (activity?.application as ProjectApplication).database.taskDao()
            )
        )
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateEditTaskBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
        setupFormFields()
        
        binding.saveButton.setOnClickListener {
            viewModel.saveTask()
        }
        
        // Observe title error
        viewModel.titleError.observe(viewLifecycleOwner) { error ->
            binding.titleInputLayout.error = error
            binding.titleInputLayout.isErrorEnabled = error != null
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
    
    private fun setupFormFields() {
        // Set up two-way binding for form fields
        binding.titleEditText.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.setTitle(s?.toString() ?: "")
            }
            override fun afterTextChanged(s: android.text.Editable?) {}
        })
        
        binding.descriptionEditText.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.setDescription(s?.toString() ?: "")
            }
            override fun afterTextChanged(s: android.text.Editable?) {}
        })
        
        binding.deadlineEditText.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.setDeadline(s?.toString() ?: "")
            }
            override fun afterTextChanged(s: android.text.Editable?) {}
        })
        
        binding.assigneeEditText.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.setAssigneeName(s?.toString() ?: "")
            }
            override fun afterTextChanged(s: android.text.Editable?) {}
        })
        
        // Observe ViewModel fields to update EditTexts when ViewModel changes
        viewModel.title.observe(viewLifecycleOwner) { title ->
            if (binding.titleEditText.text?.toString() != title) {
                binding.titleEditText.setText(title)
            }
        }
        
        viewModel.description.observe(viewLifecycleOwner) { description ->
            if (binding.descriptionEditText.text?.toString() != description) {
                binding.descriptionEditText.setText(description)
            }
        }
        
        viewModel.deadline.observe(viewLifecycleOwner) { deadline ->
            if (binding.deadlineEditText.text?.toString() != deadline) {
                binding.deadlineEditText.setText(deadline)
            }
        }
        
        viewModel.assigneeName.observe(viewLifecycleOwner) { assignee ->
            if (binding.assigneeEditText.text?.toString() != assignee) {
                binding.assigneeEditText.setText(assignee)
            }
        }
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

