package com.pankaj.downloader

import com.pankaj.downloader.http.HttpRangeConnection
import io.vlingo.actors.Actor
import io.vlingo.common.Completes
import java.io.InputStream
import java.net.URL

class HttpDownloaderActor(
    private val url: URL,
    private val streamWriter: StreamWriter,
    private val tracker: ProgressTracker
) : Actor(), HttpDownloader {

    override fun download(): Completes<Boolean> {
        val connection = HttpRangeConnection(url)
        val inputStream = connection.stream()
        val contentLength = connection.contentLength().toDouble()
        tracker.upperBound(contentLength)
        return download(inputStream)
    }

    private fun download(stream: InputStream): Completes<Boolean> {
        val buffer = ByteArray(1000)
        var size = stream.read(buffer)
        while (size != -1) {
            streamWriter.append(buffer.copyOfRange(0, size)).andThenConsume {
                tracker.increaseBy(it)
            }
            size = stream.read(buffer)
        }
        streamWriter.flush().andThenConsume {
            completesEventually().with(true)
        }
        return completes<Boolean>()
    }
}
