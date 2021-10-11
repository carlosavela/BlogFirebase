package com.example.blogappfirebase.data.model

import com.google.firebase.Timestamp

data class User(
    val username: String = "",
    val email: String = "",
    val userPhotoUrl: String = "",
    val dateCreation: Timestamp? = null,
    val name: String = "",
    val last_name: String = ""
)