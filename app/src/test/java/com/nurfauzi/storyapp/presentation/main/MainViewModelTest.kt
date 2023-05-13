package com.nurfauzi.storyapp.presentation.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.nurfauzi.storyapp.DataDummy
import com.nurfauzi.storyapp.MainDispatcherRule
import com.nurfauzi.storyapp.data.StoryRepository
import com.nurfauzi.storyapp.data.network.responses.ListStoryItem
import com.nurfauzi.storyapp.data.preferences.LoginPreferences
import com.nurfauzi.storyapp.domain.Story
import com.nurfauzi.storyapp.getOrAwaitValue
import com.nurfauzi.storyapp.ui.adapter.StoryPagingAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    @Mock
    private lateinit var loginPreferences: LoginPreferences

    @Test
    fun `when Get Story Should Not Null and Return Data`() = runTest {
        val dummyStory = DataDummy.generateDummyStoryResponse()
        val tokenDummy = DataDummy.getTokenDummy()
        val data: PagingData<Story> = StoryPagingSource.snapshot(dummyStory)
        val expectedStory = MutableLiveData<PagingData<Story>>()
        expectedStory.value = data
        Mockito.`when`(storyRepository.getAllStoriesWithLocation(tokenDummy)).thenReturn(expectedStory)

        val mainViewModel = MainViewModel(storyRepository, loginPreferences)
        val actualStory: PagingData<Story> = mainViewModel.getStories(tokenDummy).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryPagingAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStory.size, differ.snapshot().size)
        Assert.assertEquals(dummyStory[0], differ.snapshot()[0])
    }

    @Test
    fun `when Get Story Empty Should Return No Data`() = runTest {
        val data: PagingData<Story> = PagingData.from(emptyList())
        val tokenDummy = DataDummy.getTokenDummy()
        val expectedStory = MutableLiveData<PagingData<Story>>()
        expectedStory.value = data
        Mockito.`when`(storyRepository.getAllStoriesWithLocation(tokenDummy)).thenReturn(expectedStory)
        val mainViewModel = MainViewModel(storyRepository, loginPreferences)
        val actualStory: PagingData<Story> = mainViewModel.getStories(tokenDummy).getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryPagingAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)
        Assert.assertEquals(0, differ.snapshot().size)
    }
}

class StoryPagingSource : PagingSource<Int, LiveData<List<Story>>>() {
    companion object {
        fun snapshot(items: List<Story>): PagingData<Story> {
            return PagingData.from(items)
        }
    }
    override fun getRefreshKey(state: PagingState<Int, LiveData<List<Story>>>): Int {
        return 0
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<Story>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}