package com.skierski.github.applause

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.airbnb.epoxy.AsyncEpoxyController
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.skierski.github.applause.views.errorView
import com.skierski.github.applause.views.loadingView
import com.skierski.github.applause.views.repoView
import com.skierski.github.applause.views.searchView
import kotlinx.android.synthetic.main.fragment_repo_list.*

class RepoListFragment : BaseMvRxFragment() {

    private val viewModel by fragmentViewModel(RepoListViewModel::class)
    private val epoxyController = createEpoxyController()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_repo_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView.setController(epoxyController)
    }

    override fun invalidate() {
        recyclerView.requestModelBuild()
    }

    private fun createEpoxyController() = object : AsyncEpoxyController() {

        override fun buildModels() {
            withState(viewModel) {
                when (it.filteredReposState) {
                    is Loading ->
                        loadingView {
                            id("Loading")

                        }
                    is Success -> {
                        searchView {
                            id("Search")
                            query(it.query)
                            queryChangedListener { newQuery ->
                                viewModel.onQueryChanged(newQuery)
                            }
                        }
                        it.filteredReposState.invoke().forEach { repo ->
                            repoView {
                                id(repo.id)
                                title(repo.name)
                                clickListener(
                                    Navigation.createNavigateOnClickListener(
                                        R.id.repoDetailsFragment,
                                        RepoDetailsFragment.arg(repo)
                                    )
                                )
                            }
                        }
                    }

                    is Fail ->
                        errorView {
                            id("error")
                            message(it.filteredReposState.error.localizedMessage)
                            retryClickListener { _ ->
                                viewModel.onRetryClicked()
                            }
                        }
                    else -> {
                        // show nothing
                    }
                }
            }
        }
    }


}