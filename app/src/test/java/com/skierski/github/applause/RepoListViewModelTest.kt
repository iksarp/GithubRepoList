package com.skierski.github.applause

import com.airbnb.mvrx.withState
import com.nhaarman.mockitokotlin2.clearInvocations
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.skierski.github.applause.model.Repo
import io.reactivex.Single
import junit.framework.Assert.assertEquals
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class RepoListViewModelTest {

    @get:Rule
    val rxRule = ReactiveXUtils().getTestRule()

    val gitHubRepository: GitHubRepository = mock {
        on { listRepos(GITHUB_USER) } doReturn Single.just(emptyList())
    }

    val tested by lazy { RepoListViewModel(RepoListState(), gitHubRepository) }

    @Test
    fun `should start fetching list when created`() {
        // when
        tested

        // then
        verify(gitHubRepository).listRepos(GITHUB_USER)
    }

    @Test
    fun `should filter repos by name when query changed`() {
        // given
        val repo1 = repoWithName("abc")
        val repo2 = repoWithName("dfg")
        whenever(gitHubRepository.listRepos(GITHUB_USER)).thenReturn(Single.just(listOf(repo1, repo2)))

        // when
        tested.onQueryChanged("d")

        // then
        withState(tested) { state ->
            assertEquals(listOf(repo2), state.filteredReposState.invoke())
        }
    }

    @Test
    fun `should not change all repos when filtering happened`() {
        // given
        val repo1 = repoWithName("abc")
        val repo2 = repoWithName("dfg")
        whenever(gitHubRepository.listRepos(GITHUB_USER)).thenReturn(Single.just(listOf(repo1, repo2)))

        // when
        tested.onQueryChanged("d")

        // then
        withState(tested) { state ->
            assertEquals(listOf(repo1, repo2), state.allReposState.invoke())
        }
    }

    @Test
    fun `should filter repos by name ignoging when query changed`() {
        // given
        val repo1 = repoWithName("abc")
        val repo2 = repoWithName("dfg")
        whenever(gitHubRepository.listRepos(GITHUB_USER)).thenReturn(Single.just(listOf(repo1, repo2)))

        // when
        tested.onQueryChanged("D")

        // then
        withState(tested) { state ->
            assertEquals(listOf(repo2), state.filteredReposState.invoke())
        }
    }

    @Test
    fun `should return all repos by name when query is empty`() {
        // given
        val repo1 = repoWithName("abc")
        val repo2 = repoWithName("dfg")
        whenever(gitHubRepository.listRepos(GITHUB_USER)).thenReturn(Single.just(listOf(repo1, repo2)))

        // when
        tested.onQueryChanged("")

        // then
        withState(tested) { state ->
            assertEquals(listOf(repo1, repo2), state.filteredReposState.invoke())
        }
    }

    @Test
    fun `should fetch repos when retry clicked`() {
        // given
        tested
        clearInvocations(gitHubRepository)

        // when
        tested.onRetryClicked()

        // then
        verify(gitHubRepository).listRepos(GITHUB_USER)
    }

    fun repoWithName(name: String) = Repo(id = 123, name = name, description = "desc")
}