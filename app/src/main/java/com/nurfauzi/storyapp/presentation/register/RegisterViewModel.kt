package com.nurfauzi.storyapp.presentation.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nurfauzi.storyapp.data.StoryRepository

class RegisterViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun postRegister(username: String, email: String, password: String) =
        storyRepository.postRegister(username, email, password)
}

