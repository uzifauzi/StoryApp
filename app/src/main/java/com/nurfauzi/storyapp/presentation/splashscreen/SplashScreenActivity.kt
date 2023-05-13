package com.nurfauzi.storyapp.presentation.splashscreen

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import com.nurfauzi.storyapp.databinding.ActivitySplashScreenBinding
import com.nurfauzi.storyapp.presentation.login.LoginActivity

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private val delayDuration: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, delayDuration)

        playAnimation()
    }

    private fun playAnimation() {
        val image = ObjectAnimator.ofFloat(binding.ivSplashScreen, View.ALPHA, 1f).setDuration(1000)
        val title = ObjectAnimator.ofFloat(binding.tvNameApp, View.ALPHA, 1f).setDuration(1000)

        AnimatorSet().apply {
            playSequentially(image, title)
            start()
        }
    }
}