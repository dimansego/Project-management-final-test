package com.example.projectmanagement.ui.taskdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.projectmanagement.ProjectApplication
import com.example.projectmanagement.R
import com.example.projectmanagement.data.repository.ProjectRepository
import com.example.projectmanagement.databinding.FragmentTaskDetailBinding
import com.example.projectmanagement.ui.common.UiState
import com.example.projectmanagement.ui.viewmodel.TaskDetailViewModel

class TaskDetailFragment : Fragment() {
    private lateinit var binding: FragmentTaskDetailBinding
    private val viewModel: TaskDetailViewModel by viewModels {
        object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return TaskDetailViewModel(
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_task_detail, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val taskId = TaskDetailFragmentArgs.fromBundle(requireArguments()).taskId
        viewModel.loadTask(taskId)
        
        binding.editButton.setOnClickListener {
            val action = TaskDetailFragmentDirections.actionTaskDetailFragmentToCreateEditTaskFragment(taskId)
            findNavController().navigate(action)
        }
        
        viewModel.taskState.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is UiState.Error -> {
                    binding.errorTextView.text = state.message
                    binding.errorTextView.visibility = View.VISIBLE
                }
                else -> {}
            }
        })
    }
}

