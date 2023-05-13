package com.nurfauzi.storyapp.di

import android.content.Context
import com.nurfauzi.storyapp.data.StoryRepository
import com.nurfauzi.storyapp.data.local.database.StoryDatabase
import com.nurfauzi.storyapp.data.network.api.ApiConfig

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val apiService = ApiConfig.getApiService()
        val database = StoryDatabase.getDatabase(context)
        return StoryRepository.getInstance(apiService, database)
    }
}