package com.pankaj.downloader

interface ProgressPresenter {
    fun present(total: Double, completed: Double)
}