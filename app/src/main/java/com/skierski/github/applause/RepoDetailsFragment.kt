package com.skierski.github.applause

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.airbnb.mvrx.MvRx
import com.airbnb.mvrx.args
import com.skierski.github.applause.model.Repo
import kotlinx.android.synthetic.main.fragment_repo_details.*

class RepoDetailsFragment : Fragment() {

    private val repo: Repo by args()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_repo_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        repo_name.text = getString(R.string.repo_name, repo.name)
        repo_description.text = getString(R.string.repo_description, repo.description)
    }

    companion object {
        fun arg(repo: Repo) = Bundle().apply {
            putParcelable(MvRx.KEY_ARG, repo)
        }
    }
}