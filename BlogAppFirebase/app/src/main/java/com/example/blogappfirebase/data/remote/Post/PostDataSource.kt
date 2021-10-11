package com.example.blogappfirebase.data.remote.Post

import android.graphics.Bitmap
import com.example.blogappfirebase.data.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.*

class PostDataSource {

    suspend fun uploadPhoto(imageBitmap: Bitmap, description: String) {
        // obtener el usuario logeado
        val user = FirebaseAuth.getInstance().currentUser
        // referencia de donde se va a guardar la imagen en el firebaseStorage
        val imaRef = FirebaseStorage.getInstance().reference
            .child("posts")
            .child("${user?.uid}")
            .child("${UUID.randomUUID()}")
        // baos para comprimir la imagen
        val baos = ByteArrayOutputStream()
        //comprimir la /
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        // subir la imagen a firebaseStorage y obtenermos la url de la imagen
        val downloadUrl =
            imaRef.putBytes(baos.toByteArray()).await().storage.downloadUrl.await().toString()

        user?.let { user1 ->
            FirebaseFirestore.getInstance().collection("posts").add(
                Post(
                    profile_name = user1.displayName.toString(),
                    profile_picture = user1.photoUrl.toString(),
                    post_image = downloadUrl,
                    post_description = description,
                    uid_user_post = user1.uid,
                )
            ).await()
        }
    }
}