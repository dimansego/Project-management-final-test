package com.example.projectmanagement.ui.projects

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectmanagement.ProjectApplication
import com.example.projectmanagement.R
import com.example.projectmanagement.data.repository.ProjectRepository
import com.example.projectmanagement.databinding.FragmentProjectsBinding
import com.example.projectmanagement.ui.common.UiState
import com.example.projectmanagement.ui.viewmodel.ProjectsViewModel

class ProjectsFragment : Fragment() {
    private lateinit var binding: FragmentProjectsBinding
    private val viewModel: ProjectsViewModel by viewModels {
        object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return ProjectsViewModel(
                    ProjectRepository(
                        (activity?.application as ProjectApplication).database.projectDao(),
                        (activity?.application as ProjectApplication).database.taskDao()
                    )
                ) as T
            }
        }
    }
    private lateinit var adapter: ProjectsAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_projects, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        adapter = ProjectsAdapter { project ->
            val action = ProjectsFragmentDirections.actionProjectsFragmentToProjectDetailFragment(project.id)
            findNavController().navigate(action)
        }
        
        binding.projectsRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.projectsRecyclerView.adapter = adapter
        
        binding.addProjectFab.setOnClickListener {
            Toast.makeText(context, "Add project feature coming soon", Toast.LENGTH_SHORT).show()
        }
        
        viewModel.projectsState.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is UiState.Success -> {
                    adapter.submitList(state.data)
                }
                is UiState.Error -> {
                    binding.errorTextView.text = state.message
                    binding.errorTextView.visibility = View.VISIBLE
                }
                else -> {}
            }
        })
    }
}

