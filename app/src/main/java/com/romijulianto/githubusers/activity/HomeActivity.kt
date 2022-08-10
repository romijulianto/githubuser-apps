package com.romijulianto.githubusers.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.romijulianto.githubusers.R
import com.romijulianto.githubusers.adapter.GithubUsersAdapter
import com.romijulianto.githubusers.databinding.ActivityHomeBinding
import com.romijulianto.githubusers.datastore.SettingPreferences
import com.romijulianto.githubusers.userdata.GithubUsersArray
import com.romijulianto.githubusers.viewmodel.HomeActivityViewModel
import com.romijulianto.githubusers.viewmodel.SettingPreferencesViewModel
import com.romijulianto.githubusers.viewmodel.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class HomeActivity : AppCompatActivity() {
    private val listGitHubUser = ArrayList<GithubUsersArray>()
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SettingPreferences.getInstance(dataStore)
        val homeViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            HomeActivityViewModel::class.java
        )
        val settingsViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            SettingPreferencesViewModel::class.java
        )
        val layoutManager = LinearLayoutManager(this)
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = binding.gitSearch

        settingsViewModel.getThemeSettings().observe(this,
            { isDarkModeActive: Boolean ->
                if (isDarkModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            })

        homeViewModel.githubUserArray.observe(this, { userArray ->
            setGitHubUserData(userArray)
        })

        homeViewModel.isLoading.observe(this, {
            showLoading(it)
        })

        homeViewModel.isToast.observe(this, { isToast ->
            showToast(isToast, homeViewModel.toastReason.value.toString())
        })

        binding.listGithubUser.layoutManager = layoutManager
        binding.listGithubUser.addItemDecoration(itemDecoration)

        title = resources.getString(R.string.app_name)

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.git_search)
        searchView.setQuery("", false)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                listGitHubUser.clear()
                homeViewModel.findGitHubUserID(query)
                val adapter = GithubUsersAdapter(listGitHubUser)
                binding.listGithubUser.adapter = adapter
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                listGitHubUser.clear()
                homeViewModel.findGitHubUserID(newText)
                val adapter = GithubUsersAdapter(listGitHubUser)
                binding.listGithubUser.adapter = adapter
                return true
            }
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

    private fun setGitHubUserData(listGithubUserID: List<GithubUsersArray>) {
        val listReview = ArrayList<GithubUsersArray>()
        for (userID in listGithubUserID) {
            val user = GithubUsersArray(userID.login, userID.htmlUrl, userID.avatarUrl)
            listReview.add(user)
        }
        val adapter = GithubUsersAdapter(listReview)
        binding.listGithubUser.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(isToast: Boolean, toastReason: String) {
        if (!isToast) {
            Toast.makeText(this, toastReason, Toast.LENGTH_LONG).show()
        }
    }
}