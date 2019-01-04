package com.pankaj.downloader

import io.vlingo.common.Completes

interface StreamWriter {
    fun append(bytes: ByteArray): Completes<Double>
    fun flush(): Completes<Boolean>
}