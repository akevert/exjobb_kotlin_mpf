package com.example.exjobb_mpf

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedcode.Repo

class RepoListAdapter: RecyclerView.Adapter<RepoListAdapter.RepoHolder>() {
    var dataSet = listOf<Repo>()
    override fun getItemCount(): Int = dataSet.size

    override fun onBindViewHolder(holder: RepoHolder, position: Int) {
        holder.repoView.findViewById<TextView>(R.id.repo_name).text = dataSet[position].name
        holder.repoView.findViewById<TextView>(R.id.watchcount).text = dataSet[position].watchers_count.toString()
        holder.repoView.findViewById<TextView>(R.id.starcount).text = dataSet[position].stargazers_count.toString()
        holder.repoView.findViewById<TextView>(R.id.forkcount).text = dataSet[position].forks_count.toString()
    }

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): RepoHolder {
        val repo = LayoutInflater.from (parent.context).inflate(R.layout.repos_view, parent, false) as
                CardView
        return RepoHolder(repo)
    }

    class RepoHolder(var repoView: CardView) : RecyclerView.ViewHolder(repoView)
}