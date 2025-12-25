package com.example.projectmanagement.ui.projectdetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmanagement.R
import com.example.projectmanagement.data.model.Task
import com.example.projectmanagement.databinding.ItemTaskBinding

class TasksAdapter(
    private val onItemClick: (Task) -> Unit
) : ListAdapter<Task, TasksAdapter.TaskViewHolder>(TaskDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(
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
        private val binding: ItemTaskBinding,
        private val onItemClick: (Task) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(task: Task) {
            binding.titleTextView.text = task.title
            binding.descriptionTextView.text = task.description
            binding.statusChip.text = task.status.toString()
            binding.priorityTextView.text = task.priority.toString()
            binding.assigneeTextView.text = task.assigneeName
            binding.deadlineTextView.text = binding.root.context.getString(
                com.example.projectmanagement.R.string.deadline_label,
                task.deadline
            )
            binding.root.setOnClickListener {
                onItemClick(task)
            }
        }
    }
    
    class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }
    }
}

