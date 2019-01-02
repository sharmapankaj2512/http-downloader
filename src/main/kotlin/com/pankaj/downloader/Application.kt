package com.pankaj.downloader

import io.vlingo.actors.Definition
import io.vlingo.actors.World
import java.net.URL

fun main(args: Array<String>) {
    val world = World.startWithDefaults("http-downloader")
    val presenter = ConsoleProgressBarPresenter()

    val trackerDefinition = Definition.has(ProgressTrackerActor::class.java, Definition.parameters(presenter))
    val tracker = world.actorFor(trackerDefinition, ProgressTracker::class.java)
    val url = URL("https://speed.hetzner.de/100MB.bin")
    val downloaderDefinition = Definition.has(HttpDownloaderActor::class.java, Definition.parameters(url, tracker))
    val httpDownloader = world.actorFor(downloaderDefinition, HttpDownloader::class.java)
    httpDownloader.startDownload()
}