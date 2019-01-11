package com.pankaj.downloader.plugin.tracker

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class ProgressBarPresenterSpec : StringSpec({
    "should convert progress to progress bar with default scaling factor" {
        ProgressBarPresenter({ result ->
            result shouldBe "[=         ]"
        }).present(100.0, 10.0)
    }

    "should convert progress to progress bar with given scaling factor" {
        ProgressBarPresenter({ result ->
            result shouldBe "[==                  ]"
        }, 5).present(100.0, 10.0)
    }
})