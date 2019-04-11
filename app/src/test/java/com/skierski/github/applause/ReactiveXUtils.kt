package com.skierski.github.applause

import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.rules.ExternalResource

class ReactiveXUtils {

    private fun setupJavaSchedulers(scheduler: Scheduler) {
        RxJavaPlugins.setIoSchedulerHandler { scheduler }
        RxJavaPlugins.setComputationSchedulerHandler { scheduler }
        RxJavaPlugins.setNewThreadSchedulerHandler { scheduler }
        RxJavaPlugins.setSingleSchedulerHandler { scheduler }
    }

    private fun setupAndroidSchedulers(scheduler: Scheduler) {
        RxAndroidPlugins.setMainThreadSchedulerHandler { scheduler }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler }
    }

    private fun resetSchedulers() {
        RxAndroidPlugins.reset()
        RxJavaPlugins.reset()
    }

    fun getTestRule(scheduler: Scheduler = Schedulers.trampoline()) = object : ExternalResource() {
        override fun before() {
            setupAndroidSchedulers(scheduler)
            setupJavaSchedulers(scheduler)
        }

        override fun after() {
            resetSchedulers()
        }
    }
}