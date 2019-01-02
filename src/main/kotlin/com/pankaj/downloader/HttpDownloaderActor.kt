package com.pankaj.downloader

import io.vlingo.actors.Actor
import java.net.HttpURLConnection
import java.net.URL

class HttpDownloaderActor(
    val url: URL,
    val progressTracker: ProgressTracker
) : Actor(), HttpDownloader {
    override fun startDownload() {
        try {
            val connection = httpRangeConnection()
            connection.inputStream.use { stream ->
                progressTracker.upperBound(connection.contentLength.toDouble())
                val bytes = ByteArray(100)
                while (stream.read(bytes) != -1)
                    progressTracker.increaseBy(bytes.size.toDouble())
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun httpRangeConnection(): HttpURLConnection {
        val urlConnection = url.openConnection() as HttpURLConnection
        urlConnection.setRequestProperty("Range", "bytes=0-")
        urlConnection.connect()
        return urlConnection
    }
}
