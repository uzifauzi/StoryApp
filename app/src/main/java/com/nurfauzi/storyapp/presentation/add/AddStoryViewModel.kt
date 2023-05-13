package com.nurfauzi.storyapp.presentation.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.nurfauzi.storyapp.data.StoryRepository
import com.nurfauzi.storyapp.data.preferences.LoginPreferences
import com.nurfauzi.storyapp.domain.User
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(
    private val storyRepository: StoryRepository,
    private val loginPreferences: LoginPreferences
) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getToken(): LiveData<User> {
        return loginPreferences.getToken().asLiveData()
    }

    fun postStory(imageMultipart: MultipartBody.Part, description: RequestBody, token: String) =
        storyRepository.uploadImage(imageMultipart, description, token)
}