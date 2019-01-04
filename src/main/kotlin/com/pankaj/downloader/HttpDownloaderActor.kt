package com.pankaj.downloader

import io.vlingo.actors.Actor
import io.vlingo.common.Completes
import java.io.File
import java.io.InputStream
import java.net.URL

class HttpDownloaderActor(
    private val url: URL,
    private val streamWriter: StreamWriter,
    private val tracker: ProgressTracker
) : Actor(), HttpDownloader {

    override fun startDownload(): Completes<Boolean> {
        val connection = HttpRangeConnection(url)
        val inputStream = connection.stream()
        val contentLength = connection.contentLength().toDouble()
        tracker.upperBound(contentLength)
        download(inputStream)
        return completes<Boolean>()
    }

    private fun download(stream: InputStream) {
        var bytes = ByteArray(1000)
        while (stream.read(bytes) != -1) {
            streamWriter.append(bytes.clone()).andThenConsume {
                tracker.increaseBy(it)
            }
            bytes = ByteArray(1000)
        }
        streamWriter.flush().andThenConsume {
            completesEventually().with(true)
        }
    }
}
