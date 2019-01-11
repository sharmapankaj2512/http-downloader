package com.pankaj.downloader

import io.vlingo.common.Completes

interface StreamWriter {
    fun append(data: ByteArray): Completes<Double>
    fun flush(): Completes<Boolean>
}