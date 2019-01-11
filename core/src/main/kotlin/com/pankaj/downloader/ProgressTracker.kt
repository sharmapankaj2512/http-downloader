package com.pankaj.downloader

interface ProgressTracker {
    fun upperBound(bound: Double)
    fun increaseBy(offset: Double)
}
