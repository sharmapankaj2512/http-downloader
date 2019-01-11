package com.pankaj.downloader

import io.vlingo.actors.Definition
import io.vlingo.actors.World
import java.net.URL

fun main(args: Array<String>) {
    val world = World.startWithDefaults("http-downloader")
    val presenter = ConsoleProgressBarPresenter()

    val writerDefinition = Definition.has(DiskStreamWriterActor::class.java, Definition.NoParameters)
    val writer = world.actorFor(writerDefinition, StreamWriter::class.java)
    val trackerDefinition = Definition.has(ProgressTrackerActor::class.java, Definition.parameters(presenter))
    val tracker = world.actorFor(trackerDefinition, ProgressTracker::class.java)
    val url = URL("http://file-examples.com/wp-content/uploads/2017/04/file_example_MP4_480_1_5MG.mp4")
    val downloaderDefinition =
        Definition.has(HttpDownloaderActor::class.java, Definition.parameters(url, writer, tracker))
    val httpDownloader = world.actorFor(downloaderDefinition, HttpDownloader::class.java)

    httpDownloader.download().andThenConsume {
        world.terminate()
    }
}