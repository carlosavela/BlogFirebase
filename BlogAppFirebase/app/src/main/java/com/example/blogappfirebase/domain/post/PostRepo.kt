package com.example.blogappfirebase.domain.post

import android.graphics.Bitmap

interface PostRepo {
    suspend fun uploadPhoto(bitmap: Bitmap, description: String)
}