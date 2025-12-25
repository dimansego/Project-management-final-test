package com.example.projectmanagement.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmanagement.R
import com.example.projectmanagement.databinding.ItemAddProjectBinding
import com.example.projectmanagement.databinding.ItemProjectCardBinding
import com.example.projectmanagement.ui.viewmodel.ProjectUi

class ProjectsAdapter(
    private val onItemClick: (ProjectUi) -> Unit,
    private val onAddClick: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    
    companion object {
        private const val TYPE_ADD = 0
        private const val TYPE_PROJECT = 1
    }
    
    private var projects: List<ProjectUi> = emptyList()
    
    fun submitList(list: List<ProjectUi>) {
        projects = list
        notifyDataSetChanged()
    }
    
    override fun getItemViewType(position: Int): Int {
        return if (position == 0) TYPE_ADD else TYPE_PROJECT
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_ADD -> {
                val binding = ItemAddProjectBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                AddProjectViewHolder(binding, onAddClick)
            }
            else -> {
                val binding = ItemProjectCardBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ProjectViewHolder(binding, onItemClick)
            }
        }
    }
    
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is AddProjectViewHolder -> holder.bind()
            is ProjectViewHolder -> holder.bind(projects[position - 1])
        }
    }
    
    override fun getItemCount(): Int = projects.size + 1
    
    class ProjectViewHolder(
        private val binding: ItemProjectCardBinding,
        private val onItemClick: (ProjectUi) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(projectUi: ProjectUi) {
            binding.projectTitle.text = projectUi.project.title
            binding.unfinishedTasks.text = "There are ${projectUi.taskCount} unfinished tasks"
            binding.progressCircle.progress = projectUi.progress
            binding.progressText.text = "${projectUi.progress}%"
            binding.root.setOnClickListener {
                onItemClick(projectUi)
            }
        }
    }
    
    class AddProjectViewHolder(
        private val binding: ItemAddProjectBinding,
        private val onAddClick: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind() {
            binding.root.setOnClickListener {
                onAddClick()
            }
        }
    }
}

