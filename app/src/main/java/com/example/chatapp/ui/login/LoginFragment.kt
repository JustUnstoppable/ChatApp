package com.example.chatapp.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.example.chatapp.R
import com.example.chatapp.databinding.FragmentLoginBinding
import com.example.chatapp.ui.BindingFragment
import com.example.chatapp.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

//need this to able to inject dependencies into android component using Dagger hilt.
@AndroidEntryPoint
class LoginFragment :BindingFragment<FragmentLoginBinding> (){
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        // in getter of that we need to return the inflate function of our fragment login binding
        get() = FragmentLoginBinding::inflate
    //Inject ViewModel here
    private val viewModel:LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnConfirm.setOnClickListener {
            setupConnnectingUiState()
            viewModel.connectUser(binding.etUsername.text.toString())
        }
        binding.etUsername.addTextChangedListener {
            binding.etUsername.error=null
        }
        subscribeToEvents()
    }
    //to catch these events
    private fun subscribeToEvents(){
        lifecycleScope.launchWhenStarted {
            viewModel.loginEvent.collect{   event->
                when(event){
                    is LoginViewModel.LogInEvent.ErrorInputTooShort->{
                        setupIdleUiState()
                        // constants si used to fill place holder in string
                        binding.etUsername.error=getString(R.string.error_username_too_short,Constants.MIN_USERNAME_LENGTH)
                    }
                    is LoginViewModel.LogInEvent.ErrorLogIn->{
                        setupIdleUiState()
                        Toast.makeText(
                            requireContext(),
                            event.error,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    is LoginViewModel.LogInEvent.Success->{
                        setupIdleUiState()
                        Toast.makeText(
                            requireContext(),
                            "Successful Login",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

            }
        }
    }
    //when we are connecting then progress bar appears and btnConfirm is disables.
    private fun setupConnnectingUiState(){
        binding.progressBar.isVisible=true
        binding.btnConfirm.isEnabled=false
    }
    private fun setupIdleUiState(){
        binding.progressBar.isVisible=false
        binding.btnConfirm.isEnabled=true
    }
}