package com.nurfauzi.storyapp.presentation.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.nurfauzi.storyapp.data.StoryRepository
import com.nurfauzi.storyapp.data.preferences.LoginPreferences
import com.nurfauzi.storyapp.domain.User

class MapsViewModel(private val storyRepository: StoryRepository, private val loginPreferences: LoginPreferences): ViewModel() {

    fun getToken(): LiveData<User> {
        return loginPreferences.getToken().asLiveData()
    }

    fun getAllStoryLocation(location: Int, token: String) = storyRepository.getAllStoriesMap(1, token)
}