package com.nurfauzi.storyapp.presentation.login

import androidx.lifecycle.*
import com.nurfauzi.storyapp.data.StoryRepository
import com.nurfauzi.storyapp.data.preferences.LoginPreferences
import com.nurfauzi.storyapp.domain.User
import kotlinx.coroutines.launch

class LoginViewModel(
    private val storyRepository: StoryRepository,
    private val loginPreferences: LoginPreferences
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun postLogin(email: String, password: String) = storyRepository.postLogin(email, password)

    fun getToken(): LiveData<User> {
        return loginPreferences.getToken().asLiveData()
    }

    fun saveToken(user: User) {
        viewModelScope.launch {
            loginPreferences.saveToken(user)
        }
    }
}