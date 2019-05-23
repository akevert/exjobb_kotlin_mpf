package com.example.exjobb_mpf

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedcode.GithubApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.sharedcode.Repo

class ReposActivity : AppCompatActivity(){
    private lateinit var adapter: RepoListAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: RecyclerView.LayoutManager

    private fun updateRepos(results: List<Repo>) {
        adapter.dataSet = results
        adapter.notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repos)
        recyclerView = findViewById(R.id.recycler_view_repos)
        adapter = RepoListAdapter()
        recyclerView.adapter = adapter
        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        val api = GithubApi()
        api.getRepos(
            intent.getStringExtra("repos_url"),
            success = { CoroutineScope(Dispatchers.Main).launch { updateRepos(it) } },
            failure = {})
    }
}