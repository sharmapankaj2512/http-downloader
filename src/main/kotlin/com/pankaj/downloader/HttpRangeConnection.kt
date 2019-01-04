package com.pankaj.downloader

import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class HttpRangeConnection(private val url: URL) {
    val urlConnection = url.openConnection() as HttpURLConnection

    init {
        urlConnection.setRequestProperty("Range", "bytes=0-")
        urlConnection.connect()
    }

    fun contentLength(): Int {
        return urlConnection.contentLength
    }

    fun stream(): InputStream {
        return urlConnection.inputStream
    }
}
