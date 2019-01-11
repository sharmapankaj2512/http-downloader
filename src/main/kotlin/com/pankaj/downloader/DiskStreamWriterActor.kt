package com.pankaj.downloader

import io.vlingo.actors.Actor
import io.vlingo.common.Completes
import java.io.*

class DiskStreamWriterActor(fullPath: String) : Actor(), StreamWriter {
    private val outputStream = BufferedOutputStream(FileOutputStream(fullPath))

    override fun append(data: ByteArray): Completes<Double> {
        outputStream.write(data)
        return completes<Double>().with(data.size.toDouble())
    }

    override fun flush(): Completes<Boolean> {
        outputStream.flush()
        outputStream.close()
        return completes<Boolean>().with(true)
    }
}