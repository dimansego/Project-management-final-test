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
import com.example.projectmanagement.databinding.FragmentLoginBinding
import com.example.projectmanagement.ui.common.UiState
import com.example.projectmanagement.ui.viewmodel.AuthViewModelFactory
import com.example.projectmanagement.ui.viewmodel.LoginViewModel

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by activityViewModels {
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        binding.loginButton.setOnClickListener {
            viewModel.login()
        }
        
        binding.registerLink.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        
        binding.forgotPasswordLink.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        }
        
        viewModel.loginState.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is UiState.Success -> {
                    com.example.projectmanagement.data.repository.SessionManager.setCurrentUser(state.data)
                    findNavController().navigate(R.id.action_loginFragment_to_main_nav)
                }
                is UiState.Error -> {
                    binding.errorTextView.text = state.message
                    binding.errorTextView.visibility = View.VISIBLE
                }
                else -> {}
            }
        })
        
        viewModel.email.observe(viewLifecycleOwner) {
            viewModel.clearErrors()
        }
        
        viewModel.password.observe(viewLifecycleOwner) {
            viewModel.clearErrors()
        }
    }
}

