package com.example.blogappfirebase.domain.auth

import android.graphics.Bitmap
import com.example.blogappfirebase.data.remote.auth.AuthDataSource
import com.google.firebase.auth.FirebaseUser

class AuthRepoImpl(private val dataSource: AuthDataSource) : AuthRepo {

    override suspend fun singIn(email: String, pass: String): FirebaseUser? =
        dataSource.singIn(email, pass)

    override suspend fun singUp(email: String, pass: String, username: String): FirebaseUser? =
        dataSource.singUp(email, pass, username)

    override suspend fun updateProfile(imageBitmap: Bitmap, username: String) =
        dataSource.updateUserProfile(imageBitmap, username)
}