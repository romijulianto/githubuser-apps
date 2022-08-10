package com.romijulianto.githubusers.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.romijulianto.githubusers.R
import com.romijulianto.githubusers.database.FavoriteRoomDB
import com.romijulianto.githubusers.databinding.ActivityGithubUsersProfileBinding
import com.romijulianto.githubusers.userdata.GithubUsersJSON
import com.romijulianto.githubusers.viewmodel.GithubUsersProfileActivityViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso

class GithubUsersProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGithubUsersProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_github_users_profile)

        binding = ActivityGithubUsersProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userProfileViewModel =
            ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
                GithubUsersProfileActivityViewModel::class.java
            )

        val sharedPref = this.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        val gitUserSp = sharedPref.getString(keyId, "null")
        val gitHtmlSp = sharedPref.getString(urlId, "null")
        val gitImageSp = sharedPref.getString(imageId, "null")
        val sectionsPagerAdapter = SectionPagerActivity(this)
        val viewPager: ViewPager2 = binding.viewPager
        val tabs: TabLayout = binding.tabs

        userProfileViewModel.githubUserProfileJSON.observe(this, { userJSON ->
            if (gitUserSp != null) {
                setGitHubUserData(userJSON)
            }
        })

        if (gitUserSp != null) {
            userProfileViewModel.getGitHubUserData(gitUserSp)
            title = gitUserSp
        } else {
            userProfileViewModel.getGitHubUserData("Null")
        }

        userProfileViewModel.isToast.observe(this, { isToast ->
            showToast(isToast, userProfileViewModel.toastReason.value.toString())
        })

        userProfileViewModel.isLoading.observe(this, {
            showLoading(it)
        })

        viewPager.adapter = sectionsPagerAdapter
        viewPager.offscreenPageLimit = 2
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f

        checkFavourite(gitUserSp.toString())

        binding.fabAdd.setOnClickListener {
            val database = FavoriteRoomDB.getDatabase(this).favoriteDao()
            val exist = database.checkUserFavorites(gitUserSp.toString())
            createAlertDialog(
                !exist,
                gitUserSp.toString(),
                gitImageSp.toString(),
                gitHtmlSp.toString()
            )
        }

        binding.swipeToRefreshProfile.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                binding.swipeToRefreshProfile.isRefreshing = false
                userProfileViewModel.getGitHubUserData(title.toString())
                checkFavourite(title.toString())
            }, 2000)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        setMode(item.itemId)
        return super.onOptionsItemSelected(item)
    }

    private fun setMode(selectedMode: Int) {
        when (selectedMode) {
            R.id.action_favourite -> {
                val intent = Intent(this, FavoriteActivity::class.java)
                startActivity(intent)
            }
            R.id.action_settings -> {
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun setGitHubUserData(userJSON: GithubUsersJSON) {
        val gitNameText = binding.gitName
        val gitEmailText = binding.gitMail
        val gitLocationText = binding.gitLocation
        val gitImageDraw = binding.gitProfileImageview
        val gitCompanyText = binding.gitCompany
        val gitJoinText = binding.gitJoin
        val gitFollowersText = binding.gitFollowersCount
        val gitFollowingText = binding.gitFollowingCount
        val gitRepositoryText = binding.gitRepositoryCount

        if (userJSON.name.isNullOrBlank()) {
            gitNameText.text = "-"
        } else {
            gitNameText.text = userJSON.name
        }

        if (userJSON.email.isNullOrBlank()) {
            gitEmailText.text = "-"
        } else {
            gitEmailText.text = userJSON.email
        }

        if (userJSON.location.isNullOrBlank()) {
            gitLocationText.text = "-"
        } else {
            gitLocationText.text = userJSON.location
        }

        if (userJSON.company.isNullOrBlank()) {
            gitCompanyText.text = "-"
        } else {
            gitCompanyText.text = userJSON.company
        }

        if (userJSON.createdAt.isNullOrBlank()) {
            gitJoinText.text = "-"
        } else {
            val splitDate = userJSON.createdAt.substring(0, userJSON.createdAt.length - 10)
            gitJoinText.text = splitDate
        }

        gitFollowersText.text = userJSON.followers
        gitFollowingText.text = userJSON.following
        gitRepositoryText.text = userJSON.publicRepos
        Picasso.get().load(userJSON.avatarUrl).into(gitImageDraw)
    }

    private fun showToast(isToast: Boolean, toastReason: String) {
        if (!isToast) {
            Toast.makeText(this, toastReason, Toast.LENGTH_LONG).show()
        }
    }

    private fun checkFavourite(username: String) {
        val database =
            FavoriteRoomDB.getDatabase(this).favoriteDao()
        val exist = database.checkUserFavorites(username)

        if (exist) {
            binding.fabAdd.setImageResource(R.drawable.ic_baseline_favorite)
        } else {
            binding.fabAdd.setImageResource(R.drawable.ic_baseline_favorite_border)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.gitProfileImageview.visibility = View.VISIBLE
            binding.gitName.visibility = View.VISIBLE
            binding.gitNameIcon.visibility = View.VISIBLE
            binding.gitMail.visibility = View.VISIBLE
            binding.gitEmailIcon.visibility = View.VISIBLE
            binding.gitLocation.visibility = View.VISIBLE
            binding.gitLocationIcon.visibility = View.VISIBLE
            binding.gitRepository.visibility = View.VISIBLE
            binding.gitRepositoryCount.visibility = View.VISIBLE
            binding.gitFollowers.visibility = View.VISIBLE
            binding.gitFollowersCount.visibility = View.VISIBLE
            binding.gitFollowing.visibility = View.VISIBLE
            binding.gitFollowingCount.visibility = View.VISIBLE
        }
    }

    private fun createAlertDialog(
        state: Boolean,
        userId: String,
        userAvatar: String,
        userHtml: String
    ) {
        val userProfileViewModel =
            ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
                GithubUsersProfileActivityViewModel::class.java
            )

        val alertDialogBuilder = AlertDialog.Builder(this)

        val message =
            if (state) userId + " " + (resources.getString(R.string.favorite_not_in_list_notif)) else "$userId " + (resources.getString(
                R.string.favorite_in_list_notif
            ))

        with(alertDialogBuilder) {
            setTitle(R.string.favorite)
            setMessage(message)
            setCancelable(false)
            setPositiveButton(context.resources.getString(R.string.dialog_yes)) { _, _ ->
                if (state) {
                    userProfileViewModel.insertUserFavourite(
                        userId,
                        userAvatar,
                        userHtml,
                        this@GithubUsersProfileActivity
                    )
                    Toast.makeText(
                        context,
                        userId + " " + context.resources.getString(R.string.favorite_add_to_notif),
                        Toast.LENGTH_LONG
                    )
                        .show()
                    binding.fabAdd.setImageResource(R.drawable.ic_baseline_favorite)
                } else {
                    userProfileViewModel.deleteUserFavourite(userId, this@GithubUsersProfileActivity)
                    Toast.makeText(
                        context,
                        userId + " " + (context.resources.getString(R.string.favorite_removed_notif)),
                        Toast.LENGTH_LONG
                    )
                        .show()
                    binding.fabAdd.setImageResource(R.drawable.ic_baseline_favorite_border)
                }
            }
            setNegativeButton(context.resources.getString(R.string.dialog_no))
            { dialog, _ ->
                dialog.cancel()
            }
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )

        private const val prefsName = "TEMP_ID"
        private const val keyId = "key_id"
        private const val imageId = "img_id"
        private const val urlId = "url_id"
    }
}