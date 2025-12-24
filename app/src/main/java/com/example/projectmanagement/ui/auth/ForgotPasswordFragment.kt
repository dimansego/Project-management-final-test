package com.example.projectmanagement.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.projectmanagement.R
import com.example.projectmanagement.databinding.FragmentForgotPasswordBinding
import com.example.projectmanagement.ui.viewmodel.ForgotPasswordViewModel
import com.google.android.material.snackbar.Snackbar

class ForgotPasswordFragment : Fragment() {
    private lateinit var binding: FragmentForgotPasswordBinding
    private val viewModel: ForgotPasswordViewModel by viewModels()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_forgot_password, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        binding.sendResetLinkButton.setOnClickListener {
            viewModel.sendResetLink()
        }
        
        viewModel.resetLinkSent.observe(viewLifecycleOwner, Observer { sent ->
            if (sent) {
                Snackbar.make(binding.root, "Reset link sent to your email", Snackbar.LENGTH_SHORT).show()
            }
        })
        
        viewModel.email.observe(viewLifecycleOwner) {
            viewModel.clearErrors()
        }
    }
}

