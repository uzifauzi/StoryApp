package com.nurfauzi.storyapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import com.nurfauzi.storyapp.data.local.database.StoryDatabase
import com.nurfauzi.storyapp.data.network.api.ApiService
import com.nurfauzi.storyapp.data.network.responses.FileUploadResponse
import com.nurfauzi.storyapp.data.network.responses.ListStoryItem
import com.nurfauzi.storyapp.data.network.responses.LoginResponse
import com.nurfauzi.storyapp.data.network.responses.RegisterResponse
import com.nurfauzi.storyapp.domain.Story
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository(
    private val apiService: ApiService,
    private val database: StoryDatabase
) {

    fun postRegister(
        username: String,
        email: String,
        password: String
    ): LiveData<StoryResult<RegisterResponse>> = liveData {
        emit(StoryResult.Loading)
        try {
            val registerResponse = apiService.register(username, email, password)
            emit(StoryResult.Success(registerResponse))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(StoryResult.Error(e.toString()))
        }
    }

    fun postLogin(email: String, password: String): LiveData<StoryResult<LoginResponse>> =
        liveData {
            emit(StoryResult.Loading)
            try {
                val loginResponse = apiService.login(email, password)
                emit(StoryResult.Success(loginResponse))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(StoryResult.Error(e.toString()))
            }
        }

    fun getAllStories(token: String): LiveData<StoryResult<List<ListStoryItem>>> = liveData {
        emit(StoryResult.Loading)
        try {
            val mainResponse = apiService.getAllStories("Bearer $token")
            val listStories = mainResponse.listStory
            if (listStories.isNotEmpty()) {
                emit(StoryResult.Success(listStories))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(StoryResult.Error(e.toString()))
        }
    }

    fun getAllStoriesWithLocation(token: String): LiveData<PagingData<Story>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = 5),
            remoteMediator = StoryRemoteMediator(
                database,
                apiService,
                token
            ),
            pagingSourceFactory = {
                database.storyDao().getAllStory()
            }
        ).liveData
    }

    fun uploadImage(
        imageMultipart: MultipartBody.Part,
        description: RequestBody,
        token: String
    ): LiveData<StoryResult<FileUploadResponse>> = liveData {
        emit(StoryResult.Loading)
        try {
            val uploadResponse =
                apiService.uploadImage(imageMultipart, description, "Bearer $token")
            if (!uploadResponse.error) {
                Log.d("Upload Image : ", uploadResponse.toString())
                emit(StoryResult.Success(uploadResponse))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(StoryResult.Error(e.toString()))
        }
    }

    fun getAllStoriesMap(location: Int, token: String): LiveData<StoryResult<List<ListStoryItem>>> =
        liveData {
            emit(StoryResult.Loading)
            try {
                val mapResponse = apiService.getAllStoriesMap(location, "Bearer $token")
                val listStories = mapResponse.listStory
                if (listStories.isNotEmpty()) {
                    emit(StoryResult.Success(listStories))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emit(StoryResult.Error(e.toString()))
            }
        }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            apiService: ApiService,
            database: StoryDatabase
        ): StoryRepository = instance ?: synchronized(this) {
            instance ?: StoryRepository(apiService, database)
        }.also { instance = it }
    }
}