package com.example.blogappfirebase.domain.home

import com.example.blogappfirebase.core.Result
import com.example.blogappfirebase.data.model.Post
import kotlinx.coroutines.flow.Flow

interface HomeScreenRepo {
    suspend fun getLatestPosts() : Flow<Result<List<Post>>>
}