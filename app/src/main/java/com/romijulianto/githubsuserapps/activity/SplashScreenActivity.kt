package com.romijulianto.githubsuserapps.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.romijulianto.githubsuserapps.R

@Suppress("DEPRECATION")
@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.hide()

        Handler().postDelayed({
            startActivity(Intent(this@SplashScreenActivity, OnBoardingActivity::class.java))
            finish()
        }, 3000)
    }
}