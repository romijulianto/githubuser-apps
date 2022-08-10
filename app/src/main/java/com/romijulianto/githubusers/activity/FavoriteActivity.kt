package com.romijulianto.githubusers.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.romijulianto.githubusers.R
import com.romijulianto.githubusers.adapter.FavoriteUserAdapter
import com.romijulianto.githubusers.database.FavoriteDatabase
import com.romijulianto.githubusers.databinding.ActivityFavoriteBinding
import com.romijulianto.githubusers.viewmodel.FavoriteDatabaseViewModel

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private val listReview = ArrayList<FavoriteDatabase>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)

        binding.listGithubUser.layoutManager = layoutManager
        binding.listGithubUser.addItemDecoration(itemDecoration)

        title = resources.getString(R.string.favorite)

        binding.progressBar.visibility = View.VISIBLE

        val favoriteViewModel =
            ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
                FavoriteDatabaseViewModel::class.java
            )

        favoriteViewModel.getGitHubUserFavoriteData(this)
            .observe(this, { githubUserFavoriteList ->
                if (githubUserFavoriteList.isEmpty()) {
                    listReview.clear()
                    binding.listGithubUser.adapter = null
                    Toast.makeText(
                        this,
                        resources.getString(R.string.favorite_empty_list),
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    setGitHubUserFavouriteData(githubUserFavoriteList)
                }
                binding.progressBar.visibility = View.GONE
            })
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

    private fun setGitHubUserFavouriteData(listGithubUserID: List<FavoriteDatabase>) {
        val listReview = ArrayList<FavoriteDatabase>()
        for (userID in listGithubUserID) {
            val user = FavoriteDatabase(userID.id, userID.login, userID.htmlUrl, userID.avatarUrl)
            listReview.add(user)
        }
        val adapter = FavoriteUserAdapter(listReview)
        binding.listGithubUser.adapter = adapter
    }
}