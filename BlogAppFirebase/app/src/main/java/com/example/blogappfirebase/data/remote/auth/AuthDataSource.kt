package com.example.blogappfirebase.data.remote.auth

import android.graphics.Bitmap
import android.net.Uri
import com.example.blogappfirebase.data.model.User
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream

class AuthDataSource {

    //login en firebase
    suspend fun singIn(email: String, pass: String): FirebaseUser? {
        val authResult = FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pass).await()
        return authResult.user
    }

    suspend fun singUp(email: String, pass: String, username: String): FirebaseUser? {
        val authResult =
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass).await()
        // crear una coleccion de usuarios y hacer su id como identificador de ese usuario y setear el
        // valor de ese usuario por medio de un objeto
        authResult.user?.uid?.let { uid ->
            FirebaseFirestore.getInstance().collection("users").document(uid)
                .set(User(username = username, email = email, dateCreation = Timestamp.now()))
                .await()
        }
        return authResult.user
    }

    suspend fun updateUserProfile(imageBitmap: Bitmap, username: String) {
        // obtener el usuario logeado
        val userId = FirebaseAuth.getInstance().currentUser
        // referencia de donde se va a guardar la imagen en el firebaseStorage
        val imaRef =
            FirebaseStorage.getInstance().reference.child("users_photos").child("${userId?.uid}")
        // baos para comprimir la imagen
        val baos = ByteArrayOutputStream()
        //comprimir la /
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        // subir la imagen a firebaseStorage y obtenermos la url de la imagen
        val downloadUrl =
            imaRef.putBytes(baos.toByteArray()).await().storage.downloadUrl.await().toString()
        // Darle al usuario una imagen de perfil y un username
        val profileUpdate = UserProfileChangeRequest.Builder().setDisplayName(username).setPhotoUri(
            Uri.parse(downloadUrl)
        ).build()
        userId?.updateProfile(profileUpdate)?.await()

        //Actualizar la informacion de del usuario
        userId?.let {
            FirebaseFirestore.getInstance().collection("users").document(it.uid)
                .update(mapOf("userPhotoUrl" to downloadUrl, "username" to username))
                .await()
        }
    }
}