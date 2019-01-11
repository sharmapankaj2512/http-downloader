package com.pankaj.downloader.plugin.tracker

import com.pankaj.downloader.ProgressTracker
import io.kotlintest.data.forall
import io.kotlintest.specs.StringSpec
import io.kotlintest.tables.row
import io.mockk.*
import io.vlingo.actors.Definition
import io.vlingo.actors.World
import io.vlingo.actors.testkit.TestUntil

class ProgressTrackerActorSpec : StringSpec({
    val testUntil = TestUntil.happenings(3)
    val world = World.startWithDefaults("http-downloader")
    val presenter = mockk<ProgressPresenter>()
    val definition = Definition.has(ProgressTrackerActor::class.java, Definition.parameters(presenter, testUntil))
    val tracker = world.actorFor(definition, ProgressTracker::class.java)

    "should pass correct progress to the presenter" {
        every { presenter.present(any(), any()) } just runs

        val upperBound = 80.0

        tracker.upperBound(upperBound)

        forall(
            row(10.0, 10.0),
            row(20.0, 30.0),
            row(25.0, 55.0)
        ) { increment, completed ->
            tracker.increaseBy(increment)
            verify { presenter.present(upperBound, completed) }
        }
    }
})