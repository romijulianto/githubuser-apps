package com.romijulianto.githubsuserapps.activity

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.romijulianto.githubsuserapps.R
import com.romijulianto.githubsuserapps.databinding.ActivityDetailUserBinding
import com.romijulianto.githubsuserapps.model.UserGithubs
import com.bumptech.glide.Glide

class DetailUserActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var user: UserGithubs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = intent.extras?.get(EXTRA_DETAIL) as UserGithubs
        parseUserDetail(user)

        binding.btnBack.setOnClickListener(this)
        binding.btnOpen.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_back -> {
                finish()
            }

            R.id.btn_open -> {
                val url = "https://www.github.com/${user.username}"
                Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(url)
                }.also {
                    startActivity(it)
                }
            }
        }
    }

    private fun parseUserDetail(user: UserGithubs) {
        binding.apply {
            tvName.text = user.name
            tvUsername.text = user.username
            tvRepositories.text = user.repository.toString()
            tvFollowers.text = user.follower.toString()
            tvFollowing.text = user.following.toString()
            tvCompany.text = user.company
            tvLocation.text = user.location

            Glide
                .with(this@DetailUserActivity)
                .load(user.avatar)
                .placeholder(R.drawable.profile_placeholder)
                .into(ivAvatar)
        }
    }

    companion object {
        const val EXTRA_DETAIL = "extra_detail"
    }
}