package com.pankaj.downloader

open class ProgressBarPresenter(val render: (String) -> Unit, val scalingFactor: Int = 10) : ProgressPresenter {
    private val upperBound = 100
    private val scaledUpperBound = upperBound / scalingFactor

    override fun present(total: Double, completed: Double) {
        val percentage = completed / total * 100
        val scaledPercentage = (percentage / scalingFactor).toInt()
        val filled = "=".repeat(scaledPercentage)
        val empty = " ".repeat(scaledUpperBound - scaledPercentage)
        render("""[$filled$empty]""")
    }
}
