package com.example.projectmanagement.ui.projects

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmanagement.R
import com.example.projectmanagement.data.model.Project
import com.example.projectmanagement.databinding.ItemProjectBinding

class ProjectsAdapter(
    private val onItemClick: (Project) -> Unit
) : ListAdapter<Project, ProjectsAdapter.ProjectViewHolder>(ProjectDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val binding = DataBindingUtil.inflate<ItemProjectBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_project,
            parent,
            false
        )
        return ProjectViewHolder(binding, onItemClick)
    }
    
    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    class ProjectViewHolder(
        private val binding: ItemProjectBinding,
        private val onItemClick: (Project) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(project: Project) {
            binding.project = project
            binding.executePendingBindings()
            binding.root.setOnClickListener {
                onItemClick(project)
            }
        }
    }
    
    class ProjectDiffCallback : DiffUtil.ItemCallback<Project>() {
        override fun areItemsTheSame(oldItem: Project, newItem: Project): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: Project, newItem: Project): Boolean {
            return oldItem == newItem
        }
    }
}

