package com.nurfauzi.storyapp.presentation.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.nurfauzi.storyapp.data.StoryResult
import com.nurfauzi.storyapp.data.preferences.LoginPreferences
import com.nurfauzi.storyapp.data.preferences.ViewModelFactory
import com.nurfauzi.storyapp.databinding.ActivityLoginBinding
import com.nurfauzi.storyapp.domain.User
import com.nurfauzi.storyapp.presentation.main.MainActivity
import com.nurfauzi.storyapp.presentation.register.RegisterActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var user: User

    private lateinit var email: String
    private lateinit var password: String
    private var isLessThanEight: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = LoginPreferences.getInstance(dataStore)
        val factory = ViewModelFactory.getInstance(this, pref)
        loginViewModel = ViewModelProvider(this, factory).get(
            LoginViewModel::class.java
        )

        loginViewModel.getToken().observe(this) {
            if (it.token != "token") {
                val intentMain = Intent(this, MainActivity::class.java)
                startActivity(intentMain)
                finish()
            }
        }

        val btnLogin = binding.btnLogin
        val btnToRegister = binding.btnToRegistration

        loginViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        btnLogin.setOnClickListener {
            email = binding.edLoginEmail.text.toString()
            password = binding.edLoginPassword.text.toString()
            when {
                email == "" -> {
                    binding.edLoginEmail.error = "email tidak boleh kosong!"
                }
                password == "" -> {
                    binding.edLoginPassword.error = "password tidak boleh kosong!"
                }
                password.length < 8 -> {
                    binding.edLoginPassword.error = "password kurang dari 8 karakter!"
                }
                else -> {
                    isLessThanEight = true
                }
            }
            if (isLessThanEight) {
                loginViewModel.postLogin(email, password).observe(this) { result ->
                    when (result) {
                        is StoryResult.Success -> {
                            showLoading(false)
                            user = User(result.data.loginResult.token)
                            loginViewModel.saveToken(user)
                            Toast.makeText(this, "Berhasil Login", Toast.LENGTH_SHORT).show()
                            val intentMain = Intent(this, MainActivity::class.java)
                            startActivity(intentMain)
                            finish()
                        }
                        is StoryResult.Loading -> {
                            showLoading(true)
                        }
                        is StoryResult.Error -> {
                            Toast.makeText(this, "Login gagal.", Toast.LENGTH_SHORT).show()
                            showLoading(false)
                        }
                    }
                }
            }
        }

        btnToRegister.setOnClickListener {
            val intentToRegis = Intent(this, RegisterActivity::class.java)
            startActivity(intentToRegis)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}