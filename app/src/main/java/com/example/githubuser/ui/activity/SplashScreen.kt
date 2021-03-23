package com.example.githubuser.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.view.animation.*
import com.example.githubuser.MainActivity
import com.example.githubuser.R
import com.example.githubuser.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    companion object {
        const val TIME = 3000L
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar?.hide()

        animation()

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, TIME)
    }

    private fun animation() {
        val splash = AnimationUtils.loadAnimation(this, R.anim.splash)
        val text = AnimationUtils.loadAnimation(this, R.anim.text)

        binding.splash.startAnimation(splash)
        binding.text.startAnimation(text)
    }
}