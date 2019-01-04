package com.pankaj.downloader

import io.vlingo.actors.Actor
import io.vlingo.common.Completes
import java.io.*

class DiskStreamWriterActor : Actor(), StreamWriter {
    private val outputStream = BufferedOutputStream(FileOutputStream("/tmp/test"))

    override fun append(bytes: ByteArray): Completes<Double> {
        outputStream.write(bytes)
        return completes<Boolean>().with(bytes.size.toDouble())
    }

    override fun flush(): Completes<Boolean> {
        outputStream.flush()
        outputStream.close()
        return completes<Boolean>().with(true)
    }
}