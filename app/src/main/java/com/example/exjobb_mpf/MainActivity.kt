package com.example.exjobb_mpf

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedcode.GithubApi
import com.example.sharedcode.UserWithAvatar
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.request.get
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: UserListAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private val ADD_TASK_REQUEST = 1
    private val uiScope = CoroutineScope(Dispatchers.Main)

    fun updateUsers (users: List<UserWithAvatar>) {
        adapter.dataSet = users
        adapter.notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val searchBox: SearchView = findViewById(R.id.searchUserBox)
        searchBox.queryHint = "Search for user"

        recyclerView = findViewById(R.id.users)
        adapter = UserListAdapter { user : UserWithAvatar -> userClicked(user) }
        recyclerView.adapter = adapter
        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        searchBox.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(userName: String): Boolean {
                val api = GithubApi()
                api.getUsers(
                    success = {
                        getAvatars(it, success = {
                            uiScope.launch { updateUsers(it) }
                        }, failure =  {
                            println("Exception: $it")
                        })
                    },
                    failure = {},
                    userName = userName
                )
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }

    private fun userClicked(user: UserWithAvatar) {
        val intent = Intent(this, ReposActivity::class.java)
        intent.putExtra("repos_url", user.user.repos_url)
        startActivityForResult(intent, ADD_TASK_REQUEST)
    }


    //Unable to put this in shared library. Errors occurred in avatars in iOS app, when using Kotlin code.s
    fun getAvatars (users: List<UserWithAvatar>, success: (List<UserWithAvatar>) -> Unit,
                    failure: (Throwable?) -> Unit){
        val client = HttpClient(Android)
        GlobalScope.launch(Dispatchers.Default){
            try {
                for (user in users) {
                    val byteArray = client.get<ByteArray>(user.user.avatar_url)
                    val image = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                    user.avatar = image
                }
            } catch (ex: Exception) {
                failure(ex)
            }
            success(users)
        }
    }
}
