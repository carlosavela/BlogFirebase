package com.example.blogappfirebase

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.blogappfirebase.core.hideIt
import com.example.blogappfirebase.core.showIt
import com.example.blogappfirebase.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Permite hacer la nevegacion por medio del fragmentContainer
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        // se crea un navController
        navController = navHostFragment.navController
        // hacer que le bottomNavigation haga la navegacion
        binding.bottomNavigation.setupWithNavController(navController = navController)
        // Ocultar el bottomNavigation cuando esta en el fragment de login y de registro
        observeDestinationChange()
    }
    private fun observeDestinationChange() {
        // Ocultar el bottomNavigation cuando esta en el fragment de login y de registro
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginScreenFragment -> {
                    binding.bottomNavigation.hideIt()
                }
                R.id.registerScreenFragment -> {
                    binding.bottomNavigation.hideIt()
                }
                R.id.setupProfileScreenFragment -> {
                    binding.bottomNavigation.hideIt()
                }
                else -> {
                    binding.bottomNavigation.showIt()
                }
            }
        }
    }
}