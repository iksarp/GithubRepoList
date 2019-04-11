package com.skierski.github.applause

import androidx.annotation.VisibleForTesting
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.BaseMvRxViewModel
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.ViewModelContext
import com.skierski.github.applause.model.Repo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

@VisibleForTesting
const val GITHUB_USER = "applauseoss"

data class RepoListState(
    val allReposState: Async<List<Repo>> = Uninitialized,
    val filteredReposState: Async<List<Repo>> = Uninitialized,
    val query: String = ""
) : MvRxState

class RepoListViewModel(
    initialState: RepoListState,
    private val gitHubRepository: GitHubRepository
) : BaseMvRxViewModel<RepoListState>(initialState, debugMode = false) {

    companion object : MvRxViewModelFactory<RepoListViewModel, RepoListState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: RepoListState
        ): RepoListViewModel {
            /**
             * Note: normally some framework for dependency injection and management should be used
             * But it does not make sense for purposes of this demo
             */
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

            val gitHubRepository = retrofit.create(GitHubRepository::class.java)
            return RepoListViewModel(state, gitHubRepository)
        }
    }

    init {
        fetchRepos()
    }

    fun onRetryClicked() {
        fetchRepos()
    }

    fun onQueryChanged(newQuery: String) {
        setState {
            copy(
                query = newQuery,
                filteredReposState = computeFilteredReposState(allReposState, newQuery)
            )
        }
    }

    private fun fetchRepos() {
        gitHubRepository.listRepos(user = GITHUB_USER)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .execute {
                copy(
                    allReposState = it,
                    filteredReposState = computeFilteredReposState(it, query)
                )
            }
    }

    private fun computeFilteredReposState(allReposState: Async<List<Repo>>, query: String): Async<List<Repo>> {
        val allRepos: List<Repo>? = allReposState.invoke()
        return if (allRepos != null) {
            Success(filterRepos(allRepos, query))
        } else {
            allReposState
        }
    }

    private fun filterRepos(allRepos: List<Repo>, query: String): List<Repo> {
        return if (query.isEmpty()) {
            allRepos
        } else {
            allRepos.filter { it.name.contains(query, ignoreCase = true) }
        }
    }
}