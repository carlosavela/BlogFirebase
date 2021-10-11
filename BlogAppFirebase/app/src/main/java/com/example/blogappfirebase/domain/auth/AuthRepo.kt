package com.example.blogappfirebase.domain.auth

import android.graphics.Bitmap
import com.google.firebase.auth.FirebaseUser

interface AuthRepo {
    suspend fun singIn(email: String, pass: String): FirebaseUser?

    suspend fun singUp(email: String, pass: String, username: String): FirebaseUser?

    suspend fun updateProfile(imageBitmap: Bitmap, username: String)
}