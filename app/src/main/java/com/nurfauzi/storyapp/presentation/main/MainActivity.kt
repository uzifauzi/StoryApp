package com.nurfauzi.storyapp.presentation.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nurfauzi.storyapp.R
import com.nurfauzi.storyapp.data.preferences.LoginPreferences
import com.nurfauzi.storyapp.data.preferences.ViewModelFactory
import com.nurfauzi.storyapp.databinding.ActivityMainBinding
import com.nurfauzi.storyapp.domain.User
import com.nurfauzi.storyapp.presentation.add.AddStoryActivity
import com.nurfauzi.storyapp.presentation.login.LoginActivity
import com.nurfauzi.storyapp.presentation.map.MapsActivity
import com.nurfauzi.storyapp.ui.adapter.LoadingStateAdapter
import com.nurfauzi.storyapp.ui.adapter.StoryPagingAdapter

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var user: User
    private lateinit var adapter: StoryPagingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pref = LoginPreferences.getInstance(dataStore)
        val factory = ViewModelFactory.getInstance(this, pref)
        mainViewModel = ViewModelProvider(this, factory).get(
            MainViewModel::class.java
        )

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel.getToken().observe(this) {
            user = it
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvStory.layoutManager = layoutManager

        getStoryData()

        binding.fabCamera.setOnClickListener {
            val intent = Intent(this@MainActivity, AddStoryActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_map -> {
                val intentMap = Intent(this@MainActivity, MapsActivity::class.java)
                intentMap.putExtra(MapsActivity.EXTRA_TOKEN, user.token)
                startActivity(intentMap)
            }
            R.id.menu_logout -> {
                mainViewModel.clearToken()
                val intentLogin = Intent(this@MainActivity, LoginActivity::class.java)
                intentLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intentLogin)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getStoryData() {
        adapter = StoryPagingAdapter()
        binding.rvStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
    }

    override fun onResume() {
        super.onResume()
            mainViewModel.getStories(user.token).observe(this) {
                adapter.submitData(lifecycle, it)
            }
        adapter.refresh()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}