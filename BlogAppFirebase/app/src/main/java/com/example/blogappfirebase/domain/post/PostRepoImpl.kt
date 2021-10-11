package com.example.blogappfirebase.domain.post

import android.graphics.Bitmap
import com.example.blogappfirebase.data.remote.Post.PostDataSource

class PostRepoImpl(private val dataSource: PostDataSource) : PostRepo {
    override suspend fun uploadPhoto(bitmap: Bitmap, description: String) =
        dataSource.uploadPhoto(bitmap, description)
}