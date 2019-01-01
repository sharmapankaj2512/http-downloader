package com.pankaj.downloader

import io.vlingo.actors.Actor

class ProgressTrackerActor(val progressPresenter: ProgressPresenter) : Actor(), ProgressTracker {

    private var upperBound: Double = 0.0
    private var completed: Double = 0.0

    override fun upperBound(bound: Double) {
        this.upperBound = bound
    }

    override fun increaseBy(offset: Double) {
        completed += offset
        progressPresenter.present(upperBound, completed)
    }
}
