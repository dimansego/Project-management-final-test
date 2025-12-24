package com.example.projectmanagement.ui.projectdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectmanagement.ProjectApplication
import com.example.projectmanagement.R
import com.example.projectmanagement.data.model.TaskStatus
import com.example.projectmanagement.data.repository.ProjectRepository
import com.example.projectmanagement.databinding.FragmentProjectDetailBinding
import com.example.projectmanagement.ui.common.UiState
import com.example.projectmanagement.ui.viewmodel.ProjectDetailViewModel
import com.google.android.material.chip.Chip

class ProjectDetailFragment : Fragment() {
    private lateinit var binding: FragmentProjectDetailBinding
    private val viewModel: ProjectDetailViewModel by viewModels {
        object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return ProjectDetailViewModel(
                    ProjectRepository(
                        (activity?.application as ProjectApplication).database.projectDao(),
                        (activity?.application as ProjectApplication).database.taskDao()
                    )
                ) as T
            }
        }
    }
    private lateinit var adapter: TasksAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_project_detail, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val projectId = ProjectDetailFragmentArgs.fromBundle(requireArguments()).projectId
        viewModel.loadProject(projectId)
        
        adapter = TasksAdapter { task ->
            val action = ProjectDetailFragmentDirections.actionProjectDetailFragmentToTaskDetailFragment(task.id)
            findNavController().navigate(action)
        }
        
        binding.tasksRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.tasksRecyclerView.adapter = adapter
        
        binding.addTaskFab.setOnClickListener {
            val action = ProjectDetailFragmentDirections.actionProjectDetailFragmentToCreateEditTaskFragment(projectId)
            findNavController().navigate(action)
        }
        
        setupFilterChips()
        
        viewModel.tasksState.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is UiState.Success -> {
                    adapter.submitList(state.data)
                }
                is UiState.Error -> {
                    // Handle error
                }
                else -> {}
            }
        })
    }
    
    private fun setupFilterChips() {
        val chips = listOf(
            binding.allChip,
            binding.todoChip,
            binding.doingChip,
            binding.doneChip
        )
        
        chips.forEach { chip ->
            chip.setOnClickListener {
                updateChipSelection(chip, chips)
                val status = when (chip.id) {
                    R.id.allChip -> null
                    R.id.todoChip -> TaskStatus.TODO
                    R.id.doingChip -> TaskStatus.DOING
                    R.id.doneChip -> TaskStatus.DONE
                    else -> null
                }
                viewModel.filterByStatus(status)
            }
        }
        
        // Set default selection
        binding.allChip.isChecked = true
    }
    
    private fun updateChipSelection(selectedChip: Chip, allChips: List<Chip>) {
        allChips.forEach { it.isChecked = it == selectedChip }
    }
}

