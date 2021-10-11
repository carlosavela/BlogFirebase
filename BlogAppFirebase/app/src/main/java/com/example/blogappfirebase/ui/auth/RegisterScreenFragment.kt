package com.example.blogappfirebase.ui.auth


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import android.widget.Toast
import com.example.blogappfirebase.core.hideIt
import com.example.blogappfirebase.core.showIt
import androidx.navigation.fragment.findNavController
import com.example.blogappfirebase.R
import com.example.blogappfirebase.core.Result
import com.example.blogappfirebase.core.hideIt
import com.example.blogappfirebase.data.remote.auth.AuthDataSource
import com.example.blogappfirebase.databinding.FragmentRegisterScreenBinding
import com.example.blogappfirebase.domain.auth.AuthRepoImpl
import com.example.blogappfirebase.presentation.auth.AuthViewModel
import com.example.blogappfirebase.presentation.auth.AuthViewModelFactory


class RegisterScreenFragment : Fragment(R.layout.fragment_register_screen) {

    private lateinit var binding: FragmentRegisterScreenBinding

    private val viewModel by viewModels<AuthViewModel> {
        AuthViewModelFactory(
            AuthRepoImpl(
                AuthDataSource()
            )
        )
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterScreenBinding.bind(view)
        goToLoginScreen()
        getInfoUserToRegister()
    }

    private fun goToLoginScreen() {
        binding.txvSingup.setOnClickListener {
            findNavController().navigate(R.id.action_registerScreenFragment_to_loginScreenFragment)
        }
    }

    private fun getInfoUserToRegister() {
        binding.btnSingup.setOnClickListener {
            val username = binding.edtUsername.text.toString().trim()
            val email = binding.edtEmail.text.toString().trim()
            val pass = binding.edtPassword.text.toString().trim()
            val confpass = binding.edtConfirmPassword.text.toString().trim()
            validateCredentials(
                username = username,
                email = email,
                pass = pass,
                confPass = confpass
            )

        }
    }

    private fun doRegister(username: String, email: String, pass: String) {
        viewModel.singUp(username = username, email = email, pass = pass).observe(viewLifecycleOwner, { result ->
            when(result){
                is Result.Loading -> {
                    binding.progressBar.showIt()
                    binding.btnSingup.isEnabled = false
                }
                is Result.Success -> {
                    binding.progressBar.hideIt()
                    findNavController().navigate(R.id.action_registerScreenFragment_to_setupProfileScreenFragment)
                }
                is Result.Failure -> {
                    binding.progressBar.hideIt()
                    binding.btnSingup.isEnabled = true
                    Toast.makeText(
                        requireContext(),
                        "Error: ${result.exception}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun validateCredentials(
        username: String,
        email: String,
        pass: String,
        confPass: String
    ) {
        when {
            username.isEmpty() -> {
                binding.edtUsername.error = "Username is empty"
                return
            }
            email.isEmpty() -> {
                binding.edtEmail.error = "e-mail is empty"
                return
            }
            pass.isEmpty() -> {
                binding.edtPassword.error = "password is empty"
                return
            }
            confPass.isEmpty() -> {
                binding.edtConfirmPassword.error = "password is empty"
                return
            }
            confPass != pass -> {
                binding.edtConfirmPassword.error = "The password is not equals"
                return
            }
        }
        doRegister(username = username, email = email, pass = pass)
    }
}