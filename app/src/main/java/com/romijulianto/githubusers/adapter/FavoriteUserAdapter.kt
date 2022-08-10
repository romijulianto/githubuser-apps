package com.romijulianto.githubusers.adapter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.romijulianto.githubusers.R
import com.romijulianto.githubusers.activity.GithubUsersProfileActivity
import com.romijulianto.githubusers.database.FavoriteDatabase
import com.romijulianto.githubusers.database.FavoriteRoomDB
import com.romijulianto.githubusers.databinding.ItemGithubUserFavouriteBinding
import com.squareup.picasso.Picasso

class FavoriteUserAdapter(private val listUser: List<FavoriteDatabase>) :
    RecyclerView.Adapter<FavoriteUserAdapter.ViewHolder>() {

    private lateinit var sharedPref: SharedPreferences

    override fun getItemCount(): Int = listUser.size

    inner class ViewHolder(private val binding: ItemGithubUserFavouriteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(fav: FavoriteDatabase) {
            sharedPref = itemView.context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
            binding.gitUsernameView.text = fav.login
            binding.gitUrlView.text = fav.htmlUrl
            Picasso.get().load(fav.avatarUrl).into(binding.gitImageView)
            binding.gitDelete.setOnClickListener {
                val database =
                    FavoriteRoomDB.getDatabase(itemView.context.applicationContext).favoriteDao()
                val exist = database.checkUserFavorites(fav.login.toString())

                if (exist) {
                    val title = itemView.context.resources.getString(R.string.favorite)
                    val message =
                        itemView.context.resources.getString(R.string.favorite_removed_query_1) + " " + fav.login.toString() + " " + itemView.context.resources.getString(
                            R.string.favorite_removed_query_2
                        )
                    val alertDialogBuilder = AlertDialog.Builder(itemView.context)
                    with(alertDialogBuilder) {
                        setTitle(title)
                        setMessage(message)
                        setCancelable(false)
                        setPositiveButton(context.resources.getString(R.string.dialog_yes)) { _, _ ->
                            val githubUserDBFavourite =
                                FavoriteRoomDB.getDatabase(context.applicationContext)
                                    .favoriteDao()
                            githubUserDBFavourite.delete(fav.login.toString())
                            Toast.makeText(
                                context,
                                fav.login.toString() + " " + itemView.context.resources.getString(R.string.favorite_removed_notif),
                                Toast.LENGTH_LONG
                            )
                                .show()
                        }
                        setNegativeButton(context.resources.getString(R.string.dialog_no))
                        { dialog, _ ->
                            dialog.cancel()
                        }
                    }
                    val alertDialog = alertDialogBuilder.create()
                    alertDialog.show()
                }
            }
            binding.gitShare.setOnClickListener {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, fav.login)
                    type = "text/html"
                }
                itemView.context.startActivity(sendIntent)
            }
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, GithubUsersProfileActivity::class.java)
                fav.login?.let { it1 ->
                    fav.htmlUrl?.let { it2 ->
                        fav.avatarUrl?.let { it3 ->
                            sharedPrefID(
                                it1,
                                it2, it3
                            )
                        }
                    }
                }
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view =
            ItemGithubUserFavouriteBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    private fun sharedPrefID(id: String, image: String, html: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(keyId, id)
        editor.putString(imageId, image)
        editor.putString(urlId, html)
        editor.apply()
    }

    companion object {
        private const val prefsName = "TEMP_ID"
        private const val keyId = "key_id"
        private const val imageId = "img_id"
        private const val urlId = "url_id"
    }
}