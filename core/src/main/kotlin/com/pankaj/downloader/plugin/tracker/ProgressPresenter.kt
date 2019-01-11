package com.pankaj.downloader.plugin.tracker

interface ProgressPresenter {
    fun present(total: Double, completed: Double)
}