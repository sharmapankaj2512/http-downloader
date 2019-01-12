package com.pankaj.downloader.app

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.pankaj.downloader.*
import com.pankaj.downloader.app.presenter.ConsoleProgressBarPresenter
import com.pankaj.downloader.plugin.tracker.ProgressTrackerActor
import com.pankaj.downloader.plugin.writer.DiskStreamWriterActor
import io.vlingo.actors.Definition
import io.vlingo.actors.World
import java.io.File
import java.net.URL
import java.nio.file.Paths

class Application: CliktCommand() {
    val url: String by option(help="Http url of the resource to be downloaded").required()
    val path: String by option(help="Path where downloaded file will be saved").default("/tmp/http-downloader")

    override fun run() {
        val url = URL(url)
        val fileName = Paths.get(url.path).fileName.toString()
        val downloadPath = Paths.get(path, fileName).toAbsolutePath().toString()
        File(path).mkdirs()
        start(downloadPath, url)
    }

    private fun start(downloadPath: String, url: URL) {
        val presenter = ConsoleProgressBarPresenter()
        val world = World.startWithDefaults("http-downloader")
        val writerDefinition = Definition.has(DiskStreamWriterActor::class.java, Definition.parameters(downloadPath))
        val writer = world.actorFor(writerDefinition, StreamWriter::class.java)
        val trackerDefinition = Definition.has(ProgressTrackerActor::class.java, Definition.parameters(presenter))
        val tracker = world.actorFor(trackerDefinition, ProgressTracker::class.java)
        val downloaderDefinition =
            Definition.has(HttpDownloaderActor::class.java, Definition.parameters(url, writer, tracker))
        val httpDownloader = world.actorFor(downloaderDefinition, HttpDownloader::class.java)

        httpDownloader.download().andThenConsume {
            world.terminate()
        }
    }
}

fun main(args: Array<String>) = Application().main(args)