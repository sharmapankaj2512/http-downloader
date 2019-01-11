package com.pankaj.downloader.plugin.tracker

open class ProgressBarPresenter(val render: (String) -> Unit, val scalingFactor: Int = 10) :
    ProgressPresenter {
    private val upperBound = 100
    private val scaledUpperBound = upperBound / scalingFactor

    override fun present(total: Double, completed: Double) {
        val percentage = completed / total * 100
        val scaledPercentage = (percentage / scalingFactor).toInt()
        val filled = repeat("=", scaledPercentage)
        val empty = repeat(" ", scaledUpperBound - scaledPercentage)
        render("""[$filled$empty]""")
    }

    fun repeat(char: String, count: Int): String {
        if (count <= 0) return ""
        return char + repeat(char, count - 1)
    }
}
