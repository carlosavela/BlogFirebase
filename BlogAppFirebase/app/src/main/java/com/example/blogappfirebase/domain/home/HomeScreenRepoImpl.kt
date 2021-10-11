package com.example.blogappfirebase.domain.home

import com.example.blogappfirebase.core.Result
import com.example.blogappfirebase.data.model.Post
import com.example.blogappfirebase.data.remote.home.HomeScreenDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

class HomeScreenRepoImpl(private val dataSource: HomeScreenDataSource) : HomeScreenRepo {

    @ExperimentalCoroutinesApi
    override suspend fun getLatestPosts(): Flow<Result<List<Post>>> = dataSource.getLatesPosts()
}