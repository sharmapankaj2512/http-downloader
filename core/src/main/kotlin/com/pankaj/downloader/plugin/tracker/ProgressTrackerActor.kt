package com.pankaj.downloader.plugin.tracker

import com.pankaj.downloader.ProgressTracker
import io.vlingo.actors.Actor
import io.vlingo.actors.testkit.TestUntil

class ProgressTrackerActor(private val progressPresenter: ProgressPresenter) : Actor(),
    ProgressTracker {
    private var upperBound: Double = 0.0
    private var completed: Double = 0.0
    private var testUntil: TestUntil = TestUntil.happenings(0)

    constructor(progressPresenter: ProgressPresenter, testUtil: TestUntil): this(progressPresenter) {
        this.testUntil = testUtil
    }

    override fun upperBound(bound: Double) {
        this.upperBound = bound
    }

    override fun increaseBy(offset: Double) {
        completed += offset
        progressPresenter.present(upperBound, completed)
        testUntil.happened()
    }
}
