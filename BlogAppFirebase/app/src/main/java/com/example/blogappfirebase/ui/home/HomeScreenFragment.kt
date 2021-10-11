package com.example.blogappfirebase.ui.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.blogappfirebase.R
import com.example.blogappfirebase.core.Result
import com.example.blogappfirebase.core.hideIt
import com.example.blogappfirebase.core.showIt
import com.example.blogappfirebase.data.remote.home.HomeScreenDataSource
import com.example.blogappfirebase.databinding.FragmentHomeScreenBinding
import com.example.blogappfirebase.domain.home.HomeScreenRepoImpl
import com.example.blogappfirebase.presentation.home.HomeScreenViewModel
import com.example.blogappfirebase.presentation.home.HomeScreenViewModelFactory
import com.example.blogappfirebase.ui.home.adapter.HomeScreenAdapter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeScreenFragment : Fragment(R.layout.fragment_home_screen) {

    private lateinit var binding: FragmentHomeScreenBinding

    // crea el viewMopdel con un delegadfor
    private val viewModel by viewModels<HomeScreenViewModel> {
        //inyeccion de dependencia
        HomeScreenViewModelFactory(HomeScreenRepoImpl(HomeScreenDataSource()))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeScreenBinding.bind(view)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.latestPostFlow().collect { result ->
                    when (result) {
                        is Result.Loading -> binding.progressBar.showIt()
                        is Result.Success -> {
                            binding.progressBar.hideIt()
                            if (result.data.isEmpty()) {
                                binding.emptyPost.showIt()
                                return@collect
                            } else {
                                binding.emptyPost.hideIt()
                            }
                            binding.rvHome.adapter = HomeScreenAdapter(result.data)
                        }
                        is Result.Failure -> {
                            binding.emptyPost.hideIt()
                            binding.progressBar.hideIt()
                            Toast.makeText(
                                requireContext(),
                                "Ocurrio un error en: ${result.exception}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }

//        viewModel.fetchLatestPosts().observe(viewLifecycleOwner, /*Observer*/ { result ->
//            when (result) {
//                is Result.Loading -> binding.progressBar.showIt()
//                is Result.Success -> {
//                    binding.progressBar.hideIt()
//                    if (result.data.isEmpty()) {
//                        binding.emptyPost.showIt()
//                        return@observe
//                    } else {
//                        binding.emptyPost.hideIt()
//                    }
//                    binding.rvHome.adapter = HomeScreenAdapter(result.data)
//                }
//                is Result.Failure -> {
//                    binding.emptyPost.hideIt()
//                    binding.progressBar.hideIt()
//                    Toast.makeText(
//                        requireContext(),
//                        "Ocurrio un error en: ${result.exception}",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
//        })
    }
}