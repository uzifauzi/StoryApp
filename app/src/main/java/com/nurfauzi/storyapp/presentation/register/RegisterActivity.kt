package com.nurfauzi.storyapp.presentation.register

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import android.view.View
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.nurfauzi.storyapp.data.StoryRepository
import com.nurfauzi.storyapp.data.StoryResult
import com.nurfauzi.storyapp.data.local.database.StoryDatabase
import com.nurfauzi.storyapp.data.network.api.ApiConfig
import com.nurfauzi.storyapp.data.network.api.ApiService
import com.nurfauzi.storyapp.data.preferences.LoginPreferences
import com.nurfauzi.storyapp.data.preferences.ViewModelFactory
import com.nurfauzi.storyapp.databinding.ActivityRegisterBinding
import com.nurfauzi.storyapp.presentation.login.LoginActivity
import com.nurfauzi.storyapp.presentation.login.LoginViewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    private lateinit var name: String
    private lateinit var email: String
    private lateinit var password: String

    private var isLessThanEight: Boolean = false

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val btnRegister = binding.btnRegister

        val pref = LoginPreferences.getInstance(dataStore)
        val factory = ViewModelFactory.getInstance(this, pref)
        registerViewModel = ViewModelProvider(this, factory).get(
            RegisterViewModel::class.java
        )

        registerViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        btnRegister.setOnClickListener {
            name = binding.editNameText.text.toString()
            email = binding.editEmailText.text.toString()
            password = binding.editPasswordText.text.toString()
            when {
                name == "" -> {
                    binding.editNameText.error = "nama tidak boleh kosong!"
                }
                email == "" -> {
                    binding.editEmailText.error = "email tidak boleh kosong!"
                }
                password == "" -> {
                    binding.editPasswordText.error = "password tidak boleh kosong!"
                }
                password.length < 8 -> {
                    binding.editPasswordText.error = "password kurang dari 8 karakter!"
                }
                else -> {
                    isLessThanEight = true
                }
            }
            if (isLessThanEight) {
                userRegister()
            }
        }
    }

    private fun userRegister() {
        registerViewModel.postRegister(
            name, email, password
        ).observe(this) { result ->
            when (result) {
                is StoryResult.Success -> {
                    showLoading(false)
                    Toast.makeText(this, "Akun berhasil dibuat", Toast.LENGTH_SHORT).show()
                    val intentLogin = Intent(this, LoginActivity::class.java)
                    startActivity(intentLogin)
                }
                is StoryResult.Loading -> {
                    showLoading(true)
                }
                is StoryResult.Error -> {
                    Toast.makeText(this, "Akun gagal dibuat", Toast.LENGTH_SHORT).show()
                    showLoading(false)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}