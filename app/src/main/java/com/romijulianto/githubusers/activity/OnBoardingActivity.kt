package com.romijulianto.githubusers.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.romijulianto.githubusers.R

class OnBoardingActivity : AppCompatActivity(), View.OnClickListener  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)

        supportActionBar?.hide()

        val btnMoveToHomeActivity: Button = findViewById(R.id.button_started)
        btnMoveToHomeActivity.setOnClickListener(this@OnBoardingActivity)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_started -> {
                val moveIntent = Intent(this@OnBoardingActivity, HomeActivity::class.java)
                startActivity(moveIntent)
            }
        }
    }
}