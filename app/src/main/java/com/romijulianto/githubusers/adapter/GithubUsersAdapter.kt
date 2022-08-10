package com.romijulianto.githubusers.adapter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.romijulianto.githubusers.R
import com.romijulianto.githubusers.activity.GithubUsersProfileActivity
import com.romijulianto.githubusers.database.FavoriteDatabase
import com.romijulianto.githubusers.database.FavoriteRoomDB
import com.romijulianto.githubusers.databinding.ItemGithubUserBinding
import com.romijulianto.githubusers.userdata.GithubUsersArray
import com.squareup.picasso.Picasso

class GithubUsersAdapter(private val listUser: ArrayList<GithubUsersArray>) :
    RecyclerView.Adapter<GithubUsersAdapter.ListViewHolder>() {

    private lateinit var sharedPref: SharedPreferences

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemGithubUserBinding.bind(itemView)
    }

    override fun getItemCount(): Int = listUser.size

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_github_user, viewGroup, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        with(holder) {
            val (git_username, git_id, git_image) = listUser[position]
            sharedPref =
                holder.itemView.context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
            binding.gitUsernameView.text = git_username
            binding.gitUrlView.text = git_id
            Picasso.get().load(git_image).into(binding.gitImageView)
            binding.gitFavourite.setOnClickListener {
                val database =
                    FavoriteRoomDB.getDatabase(itemView.context.applicationContext).favoriteDao()
                val exist = database.checkUserFavorites(binding.gitUsernameView.text.toString())
                if (!exist) {
                    val title = itemView.context.resources.getString(R.string.favorite)
                    val message =
                        "${binding.gitUsernameView.text}" + " " + itemView.context.resources.getString(
                            R.string.favorite_not_in_list_notif
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
                            val inputFavData = FavoriteDatabase(
                                login = binding.gitUsernameView.text.toString(),
                                avatarUrl = git_image,
                                htmlUrl = binding.gitUrlView.text.toString()
                            )
                            githubUserDBFavourite.insert(inputFavData)
                            Toast.makeText(
                                context,
                                "${binding.gitUsernameView.text}" + " " + itemView.context.resources.getString(
                                    R.string.favorite_add_to_notif
                                ),
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
                } else {
                    val title = itemView.context.resources.getString(R.string.favorite)
                    val message =
                        "${binding.gitUsernameView.text}" + " " + itemView.context.resources.getString(
                            R.string.favorite_in_list_notif
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
                            githubUserDBFavourite.delete(binding.gitUsernameView.text.toString())
                            Toast.makeText(
                                context,
                                "${binding.gitUsernameView.text}" + " " + itemView.context.resources.getString(
                                    R.string.favorite_removed_notif
                                ),
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
                    putExtra(Intent.EXTRA_TEXT, git_id)
                    type = "text/html"
                }
                holder.itemView.context.startActivity(sendIntent)
            }
            holder.itemView.setOnClickListener {
                val intent = Intent(holder.itemView.context, GithubUsersProfileActivity::class.java)
                sharedPrefID(git_username, git_image, git_id)
                holder.itemView.context.startActivity(intent)
            }
        }
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