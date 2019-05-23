package com.example.sharedcode

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.list

internal expect val ApplicationDispatcher: CoroutineDispatcher
expect class Image

fun usersToUsersWithAvatars(users: List<User>): List<UserWithAvatar>  {
    val usersWithAvatars: MutableList<UserWithAvatar> = mutableListOf()
    for (user in users){
        usersWithAvatars += UserWithAvatar(user, null)
    }
    return usersWithAvatars
}

class GithubApi {
    private val client = HttpClient()
    fun getUsers(success: (List<UserWithAvatar>) -> Unit, failure: (Exception) -> Unit, userName: String) {
        val url = "https://api.github.com/search/users?q=$userName&per_page=30"
        GlobalScope.launch(ApplicationDispatcher) {
            var usersWithAvatars: List<UserWithAvatar> = listOf()
            try {
                val json = client.get<String>(url)
                val users = Json.nonstrict.parse(Results.serializer(), json).items
                usersWithAvatars = usersToUsersWithAvatars(users)
            } catch (ex: Exception) {
                println("exception when fetching users: $ex")
                failure(ex)
            }
            success(usersWithAvatars)
        }
    }

    fun getRepos (reposUrl: String, success: (List<Repo>) -> Unit, failure: (Throwable?) -> Unit) {
        GlobalScope.launch (ApplicationDispatcher) {
            try {
                val json = client.get<String>(reposUrl)
                Json.nonstrict.parse(Repo.serializer().list, json)
                    .also(success)
            } catch (ex: Exception) {
                println("Exception when fetching repos: $ex")
                failure(ex)
            }
        }
    }
}

@Serializable
data class User (val login: String,
                 val id: Int,
                 val avatar_url: String,
                 val repos_url: String)

@Serializable
data class Results(val total_count: Int,
                   val incomplete_results: Boolean,
                   val items: List<User>)

data class UserWithAvatar (val user: User,
                           var avatar: Image?)

@Serializable
data class Repo (val id: Int,
                 val description: String?,
                 val name: String,
                 val watchers_count: Int,
                 val forks_count: Int,
                 val stargazers_count: Int)
