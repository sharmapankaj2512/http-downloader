package com.pankaj.downloader

class ConsoleProgressBarPresenter : ProgressBarPresenter({ result -> print("\r\b Downloaded progress $result") })
