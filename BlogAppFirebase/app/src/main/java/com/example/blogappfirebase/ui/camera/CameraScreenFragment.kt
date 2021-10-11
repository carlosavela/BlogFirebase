package com.example.blogappfirebase.ui.camera

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.blogappfirebase.R
import com.example.blogappfirebase.core.Result
import com.example.blogappfirebase.data.remote.Post.PostDataSource
import com.example.blogappfirebase.databinding.FragmentCameraScreenBinding
import com.example.blogappfirebase.domain.post.PostRepoImpl
import com.example.blogappfirebase.presentation.post.PostViewModel
import com.example.blogappfirebase.presentation.post.PostViewModelFactory

class CameraScreenFragment : Fragment(R.layout.fragment_camera_screen) {
    private lateinit var binding: FragmentCameraScreenBinding
    private val REQUEST_IMAGE_CAPTURE = 1
    private var bitmap: Bitmap? = null

    private val viewModel by viewModels<PostViewModel> {
        PostViewModelFactory(
            PostRepoImpl(
                PostDataSource()
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCameraScreenBinding.bind(view)
        selectImagePost()
        binding.btnUploadPhoto.setOnClickListener { uploadPhoto() }
    }

    private fun openIntentToCamera() {
        val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePicture, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                requireContext(),
                "Error: No se encontro ninguna app para abrir la camara",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun selectImagePost() {
        binding.addPostImage.setOnClickListener { openIntentToCamera() }
    }

    private fun uploadPhoto() {
        val description = binding.edtDescription.text.toString().trim()
        bitmap?.let {
            viewModel.uploatPost(it, description).observe(viewLifecycleOwner, { result ->
                when (result) {
                    is Result.Loading -> {
                        Toast.makeText(requireContext(), "Subiendo foto", Toast.LENGTH_SHORT).show()
                    }
                    is Result.Success -> {
                        findNavController().navigate(R.id.action_cameraScreenFragment_to_homeScreenFragment)
                    }
                    is Result.Failure -> {
                        Toast.makeText(requireContext(), "${result.exception}", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            })
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            binding.addPostImage.setImageBitmap(imageBitmap)
            bitmap = imageBitmap

        }
    }
}