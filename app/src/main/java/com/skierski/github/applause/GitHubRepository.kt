package com.skierski.github.applause

import com.skierski.github.applause.model.Repo
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubRepository {
    @GET("users/{user}/repos")
    fun listRepos(@Path("user") user: String, @Query("per_page") count: Int): Single<List<Repo>>
}