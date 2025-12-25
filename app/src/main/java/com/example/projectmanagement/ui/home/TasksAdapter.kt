package com.example.projectmanagement.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmanagement.R
import com.example.projectmanagement.databinding.ItemTaskCardBinding
import com.example.projectmanagement.ui.viewmodel.TaskUi

class TasksAdapter(
    private val onItemClick: (TaskUi) -> Unit
) : ListAdapter<TaskUi, TasksAdapter.TaskViewHolder>(TaskDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TaskViewHolder(binding, onItemClick)
    }
    
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    class TaskViewHolder(
        private val binding: ItemTaskCardBinding,
        private val onItemClick: (TaskUi) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(taskUi: TaskUi) {
            binding.taskTitle.text = taskUi.task.title
            binding.projectNameText.text = taskUi.projectTitle
            binding.dueDateText.text = binding.root.context.getString(
                com.example.projectmanagement.R.string.deadline_label,
                taskUi.task.deadline
            )
            binding.root.setOnClickListener {
                onItemClick(taskUi)
            }
        }
    }
    
    class TaskDiffCallback : DiffUtil.ItemCallback<TaskUi>() {
        override fun areItemsTheSame(oldItem: TaskUi, newItem: TaskUi): Boolean {
            return oldItem.task.id == newItem.task.id
        }
        
        override fun areContentsTheSame(oldItem: TaskUi, newItem: TaskUi): Boolean {
            return oldItem == newItem
        }
    }
}

