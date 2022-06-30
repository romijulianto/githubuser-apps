package com.romijulianto.githubsuserapps.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.romijulianto.githubsuserapps.R
import com.romijulianto.githubsuserapps.activity.DetailUserActivity.Companion.EXTRA_DETAIL
import com.romijulianto.githubsuserapps.adapter.ListUserAdapter
import com.romijulianto.githubsuserapps.model.UserGithubs
import com.romijulianto.githubsuserapps.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var rvUserGithubs: RecyclerView

    private val list = ArrayList<UserGithubs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        rvUserGithubs = binding.rvUserGithubs
        rvUserGithubs.setHasFixedSize(true)

        list.addAll(listUsers)
        showRecyclerView()

        binding.ivProfile.setOnClickListener(this)
    }


    private val listUsers: ArrayList<UserGithubs>
        @SuppressLint("Recycle")
        get() {
            val dataUsername = resources.getStringArray(R.array.username)
            val dataName = resources.getStringArray(R.array.name)
            val dataLocation = resources.getStringArray(R.array.location)
            val dataRepository = resources.getStringArray(R.array.repository)
            val dataCompany = resources.getStringArray(R.array.company)
            val dataFollowers = resources.getStringArray(R.array.followers)
            val dataFollowing = resources.getStringArray(R.array.following)
            val dataAvatar = resources.obtainTypedArray(R.array.avatar)

            val listUsers = ArrayList<UserGithubs>()

            for (i in dataUsername.indices) {
                val user = UserGithubs(
                    dataUsername[i],
                    dataName[i],
                    dataLocation[i],
                    dataRepository[i].toInt(),
                    dataCompany[i],
                    dataFollowers[i].toInt(),
                    dataFollowing[i].toInt(),
                    dataAvatar.getResourceId(i, -1)
                )
                listUsers.add(user)
            }

            return listUsers
        }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_profile -> {

                val profile = UserGithubs(
                    getString(R.string.my_username),
                    getString(R.string.my_name),
                    getString(R.string.my_location),
                    getString(R.string._50).toInt(),
                    getString(R.string.my_company),
                    getString(R.string._24).toInt(),
                    getString(R.string._24).toInt(),
                    R.drawable.super_user
                )

                Intent(this, DetailUserActivity::class.java).apply {
                    putExtra(EXTRA_DETAIL, profile)
                }.also {
                    startActivity(it)
                }
            }
        }
    }

    private fun showRecyclerView() {
        rvUserGithubs.layoutManager = LinearLayoutManager(this)

        val listUserAdapter = ListUserAdapter(list)
        rvUserGithubs.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(user: UserGithubs) {
                goToDetailUser(user)
            }

        })
    }

    private fun goToDetailUser(user: UserGithubs) {
        Intent(this, DetailUserActivity::class.java).apply {
            putExtra(EXTRA_DETAIL, user)
        }.also {
            startActivity(it)
        }
    }
}