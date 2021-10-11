package com.example.blogappfirebase.data.remote.home

import com.example.blogappfirebase.core.Result
import com.example.blogappfirebase.data.model.Post
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


// va a buscar y a traer toda la informacion en firebase
class HomeScreenDataSource {

    // Flow para que los post se esten actualizando en tiempo realz
    @ExperimentalCoroutinesApi
    suspend fun getLatesPosts(): Flow<Result<List<Post>>> = callbackFlow {

        val postList = mutableListOf<Post>()

        var postRerefente: Query? = null
        try {
            postRerefente = FirebaseFirestore
                .getInstance()
                .collection("posts")
                .orderBy("created_at", Query.Direction.DESCENDING)

        } catch (e: Throwable) {
            close(e)
        }
        val suscription = postRerefente?.addSnapshotListener { value, error ->
            if (value == null) {
                return@addSnapshotListener
            }
            try {
                postList.clear()
                for (post in value.documents) {
                    post.toObject(Post::class.java)?.let { fbPost ->
                        fbPost.apply {
                            created_at = post.getTimestamp(
                                "created_at",
                                DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                            )?.toDate()
                        }
                        postList.add(fbPost)
                    }
                }
            } catch (e: Exception) {
                close(e)
            }

            this.trySend(Result.Success(postList)).isSuccess
        }
        awaitClose { suscription?.remove() }
    }
}