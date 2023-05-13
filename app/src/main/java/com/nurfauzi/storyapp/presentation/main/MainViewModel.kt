package com.nurfauzi.storyapp.presentation.main

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.nurfauzi.storyapp.data.StoryRepository
import com.nurfauzi.storyapp.data.preferences.LoginPreferences
import com.nurfauzi.storyapp.domain.Story
import com.nurfauzi.storyapp.domain.User
import kotlinx.coroutines.launch

class MainViewModel(private val storyRepository: StoryRepository, private val loginPreferences: LoginPreferences) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

        fun getToken(): LiveData<User> {
        return loginPreferences.getToken().asLiveData()
    }

    fun clearToken() {
        viewModelScope.launch {
            loginPreferences.clearToken()
        }
    }

    fun getStories(token: String): LiveData<PagingData<Story>> {
        return storyRepository.getAllStoriesWithLocation(token).cachedIn(viewModelScope)
    }


}