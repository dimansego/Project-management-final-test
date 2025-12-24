package com.example.projectmanagement.ui.meetings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectmanagement.R
import com.example.projectmanagement.databinding.FragmentMeetingsBinding
import com.example.projectmanagement.ui.viewmodel.MeetingsViewModel

class MeetingsFragment : Fragment() {
    private lateinit var binding: FragmentMeetingsBinding
    private val viewModel: MeetingsViewModel by viewModels()
    private lateinit var adapter: MeetingsAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_meetings, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        adapter = MeetingsAdapter()
        binding.meetingsRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.meetingsRecyclerView.adapter = adapter
        
        viewModel.meetings.observe(viewLifecycleOwner, Observer { meetings ->
            adapter.submitList(meetings)
        })
    }
}

