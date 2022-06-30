package com.romijulianto.githubsuserapps.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.romijulianto.githubsuserapps.model.UserGithubs
import com.bumptech.glide.Glide
import com.romijulianto.githubsuserapps.R
import com.romijulianto.githubsuserapps.databinding.UserCardBinding

class ListUserAdapter(private val listUser: ArrayList<UserGithubs>) :
    RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(var binding: UserCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val binding = UserCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val user = listUser[position]

        holder.binding.apply {
            cardTvName.text = user.name
            cardTvCompany.text = user.company

            Glide
                .with(holder.itemView.context)
                .load(user.avatar)
                .placeholder(R.drawable.ic_user_profile)
                .into(cardImageProfile)
        }

        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(user) }
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    interface OnItemClickCallback {
        fun onItemClicked(user: UserGithubs)
    }
}