package com.example.projectmanagement.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmanagement.R
import com.example.projectmanagement.databinding.ItemTaskCardBinding

class TasksAdapter(
    private val onItemClick: (TaskUi) -> Unit
) : ListAdapter<TaskUi, TasksAdapter.TaskViewHolder>(TaskDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = DataBindingUtil.inflate<ItemTaskCardBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_task_card,
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
            binding.taskUi = taskUi
            binding.executePendingBindings()
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

