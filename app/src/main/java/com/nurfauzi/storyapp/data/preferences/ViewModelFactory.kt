package com.nurfauzi.storyapp.data.preferences

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nurfauzi.storyapp.data.StoryRepository
import com.nurfauzi.storyapp.di.Injection
import com.nurfauzi.storyapp.presentation.add.AddStoryViewModel
import com.nurfauzi.storyapp.presentation.login.LoginViewModel
import com.nurfauzi.storyapp.presentation.main.MainViewModel
import com.nurfauzi.storyapp.presentation.map.MapsViewModel
import com.nurfauzi.storyapp.presentation.register.RegisterViewModel

class ViewModelFactory constructor(
    private val storyRepository: StoryRepository,
    private val pref: LoginPreferences
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(storyRepository) as T
        } else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(storyRepository, pref) as T
        } else if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(storyRepository, pref) as T
        } else if (modelClass.isAssignableFrom(AddStoryViewModel::class.java)) {
            return AddStoryViewModel(storyRepository, pref) as T
        } else if (modelClass.isAssignableFrom(MapsViewModel::class.java)) {
            return MapsViewModel(storyRepository, pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context, pref: LoginPreferences): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context), pref)
            }.also { instance = it }
    }
}