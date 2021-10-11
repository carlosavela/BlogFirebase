package com.example.blogappfirebase.ui.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.blogappfirebase.R
import com.example.blogappfirebase.core.Result
import com.example.blogappfirebase.core.hideIt
import com.example.blogappfirebase.core.showIt
import com.example.blogappfirebase.data.remote.auth.AuthDataSource
import com.example.blogappfirebase.databinding.FragmentLoginScreenBinding
import com.example.blogappfirebase.domain.auth.AuthRepoImpl
import com.example.blogappfirebase.presentation.auth.AuthViewModel
import com.example.blogappfirebase.presentation.auth.AuthViewModelFactory
import com.google.firebase.auth.FirebaseAuth

class LoginScreenFragment : Fragment(R.layout.fragment_login_screen) {

    private lateinit var binding: FragmentLoginScreenBinding
    private val viewModel by viewModels<AuthViewModel> {
        AuthViewModelFactory(
            AuthRepoImpl(
                AuthDataSource()
            )
        )
    }

    // Esta variable se inicializa de esta manera, para que se inicialice cuando se vaya a necesitar
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginScreenBinding.bind(view)
        isUserLoggedIn()
        doLogin()
        goToSingUpScreen()

    }

    private fun isUserLoggedIn() {
        // obtener el usuario actual y saber si no es null
        firebaseAuth.currentUser?.let { user ->
            // ir al home si el usuario ya esta loggedIn
            if (user.displayName.isNullOrEmpty()) {
                findNavController().navigate(R.id.action_loginScreenFragment_to_setupProfileScreenFragment)
            } else {
                findNavController().navigate(R.id.action_loginScreenFragment_to_homeScreenFragment)
            }
        }

    }

    private fun doLogin() {
        binding.btnSingin.setOnClickListener {
            val email = binding.edtEmail.text.toString().trim()
            val pass = binding.edtPassword.text.toString().trim()
            validateCredentiales(email, pass)
            singIn(email, pass)
        }
    }

    private fun goToSingUpScreen() {
        binding.txvSingup.setOnClickListener {
            findNavController().navigate(R.id.action_loginScreenFragment_to_registerScreenFragment)
        }
    }

    private fun validateCredentiales(
        email: String,
        pass: String
    ) {
        if (email.isEmpty()) {
            binding.edtEmail.error = "e-mail is empty"
            return
        } else if (pass.isEmpty()) {
            binding.edtPassword.error = "password is empty"
            return
        }
    }

    private fun singIn(emial: String, pass: String) {
        viewModel.singIn(emial, pass).observe(viewLifecycleOwner,  { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.showIt()
                    binding.btnSingin.isEnabled = false
                }
                is Result.Success -> {
                    binding.progressBar.hideIt()
                    if (result.data?.displayName.isNullOrEmpty()) {
                        findNavController().navigate(R.id.action_loginScreenFragment_to_setupProfileScreenFragment)
                    } else {
                        findNavController().navigate(R.id.action_loginScreenFragment_to_homeScreenFragment)
                    }
                }
                is Result.Failure -> {
                    binding.progressBar.hideIt()
                    binding.btnSingin.isEnabled = true
                    Toast.makeText(
                        requireContext(),
                        "Error: ${result.exception}",
                        Toast.LENGTH_SHORT
                    ).show()
                    when (result.exception) {
                        is IllegalArgumentException -> {
                            Toast.makeText(
                                requireContext(),
                                "Error: Campos incompletos",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        })
    }
}