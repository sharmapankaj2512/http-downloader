package com.pankaj.downloader.plugin.tracker

import com.pankaj.downloader.ProgressTracker
import io.vlingo.actors.Actor
import io.vlingo.actors.testkit.TestUntil

class ProgressTrackerActor(private val progressPresenter: ProgressPresenter,
                           private val testUtil: TestUntil = TestUntil.happenings(0)) : Actor(),
    ProgressTracker {

    private var upperBound: Double = 0.0
    private var completed: Double = 0.0

    override fun upperBound(bound: Double) {
        this.upperBound = bound
    }

    override fun increaseBy(offset: Double) {
        completed += offset
        progressPresenter.present(upperBound, completed)
        testUtil.happened()
    }
}
