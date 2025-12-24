package com.example.projectmanagement.ui.meetings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmanagement.R
import com.example.projectmanagement.databinding.ItemMeetingBinding

class MeetingsAdapter : ListAdapter<Meeting, MeetingsAdapter.MeetingViewHolder>(MeetingDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeetingViewHolder {
        val binding = DataBindingUtil.inflate<ItemMeetingBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_meeting,
            parent,
            false
        )
        return MeetingViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: MeetingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    class MeetingViewHolder(
        private val binding: ItemMeetingBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(meeting: Meeting) {
            binding.meeting = meeting
            binding.executePendingBindings()
        }
    }
    
    class MeetingDiffCallback : DiffUtil.ItemCallback<Meeting>() {
        override fun areItemsTheSame(oldItem: Meeting, newItem: Meeting): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: Meeting, newItem: Meeting): Boolean {
            return oldItem == newItem
        }
    }
}

