package com.example.projectmanagement.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.projectmanagement.ProjectApplication
import com.example.projectmanagement.R
import com.example.projectmanagement.data.repository.AuthRepository
import com.example.projectmanagement.databinding.FragmentRegisterBinding
import com.example.projectmanagement.ui.common.UiState
import com.example.projectmanagement.ui.viewmodel.AuthViewModelFactory
import com.example.projectmanagement.ui.viewmodel.RegisterViewModel

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: RegisterViewModel by activityViewModels {
        AuthViewModelFactory(
            AuthRepository(
                (activity?.application as ProjectApplication).database.userDao()
            )
        )
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        binding.registerButton.setOnClickListener {
            viewModel.register()
        }
        
        viewModel.registerState.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is UiState.Success -> {
                    Toast.makeText(context, "Registration successful!", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
                is UiState.Error -> {
                    Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        })
        
        viewModel.name.observe(viewLifecycleOwner) {
            viewModel.clearErrors()
        }
        
        viewModel.email.observe(viewLifecycleOwner) {
            viewModel.clearErrors()
        }
        
        viewModel.password.observe(viewLifecycleOwner) {
            viewModel.clearErrors()
        }
        
        viewModel.confirmPassword.observe(viewLifecycleOwner) {
            viewModel.clearErrors()
        }
    }
}

