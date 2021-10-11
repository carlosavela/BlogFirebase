package com.example.blogappfirebase.presentation.post

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.blogappfirebase.core.Result
import com.example.blogappfirebase.domain.post.PostRepo
import kotlinx.coroutines.Dispatchers

class PostViewModel(private val repo: PostRepo) : ViewModel() {
    fun uploatPost(imageBitmap: Bitmap, description: String) = liveData(Dispatchers.IO) {
        emit(Result.Loading())
        try {
            emit(Result.Success(repo.uploadPhoto(imageBitmap, description)))
        } catch (e: Exception) {
            emit(Result.Failure(e))
        }
    }
}

class PostViewModelFactory(private val repo: PostRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(PostRepo::class.java).newInstance(repo)
    }
}