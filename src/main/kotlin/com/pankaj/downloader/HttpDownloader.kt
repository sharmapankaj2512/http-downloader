package com.pankaj.downloader

import io.vlingo.common.Completes

interface HttpDownloader {
    fun download(): Completes<Boolean>
}
