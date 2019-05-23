package com.example.exjobb_mpf

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedcode.UserWithAvatar

class UserListAdapter(private val clickListener: (UserWithAvatar) -> Unit):
    RecyclerView.Adapter<UserListAdapter.UserHolder>() {
    var dataSet: List<UserWithAvatar> = listOf()
    override fun getItemCount(): Int = dataSet.size

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        holder.userView.findViewById<TextView>(R.id.username).text = dataSet[position].user.login
        holder.userView.findViewById<ImageView>(R.id.user_image).setImageBitmap(dataSet[position].avatar)
        holder.addClickListener(dataSet[position], clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): UserHolder {
        val user = LayoutInflater.from (parent.context).inflate(R.layout.user_view, parent, false)
                as CardView
        return UserHolder(user)
    }

    class UserHolder(var userView: CardView) : RecyclerView.ViewHolder(userView) {
        fun addClickListener(user: UserWithAvatar, clickListener: (UserWithAvatar) -> Unit) {
            userView.setOnClickListener { clickListener(user)}
        }
    }
}
